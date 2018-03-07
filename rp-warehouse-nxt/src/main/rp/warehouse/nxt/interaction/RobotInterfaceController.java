package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.Communication;
import rp.warehouse.nxt.Protocol;
import rp.warehouse.nxt.RobotInterface;
import lejos.nxt.*;


public class RobotInterfaceController implements RobotInterface	{
	
	private final static int LEFT = 10;
	private final static int RIGHT = 11;
	private static int command;
	private static int jobAmount;
	RobotInterfaceController theInterface = new RobotInterfaceController();
	Communication communicator = new Communication();
	
	public RobotInterfaceController()	{
		command = 0;
		jobAmount = 0;
	}
	
	public void main (String args[])		{
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
		LCD.clearDisplay();
		switch (buttonInput)	{
			case Protocol.OK:
				LCD.drawString("Amount confirmed", LCD.SCREEN_WIDTH/2, LCD.SCREEN_HEIGHT/2);
				LCD.refresh();
				communicator.sendCommand(jobAmount);
			case LEFT:
				jobAmount--;
				LCD.drawInt(jobAmount, LCD.SCREEN_WIDTH/2, LCD.SCREEN_HEIGHT/2);
				LCD.refresh();
			case RIGHT:
				jobAmount++;
				LCD.drawInt(jobAmount, LCD.SCREEN_WIDTH/2, LCD.SCREEN_HEIGHT/2);
				LCD.refresh();
		}
	}

	@Override
	public void buttonPressed(int command) {
		while (true)	{
			switch(command)	{
				case Protocol.CANCEL:
						communicator.sendCommand(Protocol.CANCEL);
				case LEFT:
						displayScreen(command);
				case RIGHT:
						displayScreen(command);
				case Protocol.OK:
						communicator.sendCommand(Protocol.PICKUP);
						displayScreen(command);
			}	
		}
	}

	@Override
	public int pickup(int amount) {
		return amount;
	}


}
