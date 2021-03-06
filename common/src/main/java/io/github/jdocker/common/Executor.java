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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by atsticks on 12.01.16.
 */
public final class Executor {

    private Executor(){}

    public static String execute(String... commands) {
        StringBuffer output = new StringBuffer();
        for(String command:commands) {
            Process p;
            try {
                p = Runtime.getRuntime().exec(command);
                p.waitFor();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(p.getInputStream()));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }

                reader =
                        new BufferedReader(new InputStreamReader(p.getErrorStream()));

                line = "";
                while ((line = reader.readLine()) != null) {
                    if(line.isEmpty()){
                        output.append("ERROR: ");
                    }
                    output.append(line + "\n");
                }

            } catch (Exception e) {
                output.append("ERROR: " + command + " - " + e.getMessage());
                e.printStackTrace();
            }
        }
        return output.toString().trim();

    }

    public static InputStream executeToInputStream(String command) throws IOException, InterruptedException {
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        return p.getInputStream();
    }

    public static String executeRemote(String machine, String... commands) {
        // open ssh session on machine...
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
