package com.bench.service.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by Dmitry on 08.04.2017.
 */
abstract public class AbstractBenchObject {

    protected int imgWidth;
    protected int imgHeight;

    public AbstractBenchObject(Point2D offsetImages) {
        this.imgWidth = (int) offsetImages.getX();
        this.imgHeight = (int) offsetImages.getY();
    }
    private static final Logger logger = LoggerFactory.getLogger(AbstractBenchObject.class);

    abstract public void update();

    abstract public void drawXY(Graphics2D gXY);

    abstract public void drawXZ(Graphics2D gXZ);

    public void afterDrawXY(Graphics2D gXY) {

    }

    public void afterDrawXZ(Graphics2D gXZ) {

    }

}