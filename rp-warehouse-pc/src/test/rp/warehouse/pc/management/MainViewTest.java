package rp.warehouse.pc.management;

import rp.warehouse.pc.data.Item;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.route.Route;
import rp.warehouse.pc.route.RoutePlan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * not a unit test, but attempts to set up a mocked environment for light gui testing
 */
class MainViewTest {

    public static void main(String[] args) {
        showUI();
    }

    static void showUI() {
        Robot robot1 = mock(Robot.class);
//        Robot robot2 = mock(Robot.class);
//        Robot robot3 = mock(Robot.class);

        when(robot1.getLocation()).thenReturn(new RobotLocation(0, 0, 3));
//        when(robot2.getLocation()).thenReturn(new RobotLocation(11, 7, 3));
//        when(robot3.getLocation()).thenReturn(new RobotLocation(0, 7, 3));

        when(robot1.getName()).thenReturn("ExpressBoi");
//        when(robot2.getName()).thenReturn("MemeMachine");
//        when(robot3.getName()).thenReturn("Orphan");

        RoutePlan.setRobots(new ArrayList<>());
        Route route = RoutePlan.plan(robot1, new Location(0, 3));
//        Route route2 = RoutePlan.plan(robot2, new Location(8, 5));
//        Route route3 = RoutePlan.plan(robot3, new Location(3, 6));

        when(robot1.getRoute()).thenReturn(route);
//        when(robot1.getRoute()).thenReturn(route2);
//        when(robot1.getRoute()).thenReturn(route3);

        Item item = new Item("", 1f, 1f);
        item.setLocation(new Location(0, 5));
        when(robot1.getTask()).thenReturn(new Task(item, 1, "a"));
//        when(robot2.getTask()).thenReturn(new Task(item, 1, "a"));
//        when(robot3.getTask()).thenReturn(new Task(item, 1, "a"));

        List<Robot> robots = new ArrayList<>(Arrays.asList(robot1));

        new MainView(robots);

        while (true);
    }

}