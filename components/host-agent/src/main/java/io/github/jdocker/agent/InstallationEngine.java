package io.github.jdocker.agent;

import io.github.jdocker.common.Executor;
import io.vertx.core.AbstractVerticle;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by atsticks on 25.01.16.
 */
public class InstallationEngine extends AbstractVerticle{

    private static final Logger LOG = Logger.getLogger(InstallationEngine.class.getName());

    @Override
    public void start() throws Exception {
        vertx.eventBus().consumer("jdocker.task", r -> {
            LOG.info("Executing " + Arrays.toString((String[])r.body()));
            Executor.execute((String[])r.body());
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
