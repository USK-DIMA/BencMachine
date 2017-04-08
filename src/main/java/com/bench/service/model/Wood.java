package com.bench.service.model;

import com.bench.service.BenchManager;
import com.bench.service.util.DelegateGraphics2D;
import javafx.geometry.Point3D;

import java.awt.*;
import java.util.LinkedList;


public class Wood extends AbstractBenchObject {

    private Point3D woodSize;

    private LinkedList<Rectangle> deletedPlanXZ = new LinkedList<>();

    private LinkedList<Rectangle> deletedPlanYX = new LinkedList<>();
    private Rectangle currentDeletet;

    private Color color = Color.ORANGE;

    public Wood(Point3D weedSize) {
        this.woodSize = weedSize;
    }


    @Override
    public void update(BenchManager benchObjects) {
    }

    @Override
    public void drawYX(DelegateGraphics2D gXY) {
        gXY.setColor(color);
        gXY.fillRectYX(0, 0, (int) woodSize.getY(), (int) woodSize.getX());
        deletedPlanYX.forEach(r -> drawRectangleYX(gXY, r));
    }

    private void drawRectangleYX(DelegateGraphics2D gXY, Rectangle r) {
        gXY.setColor(r.getColor());
        gXY.fillRectYX(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public void drawXZ(DelegateGraphics2D gXZ) {
        gXZ.setColor(color);
        gXZ.fillRectXZ(0, 0, (int) woodSize.getX(), (int) woodSize.getZ());
        gXZ.setColor(Color.white);
        deletedPlanXZ.forEach(r -> gXZ.fillRectXZ(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight()));
    }


    void addClearPlanXZ(double x, double z, int width) {
        deletedPlanXZ.add(new Rectangle(new Point((int) x, 0), width, (int) z));
    }

    void changeWidthClearRowYX(double rowY) {
        if (currentDeletet != null) {
            currentDeletet.setWidth((int) Math.round(rowY));
        }
    }


    void addNewClearRowYX(boolean add, double targetPointX, int knifeWith, int zIndex) {
        if (add) {
            int rectangleHeight;
            int rectanglePositionY = (int) Math.round(targetPointX);
            int widthShift = 0;
            if (rectanglePositionY < 0) {
                widthShift = rectanglePositionY;
                rectanglePositionY = 0;
            }
            if (targetPointX + knifeWith > woodSize.getX()) {
                rectangleHeight = (int) (knifeWith - (targetPointX + knifeWith - woodSize.getX()));
            } else {
                rectangleHeight = knifeWith;
            }
            currentDeletet = new Rectangle(new Point(0, rectanglePositionY), 0, rectangleHeight + widthShift);
            currentDeletet.generateColorByZindex(zIndex, (int) woodSize.getZ());
            deletedPlanYX.add(currentDeletet);
        } else {
            currentDeletet = null;
        }
    }

    public static class Rectangle {
        private Point position;
        private int width;
        private int height;
        private Color color;

        Rectangle(Point position, int width, int height) {
            this.position = position;
            this.width = width;
            this.height = height;
        }

        Point getPosition() {
            return position;
        }

        int getWidth() {
            return width;
        }

        int getHeight() {
            return height;
        }

        void setWidth(int width) {
            this.width = width;
        }

        Color getColor() {
            return color;
        }

        void generateColorByZindex(int zIndex, int woodZ) {
            if (woodZ >= zIndex) {
                color = Color.WHITE;
            }
            if (zIndex < 0) {
                zIndex = 0;
            }
            color = new Color(255 * (woodZ - zIndex) / woodZ, 200 * (woodZ - zIndex) / woodZ, 0);
        }
    }

}