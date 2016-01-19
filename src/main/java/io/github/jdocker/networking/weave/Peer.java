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
package io.github.jdocker.networking.weave;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by atsticks on 17.01.16.
 */
public class Peer{

    private String name;
    private String nickName;
    private long id;
    private long shortId;
    private int version;
    private Set<String> connections = new HashSet<>();


    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public long getId() {
        return id;
    }

    public long getShortId() {
        return shortId;
    }

    public int getVersion() {
        return version;
    }

    public Set<String> getConnections() {
        return connections;
    }
}
