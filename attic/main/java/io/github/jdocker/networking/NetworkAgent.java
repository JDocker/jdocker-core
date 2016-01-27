package io.github.jdocker.networking;

import io.vertx.core.AbstractVerticle;

/**
 * Main Docker network process, which is able to perform network setup and policy changes.
 */
public class NetworkAgent extends AbstractVerticle{

    public void start() {
        // register node into known nodes
    }

    public void stop() {
        // remove nodes from known nodes.
    }

}
