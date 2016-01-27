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

import org.apache.tamaya.ConfigException;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 * Implementation of the {@link org.apache.tamaya.format.ConfigurationFormat}
 * able to read configuration properties represented in JSON
 *
 * @see <a href="http://www.json.org">JSON format specification</a>
 */
public class JSONMapper {
    /** Property that make Johnzon accept commentc. */
    public static final String JOHNZON_SUPPORTS_COMMENTS_PROP = "org.apache.johnzon.supports-comments";
    /** The reader factory used. */
    private final JsonReaderFactory readerFactory;

    /**
     * Constructor, itniaitlizing zhe JSON reader factory.
     */
    public JSONMapper(){
        Map<String, Object> config = new HashMap<>();
        config.put(JOHNZON_SUPPORTS_COMMENTS_PROP, true);
        this.readerFactory = Json.createReaderFactory(config);
    }

    public Map<String,String> readJsonData(InputStream inputStream) {

        try {
            final JsonReader reader = this.readerFactory.createReader(inputStream, Charset.forName("UTF-8"));
            JsonObject root = reader.readObject();
            HashMap<String, String> values = new HashMap<>();
            JSONVisitor visitor = new JSONVisitor(root, values);
            visitor.run();
            return values;
        } catch (JsonException e) {
            throw new ConfigException("Failed to read data.", e);
        }
    }
}
