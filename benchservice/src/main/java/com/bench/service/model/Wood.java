package com.bench.service.model;

import com.bench.service.BenchManager;
import com.bench.service.util.DelegateGraphics2D;
import javafx.geometry.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Dmitry on 08.04.2017.
 */
public class Wood extends AbstractBenchObject {

    private static final Logger logger = LoggerFactory.getLogger(Wood.class);

    private Point3D weedSize;

    private LinkedList<Rectangle> deletedPlanXZ = new LinkedList<>();

    private LinkedList<Rectangle> deletedPlanYX = new LinkedList<>();
    private Rectangle currentDeletet;

    private Color color = Color.ORANGE;

    public Wood(Point3D weedSize) {
        this.weedSize = weedSize;
    }


    @Override
    public void update(HashMap<BenchManager.BenchObjectKey, AbstractBenchObject> benchObjects) {
    }

    @Override
    public void drawYX(DelegateGraphics2D gXY) {
        gXY.setColor(color);
        gXY.fillRectYX(0, 0, (int) weedSize.getY(), (int) weedSize.getX());
        deletedPlanYX.forEach(r -> drawRectangleYX(gXY, r));
    }

    private void drawRectangleYX(DelegateGraphics2D gXY, Rectangle r) {
        gXY.setColor(r.getColor());
        gXY.fillRectYX(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public void drawXZ(DelegateGraphics2D gXZ) {
        gXZ.setColor(color);
        gXZ.fillRectXZ(0, 0, (int) weedSize.getX(), (int) weedSize.getZ());
        gXZ.setColor(Color.white);
        deletedPlanXZ.forEach(r -> gXZ.fillRectXZ(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight()));
    }


    public void addClearPlanXZ(double x, double z, int width) {
        deletedPlanXZ.add(new Rectangle(new Point((int) x, 0), width, (int) z));
    }

    public void changeWidthClearRowYX(double rowY) {
        if (currentDeletet != null) {
            currentDeletet.setWidth((int) Math.round(rowY));
        }
    }


    public void addNewClearRowYX(boolean add, double targetPointX, int knifeWith, int zIndex) {
        if (add) {
            currentDeletet = new Rectangle(new Point(0, (int) Math.round(targetPointX)), 0, knifeWith);
            currentDeletet.generateColorByZindex(zIndex, (int) weedSize.getZ());
            deletedPlanYX.add(currentDeletet);
        } else {
            currentDeletet = null;
        }
    }

    public static class Rectangle {
        private Point position;
        private int width;
        private int height;
        private int zIndex;
        private Color color;

        public Rectangle(Point position, int width, int height) {
            this.position = position;
            this.width = width;
            this.height = height;
        }

        public Point getPosition() {
            return position;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getzIndex() {
            return zIndex;
        }

        public void setzIndex(int zIndex) {
            this.zIndex = zIndex;
        }

        public void setPosition(Point position) {
            this.position = position;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void generateColorByZindex(int zIndex, int woodZ) {
            if (woodZ >= zIndex) {
                color = Color.WHITE;
            }
            color = new Color(255 * (woodZ - zIndex) / woodZ, 200 * (woodZ - zIndex) / woodZ, 0);
        }
    }

}