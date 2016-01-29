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
package io.github.jdocker.common;

import java.util.Objects;

/**
 * Created by atsticks on 25.01.16.
 */
public final class CommandDescriptor {
    String commandName;
    String parameters;
    String result;
    String description;

    public static CommandDescriptor of(String commandName,
                                       String description) {
        return of(commandName, "-", "-", description);
    }

    public static CommandDescriptor of(String commandName,
                                       String parameters,
                                       String description) {
        return of(commandName, parameters, "-", description);
    }

    public static CommandDescriptor of(String commandName,
                                       String parameters,
                                       String result,
                                       String description) {
        CommandDescriptor desc = new CommandDescriptor();
        desc.commandName = Objects.requireNonNull(commandName);
        desc.parameters = Objects.requireNonNull(parameters);
        desc.result = Objects.requireNonNull(result);;
        desc.description = description;
        return desc;
    }

    public String getName() {
        return commandName;
    }

    public String getParameters() {
        return parameters;
    }

    public String getResult() {
        return result;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommandDescriptor)) return false;

        CommandDescriptor that = (CommandDescriptor) o;

        return commandName.equals(that.commandName);

    }

    @Override
    public int hashCode() {
        return commandName.hashCode();
    }

    @Override
    public String toString() {
        return "CommandDescriptor{" +
                "commandName='" + commandName + '\'' +
                " " + parameters +
                ", -> " + result +
                '}';
    }
}
