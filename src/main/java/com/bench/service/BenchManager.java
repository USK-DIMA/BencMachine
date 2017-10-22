package com.bench.service;

import com.bench.service.entity.WorkPackage;
import com.bench.service.interfaces.IBench;
import com.bench.service.model.AbstractBenchObject;
import com.bench.service.model.Knife;
import com.bench.service.util.GraphicContext;
import com.bench.service.model.Wood;
import javafx.geometry.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Класс, отвечающий за отрисовкы, обновление состояний обхектов, их инициализщацию и прочие связи.
 * Основной класс для отрисовки.
 */
public class BenchManager extends Thread implements IBench {

    public HashMap<BenchObjectKey, AbstractBenchObject> getObjects() {
        return benchObjects;
    }

    public enum BenchObjectKey {
        KNIFE, WOOD
    }

    private static final Logger logger = LoggerFactory.getLogger(BenchManager.class);

    private final int width;
    private final int height;

    private static final int FPS = 33;
    private BufferedImage imageXY;
    private BufferedImage imageXZ;

    /**
     * графика для рисования на первом изображении (в координатах XY)
     */
    private Graphics2D gXY;

    /**
     * графика для рисования на втором изображении (в координатах XZ)
     */
    private Graphics2D gXZ;

    /**
     * положение первтого изображения на JFrame
     */
    private Point2D positionImageXY;

    /**
     * положение второго изображения на JFrame
     */
    private Point2D positionImageXZ;

    /**
     * размеры изображений, на которых происходит отрисовка
     */
    private Point2D imagesOffset;

    private JPanel viewPanel;

    /**
     * объекты для отрисвки (Нож и Брусок).
     */
    private HashMap<BenchObjectKey, AbstractBenchObject> benchObjects;

    /**
     * Объект, вызывающиеся при окончании отрисовки
     */
    private EndWorkListener endWorkListener;

    public EndWorkListener getEndWorkListener() {
        return endWorkListener;
    }

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

        imagesOffset = new Point(imageWidth, imageHeight);
        GraphicContext.setImageOffset(imagesOffset);
    }

    /**
     * иницаилизация игровых объектов.
     * В процессе развития метод опустел)
     */
    private void initBenchObjects() {
        logger.info("Init BenchObjects");
        benchObjects = new LinkedHashMap<>();
    }

    @Override
    public void setEndWorkListener(EndWorkListener endWorkListener) {
        this.endWorkListener = endWorkListener;
    }


    /**
     * Запуск процесса обновления состояний объектов и их отрисовки
     */
    @Override
    public void run() {
        imageXY = new BufferedImage((int) imagesOffset.getX(), (int) imagesOffset.getY(), BufferedImage.TYPE_INT_RGB);
        imageXZ = new BufferedImage((int) imagesOffset.getX(), (int) imagesOffset.getY(), BufferedImage.TYPE_INT_RGB);
        gXY = imageXY.createGraphics();
        gXZ = imageXZ.createGraphics();
        while (isRun(FPS)) {
            benchUpdate();
            benchRender();
        }
    }

    private void benchUpdate() {
        update(benchObjects.get(BenchObjectKey.WOOD));
        update(benchObjects.get(BenchObjectKey.KNIFE));
    }

    private void update(AbstractBenchObject benchObject) {
        if(benchObject == null) {
            return;
        }
        benchObject.update(this);
    }

    /**
     * Отрисовка объектов
     */
    private void benchRender() {
        drawBackground(gXY, gXZ);
        drawObject(benchObjects.get(BenchObjectKey.WOOD));
        drawObject(benchObjects.get(BenchObjectKey.KNIFE));
        drawBorder(gXY, gXZ);
        drawAxis(gXY, "Y", "X");
        drawAxis(gXZ, "X", "Z");
        drawOnFrame();
    }

    private void drawObject(AbstractBenchObject abstractBenchObject) {
        if(abstractBenchObject == null) {
            return;
        }
        abstractBenchObject.drawYX(gXY);
        abstractBenchObject.drawXZ(gXZ);
    }

    private void drawBackground(Graphics2D... gArr) {
        for (Graphics2D g : gArr) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, GraphicContext.getImgWidth(), GraphicContext.getImgHeight());
        }
    }

    private void drawBorder(Graphics2D... gArr) {
        for (Graphics2D g : gArr) {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, GraphicContext.getImgWidth() - 1, GraphicContext.getImgHeight() - 1);
        }
    }

    private void drawAxis(Graphics2D g, String axisToRight, String axisToBottom) {
        int padding = 12;
        int length = 20;
        g.drawLine(padding, padding, length+padding, padding);
        g.drawLine(padding, padding, padding, length+padding);

        g.drawString(axisToRight, length+padding+2, padding+2);
        g.drawString(axisToBottom, padding+2, length+padding+2);
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
        Point3D woodSize = workPackage.getWoodSize();
        updateGraphicContext(woodSize);
        Wood wood = new Wood(woodSize);
        Knife knife = new Knife(workPackage.getChamferInfo(), woodSize.getY(), workPackage.getTimePause(), workPackage.isAuto());
        addBenchObject(BenchObjectKey.WOOD, wood);
        addBenchObject(BenchObjectKey.KNIFE, knife);
    }

    /**
     * Смотрит на размеры бруска и выставляет новые коэфициенты масштабирования
     * для изображений
     */
    private void updateGraphicContext(Point3D weedSize) {
        double scaleYX = calculateScale(imagesOffset, (int) Math.round(weedSize.getY()), (int) Math.round(weedSize.getX()), 5);
        double scaleXZ = calculateScale(imagesOffset, (int) Math.round(weedSize.getX()), (int) Math.round(weedSize.getZ()), 5);
        GraphicContext.setScaleXZ(scaleXZ);
        GraphicContext.setScaleYX(scaleYX);
        GraphicContext.updateAreaPosition(weedSize);
    }

    private void addBenchObject(BenchObjectKey key, AbstractBenchObject benchObject) {
        benchObjects.put(key, benchObject);
    }

    /**
     * Расчитывает коэффициент масштабирования для объекта, чтобы он целиок уместился
     * на изображении
     *
     * @param imageOffset доступные размеры изображения
     * @param objectX     размер изорбражения по ширине
     * @param objectY     размер изображения по высоте
     * @param padding     какой отступ должен быть от краёв в процентах
     * @return
     */
    private double calculateScale(Point2D imageOffset, int objectX, int objectY, int padding) {
        if (padding < 0 || padding > 99) {
            padding = 0;
        }
        int imgX = (int) Math.round(imageOffset.getX()) * (100 - 2 * padding) / 100;
        int imgY = (int) Math.round(imageOffset.getY()) * (100 - 2 * padding) / 100;
        double dx = ((double) imgX) / objectX;
        double dy = ((double) imgY) / objectY;
        return Math.min(dx, dy);
    }

    @Override
    public void stopWork() {
        logger.info("Stop Work");
        ((Knife)benchObjects.get(BenchObjectKey.KNIFE)).stop();
    }


    @Override
    public void changeModeWork(boolean isAuto) {
        ((Knife) benchObjects.get(BenchObjectKey.KNIFE)).setAuto(isAuto);
    }

    @Override
    public void step() {
        ((Knife) benchObjects.get(BenchObjectKey.KNIFE)).setStep(true);
    }

}