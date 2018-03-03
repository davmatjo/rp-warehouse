package rp.warehouse.pc.route;

import rp.warehouse.pc.data.Robot;

/**
 *Not to be used anymore!!!!!!!
 * @author roman
 *
 */
@SuppressWarnings("unused")
public class RobotController extends Thread {

    Robot robot = null;

    public RobotController(Robot _robot) {
        robot = _robot;
    }

    @Override
    public void run() {
        String answer = null;
        while (true) {


          // what value means no more items
            if (robot.getCurrentItem()==-1) {// If nothing left in the currentRoute
                switch (answer) {
                case "WAITING":

                    break;
                case "FAIL":
                    System.exit(1);
                    break;
                case "OK":

                    break;

                default:
                    break;
                }
            }
            robot.move();
            //answer= robot.getResponse(); // blocking
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
