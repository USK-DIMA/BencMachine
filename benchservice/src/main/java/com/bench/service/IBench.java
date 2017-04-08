package com.bench.service;

import com.bench.service.entity.WorkPackage;

/**
 * Created by Dmitry on 08.04.2017.
 */
public interface IBench {

    void setEndWorkListener(BenchManager.EndWorkListener endWorkListener);

    void startWork(WorkPackage workPackage);

    void stopWork();

    void changeModeWork(boolean isAuto);

    void start();

    void step();
}
