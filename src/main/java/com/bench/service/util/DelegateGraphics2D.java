package com.bench.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Point2D;


public class DelegateGraphics2D {
    private static final Logger logger = LoggerFactory.getLogger(DelegateGraphics2D.class);

    private Graphics2D g;

    public DelegateGraphics2D(Graphics2D g) {
        this.g = g;
    }


    public void fillRectYX(double x, double y, double width, double height) {
        Point2D areaPosition =  GraphicContext.getAreaPositionYX();
        double scale = GraphicContext.getScaleYX();
        fillRect(scale, areaPosition, x, y, width, height);
    }

    private void fillRect(double scale, Point2D areaPosition, double areaX, double areaY, double width, double height) {
        int x = (int) Math.round((areaPosition.getX() + (areaX * scale)));
        int y = (int) Math.round((areaPosition.getY() + (areaY* scale)));
        g.fillRect(x, y, (int) Math.ceil((width * scale)), (int) Math.ceil((height * scale)));
    }

    public void fillRectXZ(double x, double y, double width, double height) {
        Point2D areaPosition =  GraphicContext.getAreaPositionXZ();
        double scale = GraphicContext.getScaleXZ();
        fillRect(scale, areaPosition, x, y, width, height);
    }

    public void setColor(Color orange) {
        g.setColor(orange);
    }
}