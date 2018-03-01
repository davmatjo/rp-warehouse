package rp.warehouse.pc.route;

import rp.warehouse.pc.data.Robot;

/**
 * 
 * @author roman
 *
 */
public class RobotController extends Thread {
    
    Robot nxtBrick = null;
    public RobotController(Robot _nxtBrick) {
        nxtBrick=_nxtBrick;
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        //move();
        
        //answer = getResponse();  // blocking
        
        //if (!answer){plan.replan}
        //if(done){plan.more(Robot)}
        
        //
        
        
    }
    /**
     * When called cancels Job of the current item
     */
    public void cancelJob() {
        
    }
    /**
     * 
     */
    public void itemComplete() {
        
    }

}
