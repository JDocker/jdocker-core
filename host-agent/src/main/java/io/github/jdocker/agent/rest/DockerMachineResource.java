package io.github.jdocker.agent.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jdocker.MachineConfig;
import io.github.jdocker.agent.DockerAgent;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/")
public class DockerMachineResource {

    @GET
    @Timed
    @Path("/machines")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public List<String> listMachines() throws IOException {
        return DockerAgent.getInstance().machineList();
    }

    @GET
    @Path("/machines/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String inspectMachine(@PathParam("name") String machineName, @QueryParam("action") String action) throws IOException {
        switch(action){
            case "start":
                return DockerAgent.getInstance().machineStart(machineName);
            case "stop":
                return DockerAgent.getInstance().machineStop(machineName);
            case "restart":
                return DockerAgent.getInstance().machineRestart(machineName);
            case "kill":
                return DockerAgent.getInstance().machineKill(machineName);
            case "ip":
                return DockerAgent.getInstance().machineIP(machineName);
            case "inspect":
                return DockerAgent.getInstance().machineInspect(machineName);
            case "upgrade":
                return DockerAgent.getInstance().machineUpgrade(machineName);
            default:
                return DockerAgent.getInstance().machineStatus(machineName);
        }
    }

    @POST
    @Path("/machines/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String createMachine(@PathParam("name") String machineName, MachineConfig config) throws IOException {
        return DockerAgent.getInstance().machineCreate(config);
//        MachineConfig.builder(machineName).parse(json).build())
    }

    @DELETE
    @Path("/machines/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String deleteMachine(@PathParam("name") String machineName) throws IOException {
        return DockerAgent.getInstance().machineDelete(machineName);
    }

    @GET
    @Path("/machines/{name}/status")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public String getMachineStatus(@PathParam("name") String machineName) throws IOException {
        return DockerAgent.getInstance().machineStatus(machineName);
    }

}