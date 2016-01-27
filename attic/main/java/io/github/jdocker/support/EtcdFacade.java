package io.github.jdocker.support;

import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import io.github.jdocker.JDockerHost;
import io.github.jdocker.common.Executor;

import java.util.logging.Logger;

/**
 * Created by atsticks on 19.01.16.
 */
public final class EtcdFacade {

    private static final Logger LOG = Logger.getLogger(EtcdFacade.class.getName());
    private EtcdFacade(){}

    public static void installEtcd(String target){
        Executor.execute(
                "cd " + target,
                "curl -L  https://github.com/coreos/etcd/releases/download/v2.2.4/etcd-v2.2.4-linux-amd64.tar.gz -o " +
                        "etcd-v2.2.4-linux-amd64.tar.gz",
                "tar xzvf etcd-v2.2.4-linux-amd64.tar.gz",
                "mv etcd-v2.2.4-linux-amd64 etcd");
    }

    public static String runEtc(JDockerHost docker){
        ContainerConfig container = ContainerConfig.builder()
                .image("quay.io/coreos/etcd:v2.2.4")
                .hostname("etcd").build();
        try {
            ContainerCreation cr = docker.createDockerClient().createContainer(container);
            for(String w:cr.getWarnings()){
                LOG.warning(w);
            }
            return cr.id();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
