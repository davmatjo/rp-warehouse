package rp.warehouse.pc.data.robot;

import org.junit.Test;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RobotLocation;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RobotTest {
    public Robot robot ;
    
    @Before
    public void setup() {
        Queue<Task> items= new LinkedList<Task>(Arrays.asList(new Task(new Item(2.0f,3.0f), 2, "d2")));
        //ExecutorService pool = Executors.newFixedThreadPool(1 * 2);
        Communication mockedCommunications = mock(Communication.class);
        ExecutorService mockedPool = mock(ExecutorService.class);
        try {
            robot = new Robot("0016531AFBE1", "ExpressBoi", items, mockedPool, new RobotLocation(0, 0, 3));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void idTest() {
        Communication mockedCommunications = mock(Communication.class);
        
        
        ExecutorService mockedPool = mock(ExecutorService.class);
        //when(mockedPool.execute(mockedCommunications)).thenReturn(null);
        setup();
        Assertions.assertEquals(robot.getID(),"0016531AFBE1");
        
    }
    
    @Test
    public void routeTest() {
        Communication mockedCommunications = mock(Communication.class);
        
        ExecutorService mockedPool = mock(ExecutorService.class);
        //when(mockedPool.execute(mockedCommunications)).thenReturn(null);
        setup();
        
        robot.cancelJob();
        robot.getLocation();
        robot.getTask();
        robot.toString();
        robot.run();
        
        Assertions.assertEquals(robot.getRoute(),null);

    }
    
    @Test
    public void nameTest() {
        Communication mockedCommunications = mock(Communication.class);
        
        ExecutorService mockedPool = mock(ExecutorService.class);
        //when(mockedPool.execute(mockedCommunications)).thenReturn(null);
        setup();
        Assertions.assertEquals(robot.getName(),"ExpressBoi");   
    }

}
