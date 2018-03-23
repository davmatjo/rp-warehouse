package rp.warehouse.pc.route;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RobotLocation;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoutePlanTest {

    @Test
    void planningToDropoffWhenDropoffBlockedWorks() {
        Robot mockedPlanningRobot = mock(Robot.class);
        when(mockedPlanningRobot.getLocation()).thenReturn(new RobotLocation(6, 7, 3));

        Robot mockedStaticRobot = mock(Robot.class);
        when(mockedStaticRobot.getLocation()).thenReturn(new RobotLocation(4, 7, 3));
        when(mockedStaticRobot.getPreviousLocation()).thenReturn(new RobotLocation(4, 7, 3));

        Route mockedPlan = mock(Route.class);
        when(mockedPlan.getLocation(Matchers.anyInt())).thenThrow(new IndexOutOfBoundsException());
        when(mockedStaticRobot.getRoute()).thenReturn(mockedPlan);

        RoutePlan.setRobots(new ArrayList<>(Arrays.asList(mockedPlanningRobot, mockedStaticRobot)));

        Route route = RoutePlan.planDropOff(mockedPlanningRobot);

        Assertions.assertEquals(Protocol.WEST, route.poll());
        Assertions.assertEquals(Protocol.WAITING, route.poll());
    }

    @Test
    void planBlockedReturnsWait() {
        Robot mockedPlanningRobot = mock(Robot.class);
        when(mockedPlanningRobot.getLocation()).thenReturn(new RobotLocation(0, 1, 3));

        Robot mockedStaticRobot = mock(Robot.class);
        when(mockedStaticRobot.getLocation()).thenReturn(new RobotLocation(0, 0, 3));
        when(mockedStaticRobot.getPreviousLocation()).thenReturn(new RobotLocation(0, 0, 3));

        Route mockedPlan = mock(Route.class);
        when(mockedPlan.getLocation(Matchers.anyInt())).thenThrow(new IndexOutOfBoundsException());
        when(mockedStaticRobot.getRoute()).thenReturn(mockedPlan);

        RoutePlan.setRobots(new ArrayList<>(Arrays.asList(mockedPlanningRobot, mockedStaticRobot)));

        Route route = RoutePlan.plan(mockedPlanningRobot, new Location(0, 0));

        Assertions.assertEquals(Protocol.WAITING, route.poll());
    }

    @Test
    void planningToDropoffGivesDropoffAtEnd() {
        Robot mockedPlanningRobot = mock(Robot.class);
        when(mockedPlanningRobot.getLocation()).thenReturn(new RobotLocation(3, 7, 3));

        RoutePlan.setRobots(new ArrayList<>());

        Route route = RoutePlan.planDropOff(mockedPlanningRobot);

        Assertions.assertEquals(route.poll(), Protocol.EAST);
        Assertions.assertEquals(route.poll(), Protocol.DROPOFF);
    }

    @Test
    void planningToPickupGivesPickupAtEnd() {
        Robot mockedPlanningRobot = mock(Robot.class);
        when(mockedPlanningRobot.getLocation()).thenReturn(new RobotLocation(1, 0, 3));

        RoutePlan.setRobots(new ArrayList<>());

        Route route = RoutePlan.plan(mockedPlanningRobot, new Location(0, 0));

        Assertions.assertEquals(route.poll(), Protocol.WEST);
        Assertions.assertEquals(route.poll(), Protocol.PICKUP);
    }

    @Test
    void planningToBlockedPickupGivesWait() {
        Robot mockedPlanningRobot = mock(Robot.class);
        when(mockedPlanningRobot.getLocation()).thenReturn(new RobotLocation(9, 7, 3));

        Robot mockedStaticRobot = mock(Robot.class);
        when(mockedStaticRobot.getLocation()).thenReturn(new RobotLocation(11, 7, 3));

        Route mockedPlan = mock(Route.class);
        when(mockedPlan.getLocation(Matchers.anyInt())).thenThrow(new IndexOutOfBoundsException());
        when(mockedStaticRobot.getRoute()).thenReturn(mockedPlan);

        RoutePlan.setRobots(new ArrayList<>(Arrays.asList(mockedPlanningRobot, mockedStaticRobot)));

        Route route = RoutePlan.plan(mockedPlanningRobot, new Location(11, 7));

        Assertions.assertEquals(Protocol.EAST, route.poll());
        Assertions.assertEquals(Protocol.WAITING, route.poll());
    }

}