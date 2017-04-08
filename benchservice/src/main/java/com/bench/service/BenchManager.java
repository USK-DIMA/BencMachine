package com.bench.service;

import com.bench.service.entity.WorkPackage;
import com.bench.service.model.AbstractBenchObject;
import com.bench.service.model.Background;
import com.bench.service.model.Wood;
import javafx.geometry.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Consumer;

public class BenchManager extends Thread implements IBench {

    enum BenchObjectKey {
        KNIFE, BACKGROUND, WOOD
    }

    private static final Logger logger = LoggerFactory.getLogger(BenchManager.class);

    private final int width;
    private final int height;

    private static final int FPS = 33;
    private BufferedImage imageXY;
    private BufferedImage imageXZ;
    private Graphics2D gXY;
    private Graphics2D gXZ;
    private Point2D positionImageXY;
    private Point2D positionImageXZ;
    private Point2D offsetImages;

    private JPanel viewPanel;
    private LinkedHashMap<BenchObjectKey, AbstractBenchObject> benchObjects;
    private EndWorkListener endWorkListener;

    public BenchManager(JPanel panel) {
        logger.info("Create BenchManager");
        viewPanel = panel;
        width = viewPanel.getSize().width;
        height = viewPanel.getSize().height;
        calculateImagesPosition();
        initBenchObjects();
    }

    /**
     * Расчитиывает координаты расположения BufferedImage,
     * в которых будет происходить отрисовка бруска и ножа
     */
    private void calculateImagesPosition() {
        int padding = 10;
        int imageHeight = (height - 3 * padding) / 2; //два края и между изображениями
        int imageWidth = (width - 2 * padding); //два края
        positionImageXY = new Point(padding, padding);
        positionImageXZ = new Point(padding, 2 * padding + imageHeight);

        offsetImages = new Point(imageWidth, imageHeight);
    }


    private void initBenchObjects() {
        logger.info("Init BenchObjects");
        benchObjects = new LinkedHashMap<>();
        benchObjects.put(BenchObjectKey.BACKGROUND, new Background(offsetImages));
    }

    @Override
    public void setEndWorkListener(EndWorkListener endWorkListener) {
        this.endWorkListener = endWorkListener;
    }

    @Override
    public void run() {
        imageXY = new BufferedImage((int) offsetImages.getX(), (int) offsetImages.getY(), BufferedImage.TYPE_INT_RGB);
        imageXZ = new BufferedImage((int) offsetImages.getX(), (int) offsetImages.getY(), BufferedImage.TYPE_INT_RGB);
        gXY = imageXY.createGraphics();
        gXZ = imageXZ.createGraphics();
        while (isRun(FPS)) {
            benchUpdate();
            benchRender();
        }
    }

    private void benchUpdate() {
        benchObjectsForEach(AbstractBenchObject::update);
    }

    private void benchRender() {
        benchObjectsForEach(o -> o.drawXY(gXY));
        benchObjectsForEach(o -> o.drawXZ(gXZ));
        benchObjectsForEach(o -> o.afterDrawXY(gXY));
        benchObjectsForEach(o -> o.afterDrawXZ(gXZ));
        drawOnFrame();
    }

    /**
     * Метод выполняет действие forEachAction для каждого элемента из benchObjects
     * @param forEachAction
     */
    private void benchObjectsForEach(Consumer<AbstractBenchObject> forEachAction){
        benchObjects.entrySet().stream().map(e->e.getValue()).forEach(forEachAction);
    }

    private void drawOnFrame() {
        viewPanel.getGraphics().drawImage(imageXY, (int) positionImageXY.getX(), (int) positionImageXY.getY(), null);
        viewPanel.getGraphics().drawImage(imageXZ, (int) positionImageXZ.getX(), (int) positionImageXZ.getY(), null);
        viewPanel.getGraphics().dispose();
    }

    private boolean isRun(int pause) {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void startWork(WorkPackage workPackage) {
        logger.info("Start Work: " + workPackage);
        Point3D weedSize = workPackage.getWeedSize();
        double scaleXY = calculateScale(offsetImages, (int) weedSize.getY(), (int) weedSize.getX(), 5);
        double scaleXZ = calculateScale(offsetImages, (int) weedSize.getX(), (int) weedSize.getZ(), 5);
        Wood wood = new Wood(offsetImages, weedSize, scaleXY, scaleXZ);
        addBenchObject(BenchObjectKey.WOOD, wood);
    }

    private void addBenchObject(BenchObjectKey key, AbstractBenchObject benchObject) {
        benchObjects.put(key, benchObject);
    }

    /**
     * Расчитиывает коэффициент масштабирования для объекта, чтобы он целиок уместился
     * на изображении
     * @param imageOffset доступные размеры изображения
     * @param objectX размер изорбражения по ширине
     * @param objectY размер изображения по высоте
     * @param padding какой отступ должен быть от краёв в процентах
     * @return
     */
    private double calculateScale(Point2D imageOffset, int objectX, int objectY, int padding) {
        if(padding<0 || padding>99){
            padding = 0;
        }
        int imgX = (int) imageOffset.getX()*(100-2*padding)/100;
        int imgY = (int) imageOffset.getY()*(100-2*padding)/100;
        double dx = ((double)imgX)/objectX;
        double dy = ((double)imgY)/objectY;
        return Math.min(dx, dy);
    }

    @Override
    public void stopWork() {
        logger.info("Stop Work");
        //todo
    }

    public void pause() {
        //todo
    }

    @Override
    public void changeModeWork(boolean isAuto) {
        //todo
    }

    @Override
    public void step() {
        //todo
    }


    @FunctionalInterface
    public interface EndWorkListener {
        void end(String message);
    }
}