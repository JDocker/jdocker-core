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
package io.github.jdocker.common.vertx;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Codec that uses Java serialization for remoting and simply forwards references for local messages.
 */
public class JavaSerializingCodec implements MessageCodec {
    @Override
    public void encodeToWire(Buffer buffer, Object o) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oss = new ObjectOutputStream(bos);
            oss.writeObject(o);
            oss.flush();
            buffer.appendBytes(bos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object decodeFromWire(int i, Buffer buffer) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.getBytes()));
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object transform(Object o) {
        return o;
    }

    @Override
    public String name() {
        return "JavaSerializer";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
