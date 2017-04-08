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

    private Color color = Color.ORANGE;

    public Wood(Point3D weedSize) {
        this.weedSize = weedSize;
    }



    @Override
    public void update(HashMap<BenchManager.BenchObjectKey, AbstractBenchObject> benchObjects) {
    }

    @Override
    public void drawYX(DelegateGraphics2D gXY) {
        gXY.setColor(Color.ORANGE);
        gXY.fillRectYX(0, 0, (int)weedSize.getY(), (int)weedSize.getX());
    }

    @Override
    public void drawXZ(DelegateGraphics2D gXZ) {
        gXZ.setColor(Color.ORANGE);
        gXZ.fillRectXZ(0, 0, (int)weedSize.getX(), (int)weedSize.getZ());
        gXZ.setColor(Color.white);
        deletedPlanXZ.forEach(r->gXZ.fillRectXZ(r.getPosition().getX(), r.getPosition().getY(), r.getWidth(), r.getHeight()));
    }



    public void addClearPlan(double x, double z, int width) {
        deletedPlanXZ.add(new Rectangle(new Point((int) x, 0), width, (int) z));
    }

    public static class Rectangle {
        private Point position;
        private int width;
        private int height;

        public Rectangle(Point position, int width, int height) {
            this.position = position;
            this.width = width;
            this.height = height;
        }

        public Point getPosition() {
            return position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

}