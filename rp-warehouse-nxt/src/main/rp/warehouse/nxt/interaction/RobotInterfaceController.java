package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.Communication;
import rp.warehouse.nxt.Protocol;
import rp.warehouse.nxt.RobotInterface;
import lejos.nxt.*;

public class RobotInterfaceController implements RobotInterface	{
	
	private final static int LEFT = 10;
	private final static int RIGHT = 11;
	private int command = 0;
	
	public void main (String args[])		{
		RobotInterfaceController theInterface = new RobotInterfaceController();
		Button.ENTER.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				command = Protocol.OK;
			}
			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.LEFT.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				command = LEFT;
			}
			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				command = RIGHT;
			}
			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.ESCAPE.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				command = Protocol.CANCEL;
			}
			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public void displayScreen(int buttonInput) {
	}

	@Override
	public int buttonPressed(int command) {
		while (true)	{
			switch(command)	{
				case 1: command = Protocol.CANCEL;
						return Protocol.CANCEL;
				case 2: command = LEFT;
						displayScreen(command);
				case 3: command = RIGHT;
						displayScreen(command);
				case 4: command = Protocol.OK;
						displayScreen(command);
			}	
		}
	}

	public int pickup() {
		// TODO Auto-generated method stub
		return 0;
	}


}
