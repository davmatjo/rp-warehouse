package rp.warehouse.pc.route;

import rp.warehouse.pc.data.Robot;

// No to be used anymore
@SuppressWarnings("unused")
public class RouteExecution implements RouteExecutionInterface {
    private static int numberOfRobots;
    Robot[] robots = null;
    
    public RouteExecution(Robot[] _robots) {
        robots=_robots;
        numberOfRobots=robots.length;
        run();
    }

    public void run () {   
        for (int i = 0; i < robots.length; i++) {// pass this
            robots[i]=new Robot(i+"", null);
        }
  
        // Planning plan = new Planning(robot);
        
        //plan.plan;

        RobotController[] robotControl = new RobotController[numberOfRobots];
        int i =0;
        for (Robot nxt : robots) {
            // iterates through Robots and makes them move
            robotControl[i]=new RobotController(nxt);//pass Planning
            i++;
            robotControl[i].start();
        }
        
        //Send them to W MI
         i =0;
        for (Robot nxt : robots) { 
        try {
            robotControl[i].join();// waits for the thread to end
            i++;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }
        

    }

}
