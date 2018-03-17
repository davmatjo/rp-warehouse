package rp.warehouse.pc.communication;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.localisation.Ranges;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

public class CommunicationTestSingle {
    private static Communication communication;
    private static final String[] robotNames = new String[] {"ExpressBoi", "Meme Machine", "Orphan"};
    private static final String[] robotIDs = new String[] {"0016531AFBE1", "0016531501CA", "0016531303E0"};

    @BeforeClass
    public static void setup() throws Exception {
        communication = new Communication(robotIDs[0], robotNames[0], mock(Robot.class));
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(communication);
    }

    @Test
    public void sendMovement() {
        communication.sendMovement(Protocol.NORTH);
        communication.sendMovement(Protocol.EAST);
        communication.sendMovement(Protocol.SOUTH);
        communication.sendMovement(Protocol.WEST);
    }

    @Test
    public void sendPickupRequest() {
        int response = communication.sendLoadingRequest(1);
        Assert.assertTrue(response > 0);
    }

    @Test
    public void getRanges() {
        Ranges response = communication.getRanges();
        Assert.assertNotNull(response);
    }

    @Test
    public void close() {
        communication.close();
    }
}