package io.github.jdocker.deployment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by atsticks on 14.01.16.
 */
public class Deployment {

    private List<Region> regions = new ArrayList<>();
    private DeploymentRequest deloymentRequest;

    /**
     * Instantiates a new Container request.
     */
    public Deployment(DeploymentRequest deloymentRequest, List<Region> regions){
        this.regions.addAll(Objects.requireNonNull(regions));
        this.deloymentRequest = Objects.requireNonNull(deloymentRequest);
    }

    public List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    public DeploymentRequest getDeloymentRequest() {
        return deloymentRequest;
    }

    // TODO add ToString, equals hashCode
}
