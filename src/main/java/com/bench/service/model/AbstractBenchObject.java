package com.bench.service.model;

import com.bench.service.BenchManager;
import com.bench.service.util.DelegateGraphics2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * Created by Dmitry on 08.04.2017.
 */
abstract public class AbstractBenchObject {

    private static final Logger logger = LoggerFactory.getLogger(AbstractBenchObject.class);
    private boolean auto;
    private boolean step;

    abstract public void update(BenchManager benchObjects);

    final public void drawYX(Graphics2D gXY) {
        drawYX(new DelegateGraphics2D(gXY));
    }

    final public void drawXZ(Graphics2D gXZ) {
        drawXZ(new DelegateGraphics2D(gXZ));
    }

    abstract public void drawYX(DelegateGraphics2D gXY);

    abstract public void drawXZ(DelegateGraphics2D gXZ);

}