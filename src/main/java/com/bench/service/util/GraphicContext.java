package com.bench.service.util;

import javafx.geometry.Point3D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Пользотваеть может зажать разные параметры размера бруска.
 * Очень большие или очень маленькие.
 * Соответсвенно надо, чтобы размер бруска масштабировался на имзображениях,
 * чтобы не был очень маленьким или выходил за пределы изображения.
 * Соответвенно данный класс содержит в себе все коефициенты масштабирования
 * и прочую информацию, необходимую для
 */
public class GraphicContext {
    private static final Logger logger = LoggerFactory.getLogger(GraphicContext.class);

    private static int imgWidth;
    private static int imgHeight;
    private static double scaleYX;
    private static double scaleXZ;
    private static Point2D areaPositionYX;
    private static Point2D areaPositionXZ;

    public static void setImageOffset(Point2D imagesOffset){
        imgWidth = (int) imagesOffset.getX();
        imgHeight = (int) imagesOffset.getY();
    }

    public static int getImgWidth() {
        return imgWidth;
    }

    public static int getImgHeight() {
        return imgHeight;
    }

    public static double getScaleYX() {
        return scaleYX;
    }

    public static void setScaleYX(double scaleYX) {
        GraphicContext.scaleYX = scaleYX;
    }

    public static double getScaleXZ() {
        return scaleXZ;
    }

    public static void setScaleXZ(double scaleXZ) {
        GraphicContext.scaleXZ = scaleXZ;
    }

    public static Point2D getAreaPositionYX() {
        return areaPositionYX;
    }

    public static Point2D getAreaPositionXZ() {
        return areaPositionXZ;
    }

    public static void updateAreaPosition(Point3D woodSize) {
        int woodX = (int) woodSize.getX();
        int woodY = (int) woodSize.getY();
        int woodZ = (int) woodSize.getZ();

        areaPositionYX = new Point((int)(imgWidth/2 - woodY*scaleYX/2), (int)(imgHeight/2 - woodX*scaleYX/2));
        areaPositionXZ = new Point((int)(imgWidth/2 - woodX*scaleXZ/2), (int)(imgHeight/2 - woodZ*scaleXZ/2));
    }
}