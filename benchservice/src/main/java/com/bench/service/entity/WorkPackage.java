package com.bench.service.entity;

import javafx.geometry.Point3D;

/**
 * Содержит инофрмацию о работе станка.
 *
 */
public class WorkPackage {

    /**
     * Размеры бруска
     */
    private Point3D woodSize;

    /**
     * Сдвиг, размер и глубина паза
     */
    private Point3D chamferInfo;

    /**
     * Пауза во врмея работы установки
     */
    private int timePause;

    private boolean auto;

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public Point3D getWoodSize() {
        return woodSize;
    }

    public void setWoodSize(Point3D woodSize) {
        this.woodSize = woodSize;
    }

    public Point3D getChamferInfo() {
        return chamferInfo;
    }

    public void setChamferInfo(Point3D chamferInfo) {
        this.chamferInfo = chamferInfo;
    }

    public int getTimePause() {
        return timePause;
    }

    public void setTimePause(int timePause) {
        this.timePause = timePause;
    }

    @Override
    public String toString() {
        return woodSize.toString() + "\n" + chamferInfo.toString() + "\n" + timePause;
    }
}