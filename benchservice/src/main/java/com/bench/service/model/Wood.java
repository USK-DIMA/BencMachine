package com.bench.service.model;

import javafx.geometry.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Dmitry on 08.04.2017.
 */
public class Wood extends AbstractBenchObject {

    private static final Logger logger = LoggerFactory.getLogger(Wood.class);

    private Point3D weedSize;

    private PointXZ woodOffsertXZ;
    private PointYX woodOffsertYX;

    private double scaleYX;
    private double scaleXZ;



    private Color color = Color.ORANGE;

    public Wood(Point2D offsetImages) {
        super(offsetImages);
    }

    public Wood(Point2D offsetImages, Point3D weedSize, double scaleXY, double scaleXZ) {
        super(offsetImages);
        this.weedSize = weedSize;
        this.scaleYX = scaleXY;
        this.scaleXZ= scaleXZ;
        woodOffsertXZ = new PointXZ((int)(weedSize.getX()*scaleXZ), (int)(weedSize.getZ()*scaleXZ));
        woodOffsertYX = new PointYX((int)(weedSize.getY()*scaleYX), (int)(weedSize.getX()*scaleYX));
    }


    @Override
    public void update() {

    }

    @Override
    public void drawXY(Graphics2D gXY) {
        drawCleanWood(gXY, woodOffsertYX.getY(), woodOffsertYX.getX());
    }

    @Override
    public void drawXZ(Graphics2D gXZ) {
        drawCleanWood(gXZ, woodOffsertXZ.getX(), woodOffsertXZ.getZ());
    }

    private void drawCleanWood(Graphics2D g, int woodX, int woodY) {
        int x = imgWidth/2 - woodX/2;
        int y = imgHeight/2 - woodY/2;
        g.setColor(color);
        g.fillRect(x, y, woodX, woodY);
    }


    private class PointYX {
        private int Y;
        private int X;

        public PointYX(int y, int x) {
            Y = y;
            X = x;
        }

        public int getY() {
            return Y;
        }

        public void setY(int y) {
            Y = y;
        }

        public int getX() {
            return X;
        }

        public void setX(int x) {
            X = x;
        }
    }

    private class PointXZ {
        private int X;
        private int Z;

        public PointXZ(int x, int z) {
            X = x;
            Z = z;
        }

        public int getX() {
            return X;
        }

        public void setX(int x) {
            X = x;
        }

        public int getZ() {
            return Z;
        }

        public void setZ(int z) {
            Z = z;
        }
    }

}