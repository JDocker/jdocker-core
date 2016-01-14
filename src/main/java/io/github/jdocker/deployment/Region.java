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
package io.github.jdocker.deployment;

import java.util.*;

/**
 * A region that can contain containers for deployment.
 */
public class Region {

    private String name;
    private boolean deploymentTarget;
    private Map<String,String> properties = new HashMap<>();
    private boolean inheritsParentProperties = true;
    private Region parent;
    private Map<String,Region> children = new TreeMap<>();

    Region(String name, Region parent){
        this.name = Objects.requireNonNull(name);
        this.parent = parent;
        if(parent!=null){
            parent.children.put(this.name, this);
        }
    }

    public Region getParent(){
        return parent;
    }

    public String getName(){
        return name;
    }

    public String getAbsoluteName(){
        if(parent!=null){
            return parent.getAbsoluteName()+"."+name;
        }
        return name;
    }

    public boolean inheritsParentProperties(){
        return inheritsParentProperties;
    }

    public boolean isDeploymentTarget(){
        return deploymentTarget;
    }

    public Map<String,String> getProperties(){
        Map<String,String> result = new HashMap<>();
        if(inheritsParentProperties && parent!=null){
            result.putAll(parent.getProperties());
        }
        result.putAll(this.properties);
        return result;
    }

    /**
     * Returns the child region.
     * @param name the name or path of the direct (name) or indirect (path) child.
     * @return the region found, or null.
     */
    public Region getSubRegion(String name){
        return getSubRegion(name, false);
    }

    /**
     * Returns the child region.
     * @param name the name or path of the direct (name) or indirect (path) child.
     * @return the region found, or null.
     */
    public Region getSubRegion(String name, boolean createMissing){
        int index = name.indexOf('.');
        if(index>0){
            String subRegionName = name.substring(0, index);
            Region region = children.get(subRegionName);
            if(region!=null){
                return region.getSubRegion(name.substring(index+1));
            }
            if(createMissing){
                region = new Region(subRegionName, this);
                return region.getSubRegion(name.substring(index+1), true);
            }
            return null;
        }
        Region region = children.get(name);
        if(region == null && createMissing){
            region = new Region(name, this);
        }
        return region;
    }

}
