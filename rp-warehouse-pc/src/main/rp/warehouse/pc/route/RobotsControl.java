package rp.warehouse.pc.route;

import org.apache.log4j.Logger;

import rp.warehouse.pc.communication.Communication;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.Robot;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.localisation.NoIdeaException;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.LoadingFrame;
import rp.warehouse.pc.management.LocalisationView;
import rp.warehouse.pc.management.MainView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Used to link different part of the system together
 * 
 * Job Assignment   - Can use this class to initially create Array of Robots
 * 
 * Route planning   - Can use to access robot classes to plan
 * 
 * Warehouse MI     - Can use this class to get Robot classes to get all the
 * required data
 * 
 * @author roman
 *
 */
public class RobotsControl {
    private static final ArrayList<Robot> robots = new ArrayList<Robot>();
    
    //Will crash as only has one element 
    private static final String[] robotNames = new String[] {"ExpressBoi", "Meme Machine", "Jarvis"};
    private static final String[] robotIDs = new String[] {"0016531AFBE1", "0016531501CA", "00165312C16A"};
    private static final RobotLocation[] robotLocations = new RobotLocation[] {new RobotLocation(0, 0, 3),
    new RobotLocation(11, 7, 3), new RobotLocation(0, 7, 3)};
    private static ArrayList<Queue<Task>> listOfItems;
    
    private static final Logger logger = Logger.getLogger(RobotsControl.class);

    /**
     * For: Job Selection When the the items have been split between robots (number
     * of robots should be passed to you by Job Selection) pass Queue of items for
     * each of the robot.
     * 
     * If there are 3 robots, there should be 3 queues in the ArrayList and the size
     * of the Array should be 3
     * 
     * If there is only 1, then there should be just one queue in the ArrayList and
     * the size of the array should be 1
     * 
     */
    public static void addRobots(ArrayList<Queue<Task>> _listOfItems) {
        listOfItems=_listOfItems;
        logger.debug("Starting Robot Creation");

        ExecutorService pool = Executors.newFixedThreadPool(listOfItems.size() * 2);
        int i = 0;

        RoutePlan.setRobots(robots);
        
        // Create robots
        for (Queue<Task> items : listOfItems) {
            logger.trace("Robot " + i + " is being created" );

            try {
                Communication comms = new Communication(robotIDs[i], robotNames[i]);
                pool.execute(comms);

                LoadingFrame.finishedLoading();
                Localiser localiser = new Localiser(comms);

                new LocalisationView(localiser);
                RobotLocation location = localiser.getPosition();

                Robot newRobot = new Robot(robotIDs[i], robotNames[i], items, comms, location);
                robots.add(newRobot);

                logger.debug("Robot " + robotNames[i] + " created");

            } catch (IOException e) {
                logger.error("Could not connect to " + robotNames[i]);
            } catch (NoIdeaException e) {
                logger.error("Could not localise " + robotNames[i]);
            }

            i++;
        }
        
        
        // Runs Robot threads
        for (Robot robot : robots) {
            //robot.localiseRobot();
            pool.execute(robot);
        }
        logger.debug("Array of Robots has been created with " + robots.size() + " robots");

        LoadingFrame.finishedLoading();
        new MainView(robots);

        // Shut down the pool to prevent new threads being created, and allow the program to end
        pool.shutdown();
    }

    public static void setRobotData(String[] _robotNames, String [] _robotIDs, RobotLocation[] _robotLocations) {
//        robotNames =_robotNames;
//        robotIDs = _robotIDs;
//        robotLocations = _robotLocations;
    }
}
//
//  `\_('_')_/`
//