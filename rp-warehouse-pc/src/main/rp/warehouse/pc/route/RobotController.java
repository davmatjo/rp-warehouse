package rp.warehouse.pc.route;

import rp.warehouse.pc.data.Robot;
import rp.warehouse.pc.data.Robot.Response;

/**
 * 
 * @author roman
 *
 */
public class RobotController extends Thread {

    Robot robot = null;

    public RobotController(Robot _robot) {
        robot = _robot;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void run() {
        Response answer = null;
        while (true) {
            // check if there are more items in queue
            //

            robot.getCurrentJob();// can't check if empty or not
            // it doesn't return anything
            
            if (true) {// If nothing left in the currentRoute
                switch (answer) {
                case WAITING:

                    break;
                case FAIL:
                    System.exit(1);
                    break;
                case OK:

                    break;

                default:
                    break;
                }
            }
            
            robot.move();
            robot.getResponse(); // blocking
        }
    }

    /**
     * When called cancels Job of the current item
     */
    public void cancelJob() {
        // currentItem.getJob()
        // cancelltems.add(thatjob)

    }

    /**
     * marks CurrentItem as complete 
     * and asks planning to re-plan 
     */
    public void itemComplete() {
        // currentItem = getItemfromList(next valid one);
        // plan.planRoute(robot);

    }

}
