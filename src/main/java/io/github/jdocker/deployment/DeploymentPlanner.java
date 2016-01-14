package io.github.jdocker.deployment;

import java.util.*;

/**
 * Created by atsticks on 13.01.16.
 */
public final class DeploymentPlanner {

    private static final Map<String,Region> REGIONS = new HashMap<>();
    private static final Region ROOT_REGION = new Region("", null);

    public static Region getRegion(String name){
        Region region = REGIONS.get(name);
        if(region == null){
            region = ROOT_REGION.getSubRegion(name);
        }
        return region;
    }

    public static Region getOrCreateRegion(String name){
        Region region = REGIONS.get(name);
        if(region == null){
            // Extends region tree as needed, add new region(s)
            region = ROOT_REGION.getSubRegion(name, true);
            int index = name.indexOf('.');
            int lastIndex = 0;
            region = ROOT_REGION;
            while(index>0){
                String subRegionName = name.substring(lastIndex, index);
                region = region.getSubRegion(subRegionName);
                // update region cache
                REGIONS.put(region.getAbsoluteName(), region);
                lastIndex = index+1;
                index = name.indexOf('.', lastIndex);
            }
            String trailingRegionName = name.substring(lastIndex);
            // Access last region in path, this is the target region
            region = region.getSubRegion(trailingRegionName);
            // update region cache
            REGIONS.put(region.getAbsoluteName(), region);
            return region;
        }
        return region;
    }

    public static Region getRegions(){
        return ROOT_REGION;
    }

    public static Collection<Region> getDeploymentRegions(Map<String,String> properties, DeploymentRequest request){
        return Collections.emptyList();
    }

}
