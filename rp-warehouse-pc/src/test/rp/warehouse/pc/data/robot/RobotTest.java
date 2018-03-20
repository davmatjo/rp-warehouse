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
import java.util.Arrays;
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
        Item item = new Item("top", 6.0f, 3.0f);
        Item item2 = new Item("Legs", 2.0f, 1.0f);
        task1 = new Task(item, 2, "table");
        task2 = new Task(item2, 2, "table");
        tasks.add(task1);
        tasks.add(task2);
        Job job = new Job("table", tasks);
        jobs.addJob(job);
        
        RewardCounter.setJobs(jobs);
        
//        Item item = new Item("hel", 2.0f, 3.0f);
//        Item item2 = new Item("hel", 2.0f, 3.0f);
        Location location = new Location(3, 3);
        Location location2 = new Location(5, 3);
        
        item.setLocation(location);
        item2.setLocation(location2);
        
        Queue<Task> items= new LinkedList<Task>();
        items.add(task1);
        items.add(task2);
        
        Communication mockedCommunications = mock(Communication.class);
        when(mockedCommunications.sendLoadingRequest(2)).thenReturn(2);
        //ExecutorService mockedPool = mock(ExecutorService.class);
        ArrayList<Robot> robots = new ArrayList<>();
        try {
            robot = new Robot("0016531AFBE1", "ExpressBoi", items, mockedCommunications, new RobotLocation(0, 0, 3));
            robots.add(robot);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //robot.setComminications(mockedCommunications);
        RoutePlan.setRobots(robots);
        
    }

    @Test
    public void idTest() {
        System.out.println("Test 1");
        Communication mockedCommunications = mock(Communication.class);
        RoutePlan mockedRoutePlan = mock(RoutePlan.class);
        
        Route mockedRoute = mock(Route.class);
        Queue<Integer> route = new  LinkedList<>();
        route.add(Protocol.EAST);
        route.add(Protocol.EAST);
        route.add(Protocol.EAST);
        route.add(Protocol.NORTH);
        route.add(Protocol.NORTH);
        route.add(Protocol.NORTH);
        route.add(Protocol.PICKUP);
        
        when(mockedRoute.poll()).thenCallRealMethod();//thenReturn(route.poll());
        when(mockedRoute.peek()).thenReturn(route.peek());
        when(mockedRoute.isEmpty()).thenReturn(route.isEmpty());
        //RoutePlan newPlan = PowerMockito.spy(new RoutePlan());
        //PowerMockito.mockStatic(RoutePlan.class);
        //Mockito.when(RoutePlan.plan(robot, new Location(3, 3))).thenReturn(mockedRoute);
        ExecutorService mockedPool = mock(ExecutorService.class);
        
        robot.run();
//        Thread t = new Thread(robot);
//        t.start();
        System.out.println("Hell");
        
        Assertions.assertEquals(robot.getID(),"0016531AFBE1");
    }
//    
//    @Test
//    public void routeTest() {
//        System.out.println("Test 2");
//        Communication mockedCommunications = mock(Communication.class);
//        when(mockedCommunications.sendLoadingRequest(0)).thenReturn(0);
//        
//        ExecutorService mockedPool = mock(ExecutorService.class);
//        //when(mockedPool.execute(mockedCommunications)).thenReturn(null);
//        //setup();
//        
////        robot.cancelJob();
////        robot.getLocation();
////        robot.getTask();
////        robot.toString();
////        robot.run();
//        
//        Assertions.assertEquals(robot.getRoute(),null);
//
//    }
//    
//    @Test
//    public void nameTest() {
//        System.out.println("Test 3");
//        Communication mockedCommunications = mock(Communication.class);
//        
//        ExecutorService mockedPool = mock(ExecutorService.class);
//        //when(mockedPool.execute(mockedCommunications)).thenReturn(null);
//        //setup();
//        Assertions.assertEquals(robot.getName(),"ExpressBoi");   
//    }

}
