package io.github.jdocker.deployment;

import java.util.Collection;
import java.util.Map;

/**
 * Created by atsticks on 13.01.16.
 */
public interface DeploymentEvaluator {

    Collection<Region> evaluateTargetRegions(Region rootRegion, Map<String,String> attributes);

}
