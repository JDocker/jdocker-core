///*
// * Licensed to the Apache Software Foundation (ASF) under one
// * or more contributor license agreements.  See the NOTICE file
// * distributed with this work for additional information
// * regarding copyright ownership.  The ASF licenses this file
// * to you under the Apache License, Version 2.0 (the
// * "License"); you may not use this file except in compliance
// * with the License.  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing,
// * software distributed under the License is distributed on an
// * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// * KIND, either express or implied.  See the License for the
// * specific language governing permissions and limitations
// * under the License.
// */
//package io.github.jdocker.agent;
//
//import io.github.jdocker.DockerAgentCommands;
//import io.vertx.core.Vertx;
//import io.vertx.core.eventbus.EventBus;
//import io.vertx.core.json.JsonObject;
//import io.vertx.ext.unit.TestContext;
//import io.vertx.ext.unit.junit.VertxUnitRunner;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//
//import static org.junit.Assert.*;
//
///**
// * Created by atsticks on 28.01.16.
// */
//@RunWith(VertxUnitRunner.class)
//public class DockerAgentAPITest {
//
//    private Vertx vertx;
//    private DockerAgent verticle;
//    private EventBus eventBus;
//
//    @Before
//    public void setUp(TestContext context) {
//        vertx = Vertx.vertx();
//        verticle = new DefaultDockerAgent();
//        vertx.deployVerticle(verticle);
//        eventBus = vertx.eventBus();
//    }
//
//    @After
//    public void tearDown(TestContext context) {
//        vertx.close(context.asyncAssertSuccess());
//    }
//
//    @org.junit.Test
//    public void testSetLabels() throws Exception {
//        eventBus.send(DockerAgentCommands.AGENT_SET_LABELS.getName(),
//                new JsonObject().put("label1", "test1").put("label2", "test2"),
//                h -> {
//                    assertTrue(h.succeeded());
//                    assertEquals(h.result().body().toString(), "");
//                });
//    }
//
//    @org.junit.Test
//    public void testGetLabels() throws Exception {
//        eventBus.send(DockerAgentCommands.AGENT_GET_LABELS.getName(), "workhorse.atsticks.ch", h -> {
//            assertTrue(h.succeeded());
//            assertEquals(h.result().body().toString(), "");
//        });
//    }
//
//    @org.junit.Test
//    public void testDockerInfo() throws Exception {
//        eventBus.send(DockerAgentCommands.DOCKER_INFO.getName(), "workhorse.atsticks.ch");
//    }
//
//    @org.junit.Test
//    public void testDockerVersion() throws Exception {
//        eventBus.send(DockerAgentCommands.DOCKER_VERSION.getName(), "workhorse.atsticks.ch", h -> {
//            assertTrue(h.succeeded());
//            System.out.println("Docker version: " + h.result().body().toString());
//            assertEquals(h.result().body().toString(), "");
//        });
//    }
//
//
//}