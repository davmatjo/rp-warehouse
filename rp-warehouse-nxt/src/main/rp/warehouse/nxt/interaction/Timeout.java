package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.communication.Communication;
import rp.warehouse.nxt.communication.Protocol;

/**
 * @author Harry
 * this class counts 25 seconds and cancels a job unless interrupted
 */
public class Timeout extends Thread	{
	
	private Communication communicator;
	
	Timeout(Communication theCommunicator)	{
		communicator = theCommunicator;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(25000);
			communicator.sendCommand(Protocol.CANCEL);
		} 	catch (InterruptedException ignored) {

		}
		
		
	}

}
