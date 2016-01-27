# COMPONENTS
Directory containing the different submodules of JDocker:

* _attic_: some old code that partially should be integrated with new logic.
* _common_ Common functionality used through the system, e.g. Runtmie Executors and HealtChecks.
* _host-agent_ Agent to be installed on the host running Docker.
* _host-api_ Java API that is used by the _dockerhost-agent_
* _orchestrator_: Overall orchestration API that should be exposed via REST
  or other matching protocols.

For building you must frist install the parent module (and its buildconfiguration child module).

NOTE: Most of the code here is completely untested. Do not tell us, we haven't warned you.
