package rp.warehouse.pc.data.robot;

import org.junit.Test;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RewardCounter;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.input.Job;
import rp.warehouse.pc.input.Jobs;
import rp.warehouse.pc.route.Route;
import rp.warehouse.pc.route.RoutePlan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
/**
 * 
 * @author roman
 *
 */
public class RobotTest {
    public Robot robot ;
    private Jobs jobs;
    private Task task1;
    private Task task2;

    @Before
    public void setup() {
        jobs = new Jobs();
        ArrayList<Task> tasks = new ArrayList<>();
        Item item = new Item("top", 6.0f, 3.0f, new Location(3, 3));
        Item item2 = new Item("Legs", 2.0f, 1.0f, new Location(5, 3));
        task1 = new Task(item, 2, "table");
        task2 = new Task(item2, 2, "table");
        tasks.add(task1);
        tasks.add(task2);
        Job job = new Job("table", tasks);
        jobs.addJob(job);
        
        RewardCounter.setJobs(jobs);
        

        
        Queue<Task> items= new LinkedList<Task>();
        items.add(task1);
        items.add(task2);
        
        Communication mockedCommunications = mock(Communication.class);
        when(mockedCommunications.sendLoadingRequest(2)).thenReturn(2);

        ArrayList<Robot> robots = new ArrayList<>();
        try {
            robot = new Robot("0016531AFBE1", "ExpressBoi", items, mockedCommunications, new RobotLocation(0, 0, 3));
            robots.add(robot);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        RoutePlan.setRobots(robots);
        
    }

    @Test
    public void idTest() {
        System.out.println("Test 1");

        
        Route mockedRoute = mock(Route.class);
        Queue<Integer> route = new  LinkedList<>();
        route.add(Protocol.EAST);
        route.add(Protocol.EAST);
        route.add(Protocol.EAST);
        route.add(Protocol.NORTH);
        route.add(Protocol.NORTH);
        route.add(Protocol.NORTH);
        route.add(Protocol.PICKUP);
        
        when(mockedRoute.poll()).thenCallRealMethod();
        when(mockedRoute.peek()).thenReturn(route.peek());
        when(mockedRoute.isEmpty()).thenReturn(route.isEmpty());

        ExecutorService mockedPool = mock(ExecutorService.class);
        
        robot.run();
        
        Assertions.assertEquals(robot.getID(),"0016531AFBE1");
    }

}
