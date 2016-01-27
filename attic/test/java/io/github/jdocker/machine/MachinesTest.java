package io.github.jdocker.machine;

import io.github.jdocker.MachineConfig;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by atsticks on 12.01.16.
 */
public class MachinesTest {

    @Test
    public void testGetMachineNames() throws Exception {
        List<String> names = Machines.getMachineNames();
        assertNotNull(names);
        System.out.println("MACHINENAMES: " + names);
    }

    @Test
    public void testGetMachinesInfo() throws Exception {
        List<MachineConfig> machines = Machines.getMachinesInfo();
        assertNotNull(machines);
        System.out.println("MACHINES: " + machines);
    }

    @Test @Category(DockerMachineTest.class)
    public void testGetMachine() throws Exception {
        MachineConfig machine = Machines.getMachine("swarm-agent-00");
        assertNotNull(machine);
        assertEquals(machine.getName(), "swarm-agent-00");
    }

    @Test
    public void testStopRunning() throws Exception {

    }

    @Test
    public void testStopRunning1() throws Exception {

    }

    @Test
    public void testStartNotRunning() throws Exception {

    }

    @Test
    public void testStartNotRunning1() throws Exception {

    }
}