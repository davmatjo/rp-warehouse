package rp.warehouse.pc.communication;

import org.junit.*;
import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.localisation.Ranges;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;

public class CommunicationTestMultiple {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(6);
    private static Communication[] communications;
    private static final String[] robotNames = new String[] {"ExpressBoi", "Meme Machine", "Orphan"};
    private static final String[] robotIDs = new String[] {"0016531AFBE1", "0016531501CA", "0016531303E0"};

    @BeforeClass
    public static void setup() throws Exception {
        for (int i=0; i < 3; i++) {
            communications[i] = new Communication(robotIDs[i], robotNames[i], mock(Robot.class));
            executorService.execute(communications[i]);
        }
    }

    @Test
    public void sendMovement() {
        executorService.execute(() -> {
            communications[0].sendMovement(Protocol.NORTH);
            communications[0].sendMovement(Protocol.EAST);
            communications[0].sendMovement(Protocol.SOUTH);
            communications[0].sendMovement(Protocol.WEST);
        });
        executorService.execute(() -> {
            communications[1].sendMovement(Protocol.NORTH);
            communications[1].sendMovement(Protocol.EAST);
            communications[1].sendMovement(Protocol.SOUTH);
            communications[1].sendMovement(Protocol.WEST);
        });
        executorService.execute(() -> {
            communications[2].sendMovement(Protocol.NORTH);
            communications[2].sendMovement(Protocol.EAST);
            communications[2].sendMovement(Protocol.SOUTH);
            communications[2].sendMovement(Protocol.WEST);
        });
    }

    @Test
    public void sendPickupRequest() {
        executorService.execute(() -> {
            int response = communications[0].sendLoadingRequest(1);
            Assert.assertTrue(response > 0);
        });
        executorService.execute(() -> {
            int response = communications[1].sendLoadingRequest(1);
            Assert.assertTrue(response > 0);
        });
        executorService.execute(() -> {
            int response = communications[2].sendLoadingRequest(1);
            Assert.assertTrue(response > 0);
        });
    }

    @Test
    public void getRanges() {
        executorService.execute(() -> {
            Ranges response = communications[0].getRanges();
            Assert.assertNotNull(response);
        });
        executorService.execute(() -> {
            Ranges response = communications[1].getRanges();
            Assert.assertNotNull(response);
        });
        executorService.execute(() -> {
            Ranges response = communications[2].getRanges();
            Assert.assertNotNull(response);
        });
    }

    @Test
    public void close() {
        communications[0].close();
        communications[1].close();
        communications[2].close();
    }

    @AfterClass
    public static void after() {
        executorService.shutdown();
    }
}