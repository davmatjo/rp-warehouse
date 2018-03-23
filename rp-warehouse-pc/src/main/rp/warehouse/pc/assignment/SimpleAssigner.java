package rp.warehouse.pc.assignment;

import org.apache.log4j.Logger;
import rp.warehouse.pc.data.Task;
import rp.warehouse.pc.input.Job;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A simple job assigner Splits items between robots evenly
 * @deprecated - dxj786
 * @author Dylan
 */
public class SimpleAssigner {

    private static final Logger logger = Logger.getLogger(SimpleAssigner.class);
    private ArrayList<Job> jobs;

    public SimpleAssigner(ArrayList<Job> jobs) {
        this.jobs = jobs;
        assign();
    }

    private void assign() {
        ArrayList<Queue<Task>> assignedItems = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            assignedItems.add(new LinkedList<>());
        }

        while (!jobs.isEmpty()) {

            Job job = jobs.get(0);
            jobs.remove(0);
            logger.debug("Next job");

            ArrayList<Task> unassignedItems = job.getItems();

            int i = 0;
            while (!unassignedItems.isEmpty()) {
                Task nextItem = unassignedItems.get(0);
                unassignedItems.remove(0);


                assignedItems.get(i).add(nextItem);
                logger.trace("Item assigned to robot " + i);

                i++;
                if (i >= assignedItems.size())
                    i = 0;
            }
            logger.trace("All items from job assigned");

        }
        logger.debug("All jobs assigned");

    }
}
