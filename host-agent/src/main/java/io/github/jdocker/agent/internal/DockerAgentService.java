package io.github.jdocker.agent.internal;//package io.github.jdocker.agent;
//
//import io.github.jdocker.MachineConfig;
//import io.github.jdocker.common.OwnedResult;
//import io.vertx.core.AsyncResult;
//import io.vertx.core.Handler;
//import io.vertx.core.Vertx;
//
//import java.util.*;
//
///**
// * Created by atsticks on 31.01.16.
// */
////@ProxyGen
//public interface DockerAgentService {
//
//    // A couple of factory methods to create an instance and a proxy
//    static DockerAgentService create(Vertx vertx) {
//        return new DockerAgentServiceImpl(vertx);
//    }
//
////    static SomeDatabaseService createProxy(Vertx vertx,
////                                           String address) {
////        return new SomeDatabaseServiceVertxEBProxy(vertx, address);
////    }
//
////    // Actual service operations here...
////    void save(String collection, JsonObject document,
////              Handler<AsyncResult<Void>> resultHandler);
//
//    String getAgentName();
//
//    void getLabels(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void setLabels(String agentName, Map<String, String> labels, Handler<AsyncResult<OwnedResult>> handler);
//
//    void getKernelVersion(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void locateCommand(String agentName, String commandName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void isDockerInstalled(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
////    void installDocker(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void isDockerMachineInstalled(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
////    void installDockerMachine(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void isDockerComposeInstalled(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
////    void installDockerCompose(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
////    void isSDNInstalled(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
////    void installSDN(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void getDockerVersion(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    void getDockerInfo(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Access a machine status.
//     *
//     * @param agentName        the host expression (wildcards possible).
//     * @param machineName the machine name , not null. Must match a machine registered on the given host.
//     * @param handler     the result handler
//     * @param handler     the result handler
//     * @return a JSON object mapping host, machine to a status, not null
//     */
//    void machineStatus(String agentName, String machineName, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Access a machine ip address.
//     *
//     * @param agentName        the host expression (wildcards possible).
//     * @param machineName the machine name , not null. Must match a machine registered on the given host.
//     * @param handler     the result handler
//     * @return a JSON object mapping host, machine to an IP, not null
//     */
//    void machineIP(String agentName, String machineName, Handler<AsyncResult<OwnedResult>> handler);
//
//
//    /**
//     * Upgrades the installed docker version on the machine.
//     *
//     * @param agentName    the host expression (wildcards possible).
//     * @param name    the machine name , not null.
//     * @param handler the result handler
//     * @return the upgrade result for the machine.
//     */
//    void machineUpgrade(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Starts a machine.
//     *
//     * @param agentName the host expression (wildcards possible).
//     * @param name the machine name, not null.
//     * @return the status, not null
//     */
//    void machineStart(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Restarts a machine.
//     *
//     * @param agentName    the host expression (wildcards possible).
//     * @param name    the machine name, not null.
//     * @param handler the result handler
//     * @return the status, not null
//     */
//    void machineRestart(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Stops a machine.
//     *
//     * @param agentName    the host expression (wildcards possible).
//     * @param name    the machine name, not null.
//     * @param handler the result handler
//     * @return the status, not null
//     */
//    void machineStop(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Kills a machine.
//     *
//     * @param agentName    the host expression (wildcards possible).
//     * @param name    the machine name, not null.
//     * @param handler the result handler
//     * @return the status, not null
//     */
//    void machineKill(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * This calls maps to {@code docker-io.github.jdocker.machine ls} listing all known machines for a given docker root.
//     *
//     * @return a list with all io.github.jdocker.machine names, never null.
//     */
//    void machineLS(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//
//    /**
//     * Create a new machines with the given machine configuration.
//     *
//     * @param machineConfig the machine config, not null.
//     * @param handler       the result handler
//     * @return the new machine, check its status if all is OK.
//     */
//    void machineCreate(String agentName, MachineConfig machineConfig, Handler<AsyncResult<OwnedResult>> handler);
//
//
//    /**
//     * Access a machine configuration by name.
//     *
//     * @param agentName    the machine name , not null.
//     * @param handler the result handler.
//     * @return the MachineConfig instance, or null.
//     */
//    void machineInspect(String agentName, String name, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     * Access the interface configuration of a machine.
//     *
//     * @param agentName    the host expression (wildcards possible).
//     * @param handler the result handler
//     */
//    void getIFConfig(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
////    /**
////     * Lookup all known machine configurations.
////     * @param agentName the host expression (wildcards possible).
////     * @param handler the result handler.
////     */
////    void lookupMachines(String agentName, Handler<AsyncResult<OwnedResult>> handler);
//
//    /**
//     *
//     * @param agentName the host expression (wildcards possible).
//     * @param machineName the machine name.
//     * @param handler the result handler.
//     */
//    void getMachineConfig(String agentName, String machineName, Handler<AsyncResult<OwnedResult>> handler);
//
//}
