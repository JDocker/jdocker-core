package io.github.jdocker.events;

/**
 * For these commands logic is required and according listeners/actors must be defined.
 */
public enum CommandType {
    /* IPAM */
    DEFINE_POOL,
    RELEASE_POOL,

    /*Deploy /undeploy */
    DEPLOY_CONTAINER,
    DEPLOY_MACHINE,
    DEPLOY_NETWORK,
    DEPLOY_SWARM,

    UNDEPLOY_SWARM,
    UNDEPLOY_CONTAINER,
    UNDEPLOY_MACHINE,
    UNDEPLOY_NETWORK,

    /* SSH. */
    EXECUTE_COMMAND,

    /*  DNS handling. */
    REGISTER_SERVICE,
    UNREGISTER_SERVICE,
    REGISTER_HOST,
    UNREGISTER_HOST,
    REGISTER_CONTAINER,
    UNREGISTER_CONTAINER,

    /* Security. */
    DEFINE_USER,
    DEFINE_SECURITYPOLICY,

    /* Scaling. */
    SCALE_UP,
    SCALE_DOWN,

    /* Failover. */
    RELOCATE,
    MASTER_SYNCH,
    CONFIG_UPDATE,

    /* Monitoring. */
    FAILURE,
    LATENCY,
    AUTODISCOVERY,
    OVERLOAD,
    UNDERLOAD,
}
