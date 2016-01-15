package io.github.jdocker.deployment;

import io.github.jdocker.machine.Machine;
import io.github.jdocker.machine.Machines;
import io.github.jdocker.spi.ServiceContextManager;

import java.util.*;

/**
 * Created by atsticks on 13.01.16.
 */
public final class Deployer {

    private static final Map<String, Region> REGIONS = new HashMap<>();
    private static final Region ROOT_REGION = new Region("", null);

    public static Region getRegion(String name) {
        Region region = REGIONS.get(name);
        if (region == null) {
            region = ROOT_REGION.getSubRegion(name);
        }
        return region;
    }

    public static Region getOrCreateRegion(String name) {
        Region region = REGIONS.get(name);
        if (region == null) {
            // Extends region tree as needed, add new region(s)
            region = ROOT_REGION.getSubRegion(name, true);
            int index = name.indexOf('.');
            int lastIndex = 0;
            region = ROOT_REGION;
            while (index > 0) {
                String subRegionName = name.substring(lastIndex, index);
                region = region.getSubRegion(subRegionName);
                // update region cache
                REGIONS.put(region.getAbsoluteName(), region);
                lastIndex = index + 1;
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

    public static Region getRegions() {
        return ROOT_REGION;
    }

    public static Collection<Region> getEligibleRegions(DeploymentRequest request) {
        return ServiceContextManager.getServiceContext().getService(DockerNodeElector.class).evaluateTargetRegions(
                ROOT_REGION, request
        );
    }

    public List<Machine> deploy(DeploymentRequest request) {
        Collection<Region> regions = getEligibleRegions(request);
        regions = ServiceContextManager.getServiceContext().getService(DockerNodeSelector.class).selectTargetRegions(
                regions, request
        );
        List<Machine> machines = new ArrayList<>();
        for (Region region : regions) {
            machines.addAll(deployDirect(region, request));
        }
        return machines;
    }

    public List<Machine>  deployDirect(Region region, DeploymentRequest request) {
        // 1 get machines
        // 2 check for clustered swarm
        // 3 if swarm -> use swarm master
        // 4 no swarm -> use MachineSelectionPolicy
        List<Machine> machines = Machines.getMachines(region);
        machines = ServiceContextManager.getServiceContext().getService(MachineSelectionPolicy.class).selectMachines(
                machines, request
        );
        for (Machine machine : machines) {
            deployDirect(machine, request);
        }
        return machines;
    }

    public void deployDirect(Machine machine, DeploymentRequest request) {
        // TODO
    }

}