package rp.warehouse.pc.assignment;

import org.apache.log4j.Logger;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.data.robot.utils.RobotLocation;
import rp.warehouse.pc.input.Job;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// It all works I think
// This is the big boy you want to make to replace SimpleAssigner

/**
 * Assigns items in a job to robots through auctioning
 *
 * @author Dylan
 */
public class Auctioner {

    private static final TSP tsp = new TSP();
    private static final Logger logger = Logger.getLogger(Auctioner.class);
    private List<Job> jobs;
    private List<Location> robots;

    /**
     * @param jobs   List of ordered jobs
     * @param robots List of robot locations
     */
    public Auctioner(List<Job> jobs, List<RobotLocation> robots) {
        this.jobs = jobs;
        this.robots = new ArrayList<>(robots);
    }

    /**
     * Assigns all items
     */
    public List<Queue<Task>> assign() {
        return auction();
    }

    /**
     * Will Auction off the items in a job. Each robot bids on the item closest to
     * their location. Winner is the bid with the lowest cost, winner is assigned
     * their chosen item. Repeat until all the items in the job are assigned
     */
    private List<Queue<Task>> auction() {
        List<Queue<Task>> assignedItems = new ArrayList<>();
        for (int i = 0; i < robots.size(); i++) {
            assignedItems.add(new LinkedList<>());
        }

        while (!jobs.isEmpty()) {
            Job job = jobs.get(0);
            jobs.remove(0);
            logger.debug("Assigning next job");

            List<Queue<Task>> assigning = new ArrayList<>();
            for (int i = 0; i < robots.size(); i++) {
                assigning.add(new LinkedList<>());
            }

            ArrayList<Task> unassignedItems = job.getItems();

            // Auction items
            while (!unassignedItems.isEmpty()) {
                ArrayList<Bid> bids = new ArrayList<>();
                for (int i = 0; i < assigning.size(); i++) {
                    bids.add(getBid(job, assigning.get(i), i));
                }

                Bid winner = bids.get(0);
                for (Bid bid : bids) {
                    if (bid.getItemOrder().getPathCost() < winner.getItemOrder().getPathCost()) {
                        winner = bid;
                    }
                }

                assigning.set(winner.getOwner(), winner.getItemOrder().getOrder());
                unassignedItems.remove(winner.getItem());
                logger.trace("Item assigned to robot " + robots.get(winner.getOwner()));
            }

            // Final assignment
            for (int i = 0; i < assignedItems.size(); i++) {
                for (Task assignedTask : assigning.get(i)) {
                    assignedItems.get(i).offer(assignedTask);
                }
            }

            // Update locations
            for (int i = 0; i < assignedItems.size(); i++) {
                LinkedList<Task> robotTasks = (LinkedList<Task>) assignedItems.get(i);
                if (!robotTasks.isEmpty()) {
                    robots.remove(i);
                    robots.add(i, robotTasks.get(robotTasks.size() - 1).getItem().getLocation());
                }
            }
            logger.trace("All items assigned");
        }
        logger.debug("All jobs assigned");

        return assignedItems;
    }

    /**
     * Chooses the closest item in the job to a robot and 'bids' on it
     *
     * @param job          The job
     * @param currentItems The items the robot has won
     * @param robot        The robot bidding
     * @return The robot's bid
     */
    private Bid getBid(Job job, Queue<Task> currentItems, int robot) {
        Task bidItem = null;
        int bidVal = Integer.MAX_VALUE;
        ItemOrder bidOrder = null;

        for (Task task : job.getItems()) {
            ItemOrder newOrder = tsp.insertMinimumEdge(task, currentItems, robots.get(robot));
            int newCost = newOrder.getPathCost();
            if (newCost < bidVal) {
                bidItem = task;
                bidVal = newCost;
                bidOrder = newOrder;
            }
        }
        return new Bid(bidItem, robot, bidOrder);
    }


}
