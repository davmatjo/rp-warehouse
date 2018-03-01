package rp.warehouse.pc;

public class RouteExecution implements RouteExecutionInterface {
    private static int numberOfRobots = 1;

    public static void main(String[] args) {
        // JobInput job = new JobInput("filename");
        // Reads everything into some kind of class
        // should it return that class to then pass it to selection
        
        
        // JobSellection select = new JobSelefction();
        // accesses that class
        
        
        // Array of robot IDs
        Robot[] robots = new Robot[numberOfRobots];
        
        for (int i = 0; i < robots.length; i++) {// pass this
            robots[i]=new Robot(i+"");
        }
        

        // Assignment jobs = new Assignment(robots); // type Robot[]
        // should i pass map class as well
        
        // Planning plan = new Planning(robot, jobs);
        
        //plan.plan;

        for (Robot nxt : robots) {
            // iterates through Robots and makes them move
            RobotController robot1 = new RobotController(nxt);//pass Planning
            robot1.start();
            nxt.move(0);
        }
        

        
        
        
        /*
        try {
            robot1.join();// waits for the thread to end
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        // pick up (number)
        
        // cancel
        

    }

}
