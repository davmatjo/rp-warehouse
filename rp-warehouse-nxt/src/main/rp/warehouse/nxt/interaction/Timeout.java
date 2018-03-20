package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.communication.Communication;
import rp.warehouse.nxt.communication.Protocol;

public class Timeout extends Thread	{
	
	Communication communicator;
	
	public Timeout(Communication theCommunicator)	{
		communicator = theCommunicator;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(25000);
			communicator.sendCommand(Protocol.CANCEL);
		} 	catch (InterruptedException e) {
			
		}
		
		
	}

}
