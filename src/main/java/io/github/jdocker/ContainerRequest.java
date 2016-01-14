package io.github.jdocker;

import io.github.jdocker.deployment.Region;
import io.github.jdocker.machine.Machine;

import java.util.*;

/**
 * Request encapsulating {@code docker create ...}.
 */
public class ContainerRequest {

    private Region targetRegion;
    private int scale;

//    -a, --attach=[]                 Attach to STDIN, STDOUT or STDERR
    // -i, --interactive=false         Keep STDIN open even if not attached
    // --log-opt=[]                    Log driver options
    // -t, --tty=false                 Allocate a pseudo-TTY

    /** Add a custom host-to-IP mapping (host:ip), maps to {@code --add-host=[]}. */
    private Set<String> customHostToIPMappings = new TreeSet<>();
    /** Block IO (relative weight), between 10 and 1000, aps to {@code --blkio-weight=0}. */
    private int blkIOWeight;
    /** The CPU shares to assigned (relative weight), maps to {@code -c, --cpu-shares=0 }. */
    private int cpuShares;
    /** Add Linux capabilities, maps to {@code --cap-add=[]}. */
    private Set<String> addCapabilities = new TreeSet<>();
    /** Removes Linux capabilities, maps to {@code --cap-drop=[]}. */
    private Set<String> removeCapabilities = new TreeSet<>();
    /** Optional parent cgroup for the container, meps to {@code --cgroup-parent= }. */
    private String parentCgroup;
    /** Write container iD to file, maps to {@code -cidfile=<FILE>}. */
    private String cidfile;
    /** Limit CPU CFS (Completely Fair Scheduler) period, maps to {@code --cpu-period=0}. */
    private int cpuPeriod;
    /** Limit (Completely Fair Scheduler) period. */
    /** Limit CPU CFS (Completely Fair Scheduler) quota, maps to {@code --cpu-quota=0}. */
    private int cpuQuoty;
    /** numCPUs. CPUs in which to allow execution (0-3, 0,1), maps to {@code --cpuset-cpus=}. */
    private int numCPUs;
    /** MEMs in which to allow execution, maps to {@code -cpuset-mems=(0-3, 0,1)}. */
    private String memory;
    /** Add a host device to the container, maps to {@code --device=[]}. */
    private Set<String> hostDevices = new TreeSet<>();

     /** Skip image verification, maps to {@code --disable-content-trust=true}. */
    private boolean disabledContentTrust=true;
    /** Custom DN Servers, maps to {@code --dns=[]}. */
    private Set<String> dnsServers = new TreeSet<>();
    /** Custom DN Search Domains, maps to {@code --dns-search=[]}. */
    private Set<String> dnsSearch = new TreeSet<>();
    /** The environment, maps to {@code e, --env=[]}. */
    private Map<String,String> environment = new HashMap<>();
    /** Override the default ENTRYPOINT of the image, maps to {@code --entrypoint= }. */
    private String entryPoint;
    /** Read in a file of environment variables, maps to {@code --env-file=[] }. */
    private List<String> environmentFiles = new ArrayList<>();
    /** Expose a port or a range of ports, maps to {@code --expose=[]}. */
    private Set<String> exposedPorts = new TreeSet<>();
    /** Adds additional groups to join, maps to {@code --group-add=[] }.. */
    private Set<String> groups = new TreeSet<>();
    /** THe container host name. */
    private String host;
    /** IPC namespace to use. Maps to {@code --ipc=} */
    private String ipcNS;
    /** container labels {@code -l, --label=[]}. */
    private Map<String,String> labels = new HashMap<>();
    /** Read in a line delimited file of labels, maps to {@code --label-file=[]}. */
    private Set<String> labelFields = new TreeSet<>();
    /** Add link to another container, maps to {@code --link=[]}. */
    private Set<String> links = new TreeSet<>();
    /** Logging driver for container, maps to {@code --log-driver=}. */
    private String loggingDriver;
    /** Add LXC, options to {@code --lxc-conf=[]}. */
    private Map<String,String> lxcConfig = new HashMap<>();
    /** Memory limit, maps to {@code -m, --memory=}. */
    private String memoryLimit;
    /** Container MAC address (e.g. 92:d0:c6:0a:29:33) to be used, maps to {@code --mac-address=}. */
    private String macAddress;
    /** Total memory (memory + swap), '-1' to disable swap, maps to {@code --memory-swap= }. */
    private int totalMemory;
    /** Tuning container memory swappiness (0 to 100), maps to {@code --memory-swappiness=-1 }. */
    private int memorySwapiness;
    /** Container name, maps to {@code --name=}. */
    private String name;
    /** Set the Network mode for the container, maps to {@code --net=default }. */
    private NetworkMode networkMode;
    /** Disable OOM Killer, maps to {@code --oom-kill-disable=false}. */
    private boolean disableDOMKill;
    /** Publish all exposed ports to random ports, maps to {@code -P, --publish-all=false}. */
    private boolean publishedRandomPorts;
    /** Publish a container's port(s) to the host, maps to {@code -p, --publish=[]}. */
    private Set<String> ports = new TreeSet<>();
    /** PID namespace to use, maps to {@code --pid=  }. */
    private String pidNamespace;
    /** Give extended privileges to this container, maps to {@code -privileged=false}. */
    private boolean privileged;
    /** Mount the container's root filesystem as read only, maps to {@code --read-only=false}. */
    private boolean mountReadOnlyFS;
    /** Restart policy to apply when a container exits, maps to {@code --restart=no}. */
    private RestartPolicy restartPolicy;
    /** Publish a container's port(s) to the host, maps to {@code --security-opt=[]}. */
    private Map<String,String> securityOptions = new HashMap<>();
    /**  Username or UID (format: <name|uid>[:<group|gid>]), maps to {@code -u, --user= }. */
    private String user;
    /** ULimit options, maps to {@code --ulimit=[]}. */
    private Set<String> ulimitOptions = new TreeSet<>();
    /** UTS namespace to use, maps to {@code --uts=}. */
    private String utsNamespace;
    /** Bind to volumes, maps to {@code -v, --volume=[]}. */
    private Map<String,String> volumes = new HashMap<>();
    /** Optional volume driver {@code --volume-driver= }. */
    private String volumeDriver;
    /** Mount volumes from specified containers, maps to {@code --volumes-from=[]}. */
    private Set<String> volumesFrom = new TreeSet<>();
    /** Working directory inside the container, maps to {@code-w, --workdir=  }. */
    private String workDir;

    /**
     * Instantiates a new Container request.
     */
    public ContainerRequest(Region region){
        this(region, 1);
    }

    /**
     * Instantiates a new Container request.
     */
    public ContainerRequest(Region region, int scale){
        this.targetRegion = Objects.requireNonNull(region);
        this.scale = scale;
    }

    /**
     * Gets custom host to ip mappings.
     *
     * @return the custom host to ip mappings
     */
    public Set<String> getCustomHostToIPMappings() {
        return customHostToIPMappings;
    }

    /**
     * Gets blk io weight.
     *
     * @return the blk io weight
     */
    public int getBlkIOWeight() {
        return blkIOWeight;
    }

    /**
     * Gets cpu shares.
     *
     * @return the cpu shares
     */
    public int getCpuShares() {
        return cpuShares;
    }

    /**
     * Gets add capabilities.
     *
     * @return the add capabilities
     */
    public Set<String> getAddCapabilities() {
        return addCapabilities;
    }

    /**
     * Gets remove capabilities.
     *
     * @return the remove capabilities
     */
    public Set<String> getRemoveCapabilities() {
        return removeCapabilities;
    }

    /**
     * Gets parent cgroup.
     *
     * @return the parent cgroup
     */
    public String getParentCgroup() {
        return parentCgroup;
    }

    /**
     * Gets cidfile.
     *
     * @return the cidfile
     */
    public String getCidfile() {
        return cidfile;
    }

    /**
     * Gets cpu period.
     *
     * @return the cpu period
     */
    public int getCpuPeriod() {
        return cpuPeriod;
    }

    /**
     * Gets cpu quoty.
     *
     * @return the cpu quoty
     */
    public int getCpuQuoty() {
        return cpuQuoty;
    }

    /**
     * Gets num cp us.
     *
     * @return the num cp us
     */
    public int getNumCPUs() {
        return numCPUs;
    }

    /**
     * Gets memory.
     *
     * @return the memory
     */
    public String getMemory() {
        return memory;
    }

    /**
     * Gets host devices.
     *
     * @return the host devices
     */
    public Set<String> getHostDevices() {
        return hostDevices;
    }

    /**
     * Is disabled content trust boolean.
     *
     * @return the boolean
     */
    public boolean isDisabledContentTrust() {
        return disabledContentTrust;
    }

    /**
     * Gets dns servers.
     *
     * @return the dns servers
     */
    public Set<String> getDnsServers() {
        return dnsServers;
    }

    /**
     * Gets dns search.
     *
     * @return the dns search
     */
    public Set<String> getDnsSearch() {
        return dnsSearch;
    }

    /**
     * Gets environment.
     *
     * @return the environment
     */
    public Map<String, String> getEnvironment() {
        return environment;
    }

    /**
     * Gets entry point.
     *
     * @return the entry point
     */
    public String getEntryPoint() {
        return entryPoint;
    }

    /**
     * Gets environment files.
     *
     * @return the environment files
     */
    public List<String> getEnvironmentFiles() {
        return environmentFiles;
    }

    /**
     * Gets exposed ports.
     *
     * @return the exposed ports
     */
    public Set<String> getExposedPorts() {
        return exposedPorts;
    }

    /**
     * Gets groups.
     *
     * @return the groups
     */
    public Set<String> getGroups() {
        return groups;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets ipc ns.
     *
     * @return the ipc ns
     */
    public String getIpcNS() {
        return ipcNS;
    }

    /**
     * Gets labels.
     *
     * @return the labels
     */
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * Gets label fields.
     *
     * @return the label fields
     */
    public Set<String> getLabelFields() {
        return labelFields;
    }

    /**
     * Gets links.
     *
     * @return the links
     */
    public Set<String> getLinks() {
        return links;
    }

    /**
     * Gets logging driver.
     *
     * @return the logging driver
     */
    public String getLoggingDriver() {
        return loggingDriver;
    }

    /**
     * Gets lxc config.
     *
     * @return the lxc config
     */
    public Map<String, String> getLxcConfig() {
        return lxcConfig;
    }

    /**
     * Gets memory limit.
     *
     * @return the memory limit
     */
    public String getMemoryLimit() {
        return memoryLimit;
    }

    /**
     * Gets mac address.
     *
     * @return the mac address
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Gets total memory.
     *
     * @return the total memory
     */
    public int getTotalMemory() {
        return totalMemory;
    }

    /**
     * Gets memory swapiness.
     *
     * @return the memory swapiness
     */
    public int getMemorySwapiness() {
        return memorySwapiness;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets network mode.
     *
     * @return the network mode
     */
    public NetworkMode getNetworkMode() {
        return networkMode;
    }

    /**
     * Is disable dom kill boolean.
     *
     * @return the boolean
     */
    public boolean isDisableDOMKill() {
        return disableDOMKill;
    }

    /**
     * Is published random ports boolean.
     *
     * @return the boolean
     */
    public boolean isPublishedRandomPorts() {
        return publishedRandomPorts;
    }

    /**
     * Gets ports.
     *
     * @return the ports
     */
    public Set<String> getPorts() {
        return ports;
    }

    /**
     * Gets pid namespace.
     *
     * @return the pid namespace
     */
    public String getPidNamespace() {
        return pidNamespace;
    }

    /**
     * Is privileged boolean.
     *
     * @return the boolean
     */
    public boolean isPrivileged() {
        return privileged;
    }

    /**
     * Is mount read only fs boolean.
     *
     * @return the boolean
     */
    public boolean isMountReadOnlyFS() {
        return mountReadOnlyFS;
    }

    /**
     * Gets restart policy.
     *
     * @return the restart policy
     */
    public RestartPolicy getRestartPolicy() {
        return restartPolicy;
    }

    /**
     * Gets security options.
     *
     * @return the security options
     */
    public Map<String,String> getSecurityOptions() {
        return securityOptions;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets ulimit options.
     *
     * @return the ulimit options
     */
    public Set<String> getUlimitOptions() {
        return ulimitOptions;
    }

    /**
     * Gets uts namespace.
     *
     * @return the uts namespace
     */
    public String getUtsNamespace() {
        return utsNamespace;
    }

    /**
     * Gets volumes.
     *
     * @return the volumes
     */
    public Map<String, String> getVolumes() {
        return volumes;
    }

    /**
     * Gets volume driver.
     *
     * @return the volume driver
     */
    public String getVolumeDriver() {
        return volumeDriver;
    }

    /**
     * Gets volumes from.
     *
     * @return the volumes from
     */
    public Set<String> getVolumesFrom() {
        return volumesFrom;
    }

    /**
     * Gets work dir.
     *
     * @return the work dir
     */
    public String getWorkDir() {
        return workDir;
    }

    public ContainerRequest addCustomHostToIPMapping(String... customHostToIPMappings) {
        for(String mapping:customHostToIPMappings){
            this.customHostToIPMappings.add(mapping);
        }
        return this;
    }

    public ContainerRequest setBlkIOWeight(int blkIOWeight) {
        this.blkIOWeight = blkIOWeight;
        return this;
    }

    public ContainerRequest setCpuShares(int cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    public ContainerRequest addCapabilities(String... capabilities) {
        for(String cap:capabilities){
            this.addCapabilities.add(cap);
        }
        return this;
    }

    public ContainerRequest removeCapabilities(String... capabilities) {
        for(String cap:capabilities){
            this.removeCapabilities.add(cap);
        }
        return this;
    }

    public ContainerRequest setParentCgroup(String parentCgroup) {
        this.parentCgroup = parentCgroup;
        return this;
    }

    public ContainerRequest setCidfile(String cidfile) {
        this.cidfile = cidfile;
        return this;
    }

    public ContainerRequest setCpuPeriod(int cpuPeriod) {
        this.cpuPeriod = cpuPeriod;
        return this;
    }

    public ContainerRequest setCpuQuoty(int cpuQuoty) {
        this.cpuQuoty = cpuQuoty;
        return this;
    }

    public ContainerRequest setNumCPUs(int numCPUs) {
        this.numCPUs = numCPUs;
        return this;
    }

    public ContainerRequest setMemory(String memory) {
        this.memory = memory;
        return this;
    }

    public ContainerRequest addHostDevices(String... hostDevices) {
        for(String hd: hostDevices) {
            this.hostDevices.add(hd);
        }
        return this;
    }

    public ContainerRequest setDisabledContentTrust(boolean disabledContentTrust) {
        this.disabledContentTrust = disabledContentTrust;
        return this;
    }

    public ContainerRequest addDnsServers(String... dnsServers) {
        for(String dns: hostDevices) {
            this.dnsServers.add(dns);
        }
        return this;
    }

    public ContainerRequest addDnsSearchDomains(String... dnsSearch) {
        for(String s: dnsSearch) {
            this.dnsSearch.add(s);
        }
        return this;
    }

    public ContainerRequest setEnvironment(Map<String, String> environment) {
        this.environment = environment;
        return this;
    }

    public ContainerRequest setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
        return this;
    }

    public ContainerRequest setEnvironmentFiles(List<String> environmentFiles) {
        this.environmentFiles = environmentFiles;
        return this;
    }

    public ContainerRequest setExposedPorts(Set<String> exposedPorts) {
        this.exposedPorts = exposedPorts;
        return this;
    }

    public ContainerRequest setGroups(Set<String> groups) {
        this.groups = groups;
        return this;
    }

    public ContainerRequest setHost(String host) {
        this.host = host;
        return this;
    }

    public ContainerRequest setIpcNS(String ipcNS) {
        this.ipcNS = ipcNS;
        return this;
    }

    public ContainerRequest setLabel(String key, String value) {
        this.labels.put(key, value);
        return this;
    }

    public ContainerRequest setLabelField(String... labelFields) {
        for(String lf:labelFields){
            this.labelFields.add(lf);
        }
        return this;
    }

    public ContainerRequest setLinks(Set<String> links) {
        this.links = links;
        return this;
    }

    public ContainerRequest setLoggingDriver(String loggingDriver) {
        this.loggingDriver = loggingDriver;
        return this;
    }

    public ContainerRequest setLxcConfig(String key, String value) {
        this.lxcConfig.put(key, value);
        return this;
    }

    public ContainerRequest setMemoryLimit(String memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    public ContainerRequest setMacAddress(String macAddress) {
        this.macAddress = macAddress;
        return this;
    }

    public ContainerRequest setTotalMemory(int totalMemory) {
        this.totalMemory = totalMemory;
        return this;
    }

    public ContainerRequest setMemorySwapiness(int memorySwapiness) {
        this.memorySwapiness = memorySwapiness;
        return this;
    }

    public ContainerRequest setName(String name) {
        this.name = name;
        return this;
    }

    public ContainerRequest setNetworkMode(NetworkMode networkMode) {
        this.networkMode = networkMode;
        return this;
    }

    public ContainerRequest setDisableDOMKill(boolean disableDOMKill) {
        this.disableDOMKill = disableDOMKill;
        return this;
    }

    public ContainerRequest setPublishedRandomPorts(boolean publishedRandomPorts) {
        this.publishedRandomPorts = publishedRandomPorts;
        return this;
    }

    public ContainerRequest addPorts(String... ports) {
        for(String p:ports){
            this.ports.add(p);
        }
        return this;
    }

    public ContainerRequest setPidNamespace(String pidNamespace) {
        this.pidNamespace = pidNamespace;
        return this;
    }

    public ContainerRequest setPrivileged(boolean privileged) {
        this.privileged = privileged;
        return this;
    }

    public ContainerRequest setMountReadOnlyFS(boolean mountReadOnlyFS) {
        this.mountReadOnlyFS = mountReadOnlyFS;
        return this;
    }

    public ContainerRequest setRestartPolicy(RestartPolicy restartPolicy) {
        this.restartPolicy = restartPolicy;
        return this;
    }

    public ContainerRequest setSecurityOption(String key, String securityOptions) {
        this.securityOptions.put(key, securityOptions);
        return this;
    }

    public ContainerRequest setUser(String user) {
        this.user = user;
        return this;
    }

    public ContainerRequest setUlimitOptions(Set<String> ulimitOptions) {
        this.ulimitOptions = ulimitOptions;
        return this;
    }

    public ContainerRequest setUtsNamespace(String utsNamespace) {
        this.utsNamespace = utsNamespace;
        return this;
    }

    public ContainerRequest setVolumes(Map<String, String> volumes) {
        this.volumes = volumes;
        return this;
    }

    public ContainerRequest setVolumeDriver(String volumeDriver) {
        this.volumeDriver = volumeDriver;
        return this;
    }

    public ContainerRequest setVolumesFrom(Set<String> volumesFrom) {
        this.volumesFrom = volumesFrom;
        return this;
    }

    public ContainerRequest setWorkDir(String workDir) {
        this.workDir = workDir;
        return this;
    }

    public DockerContainer create(){
        // TODO call docker create
        return null;
    }
}
