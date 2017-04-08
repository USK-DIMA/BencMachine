package com.bench.service.model;

import com.bench.service.BenchManager;
import com.bench.service.util.DelegateGraphics2D;
import javafx.geometry.Point3D;


import java.awt.*;
import java.util.*;
import java.util.List;


public class Knife extends AbstractBenchObject {

    /**
     * Позиция ножа -- левый кончик острия ножа
     */
    private Point3D position;

    private static final int width = 10;
    private static final double length = 2;
    private LinkedList<Point3D> targetPoints;
    private int speed;
    private boolean auto;
    private boolean step;

    private static final int depth = 1; //глубина выреза за раз. Со значениями отличными от единицы не протестено, поэтому не меняй


    public Knife(Point3D chamferInfo, double woodLength, int speed, boolean auto) {
        position = new Point3D(0, 0, -10);
        this.auto = auto;
        this.step = false;
        this.speed = speed;
        targetPoints = generateTargetPoints(chamferInfo, woodLength);
    }

    private LinkedList<Point3D> generateTargetPoints(Point3D chamferInfo, double woodLength) {
        LinkedList<Point3D> targetPoints = new LinkedList<>();
        int x = (int) chamferInfo.getX(); //начало паза
        int y = (int) chamferInfo.getY(); //ширина паза
        int z = (int) chamferInfo.getZ(); //глубина паза
        int referencePointsCount = (int) Math.ceil(y / width);
        List<Integer> referencePoints = new ArrayList<>(referencePointsCount);
        int tx = x;
        while (tx + width < x + y) {
            referencePoints.add(tx);
            tx += width;
        }
        if (tx < x + y) {
            referencePoints.add((x + y - width));
        }

        int iter = 0; //задел на глибину выреза за раз больше одного
        for (int cz = 1; cz <= z; cz += depth) { //проходим слои по одному милиметру за раз
            iter++;
            if (iter % 2 == 1) { //нечётные слои проходим слева на право
                for (int i = 0; i < referencePoints.size(); i++) {
                    addTargetPointByReferencePoint(targetPoints, referencePoints.get(i), woodLength, cz);
                }
            } else { //чётные справа на лево
                for (int i = referencePoints.size() - 1; i >= 0; i--) {
                    addTargetPointByReferencePoint(targetPoints, referencePoints.get(i), woodLength, cz);
                }
            }
        }
        return targetPoints;
    }

    private void addTargetPointByReferencePoint(List<Point3D> targetPoints, Integer p, double woodLength, int zLayer) {
        targetPoints.add(new Point3D(p, 0, zLayer - 2));
        targetPoints.add(new Point3D(p, 0, zLayer));
        targetPoints.add(new Point3D(p, woodLength, zLayer));
        targetPoints.add(new Point3D(p, woodLength, zLayer - 1));
    }

    @Override
    public void update(BenchManager benchManager) {
        if (!auto & !step) {
            return;
        }
        Wood wood = (Wood) benchManager.getObjects().get(BenchManager.BenchObjectKey.WOOD);
        Point3D tPoint;
        try {
            tPoint = targetPoints.getFirst();
        } catch (NoSuchElementException e) {
            return;
        }
        double oldCurrentPointZ = position.getZ();
        boolean alreadyHere = updateCurrentPosition(tPoint);
        wood.changeWidthClearRowYX(position.getY());
        if (alreadyHere) {
            try {
                Point3D currentPoint = targetPoints.removeFirst();
                wood.addNewClearRowYX(oldCurrentPointZ < currentPoint.getZ(), currentPoint.getX(), width, (int) currentPoint.getZ());
                if (oldCurrentPointZ > currentPoint.getZ()) {
                    step = false;
                }
                collapseWoodXZ(wood, currentPoint, targetPoints.getFirst());
            } catch (NoSuchElementException e) { //норм, да? Пока так.

            }
            if (targetPoints.size() == 0) {
                benchManager.getEndWorkListener().end("Работа завершена");
            }
        }
    }

    /**
     * Cообщает дереву, что у него отрезали кусок, т.е. дерево меняет своё состояние
     */
    private void collapseWoodXZ(Wood wood, Point3D currentPoint, Point3D first) {
        //вся логика передачи информации дереву вообще ушербная, но пусть пока остаётся такая.
        //если будет время, переделать нормально
        if (currentPoint.getZ() > first.getZ()) { //если нож поднялся, то передаём дереву координаты, от куда он поднялся
            wood.addClearPlanXZ(currentPoint.getX(), currentPoint.getZ(), width);
        }
    }

    private boolean updateCurrentPosition(Point3D tPoint) {
        double dx = tPoint.getX() - position.getX();
        double dy = tPoint.getY() - position.getY();
        double dz = tPoint.getZ() - position.getZ();
        boolean alreadyHere = true;
        if (Math.abs(dx) > speed) {
            dx = dx / Math.abs(dx) * speed;
            alreadyHere = false;
        }
        if (Math.abs(dy) > speed) {
            dy = dy / Math.abs(dy) * speed;
            alreadyHere = false;
        }
        if (Math.abs(dz) > speed) {
            dz = dz / Math.abs(dz) * speed;
            alreadyHere = false;
        }
        updatePosition(dx, dy, dz);
        return alreadyHere;
    }

    private void updatePosition(double dx, double dy, double dz) {
        double newX = position.getX() + dx;
        double newY = position.getY() + dy;
        double newZ = position.getZ() + dz;
        position = new Point3D(newX, newY, newZ);
    }


    @Override
    public void drawYX(DelegateGraphics2D gXY) {
        gXY.setColor(Color.gray);
        gXY.fillRectYX(position.getY() - length, position.getX(), length, width);
    }

    @Override
    public void drawXZ(DelegateGraphics2D gXZ) {
        gXZ.setColor(Color.gray);
        gXZ.fillRectXZ(position.getX(), -100, width, 100 + position.getZ());
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public void setStep(boolean step) {
        this.step = step;
    }

    public void stop() {
        targetPoints = new LinkedList<>();
    }
}