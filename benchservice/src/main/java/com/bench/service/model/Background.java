package com.bench.service.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Dmitry on 08.04.2017.
 */
public class Background extends AbstractBenchObject {
    private static final Logger logger = LoggerFactory.getLogger(Background.class);

    private Color color = Color.white;
    private Color borderColor = Color.black;

    public Background(Point2D offsetImages) {
        super(offsetImages);
    }


    public void update() {
    }

    @Override
    public void drawXY(Graphics2D gXY) {
        drawBackground(gXY);
    }

    @Override
    public void drawXZ(Graphics2D gXZ) {
        drawBackground(gXZ);
    }

    @Override
    public void afterDrawXY(Graphics2D g) {
        drawBorder(g);
        drawAxis(g,"Y", "X");
    }

    @Override
    public void afterDrawXZ(Graphics2D g) {
        drawBorder(g);
        drawAxis(g, "X", "Z");
    }

    private void drawBackground(Graphics2D g) {
        g.setColor(color);
        g.fillRect(0, 0, imgWidth, imgHeight);
    }

    private void drawAxis(Graphics2D g, String axisToRight, String axisToBottom) {
        int padding = 12;
        int length = 20;
        g.drawLine(padding, padding, length+padding, padding);
        g.drawLine(padding, padding, padding, length+padding);

        g.drawString(axisToRight, length+padding+2, padding+2);
        g.drawString(axisToBottom, padding+2, length+padding+2);
    }

    private void drawBorder(Graphics2D g) {
        g.setColor(Color.black);
        g.drawRect(0, 0, imgWidth -1, imgHeight -1);
    }

}