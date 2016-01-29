/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
