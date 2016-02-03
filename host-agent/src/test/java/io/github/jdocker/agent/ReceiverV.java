package io.github.jdocker.agent;

import io.github.jdocker.common.vertx.JavaSerializingCodec;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.eventbus.DeliveryOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by atsticks on 30.01.16.
 */
public class ReceiverV extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        vertx.eventBus().registerCodec(new JavaSerializingCodec());
        vertx.eventBus().consumer("test")
                .handler(h -> {
                    System.out.println("Received:\n  headers=" + h.headers() + "\n  body="+h.body());
                    Map map = new HashMap<>();
                    map.put("reply","Thanks!");
                    vertx.eventBus().publish("test-result", map, new DeliveryOptions().setCodecName("JavaSerializer"));
                });
        System.out.println("Receiver started.");
    }

    public static void main(String... args){
        Launcher.main(new String[]{"run", Main.class.getName()});
    }

    public final static class Main extends AbstractVerticle{

        @Override
        public void start() throws Exception {
            vertx.deployVerticle(ReceiverV.class.getName(), new DeploymentOptions().setInstances(5));
            vertx.setPeriodic(2000L, h -> {
                vertx.eventBus().publish("test", System.currentTimeMillis());
            });
            vertx.eventBus().consumer("test-result")
                    .handler(h -> {
                        System.out.println("Received:\n  headers=" + h.headers() + "\n  body="+h.body());
                        h.reply("Thanks!");
                    });
        }
    }

}
