package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.communication.*;
import rp.warehouse.nxt.motion.Movement;
import rp.warehouse.nxt.RobotInterface;
import lejos.nxt.*;


public class RobotInterfaceController implements RobotInterface	{
	
	/* Two final variables are created to represent left and right in switch statements */
	
	private final static int LEFT = 10;
	private final static int RIGHT = 11;
	private static int command;
	private static int jobAmount;
	private boolean waiting;
	
	
	/*An interface is created and the communicator to send it is created */
	
	Communication communicator;
	
	public RobotInterfaceController(Communication theCommunicator)	{
		waiting = true;
		command = 0;
		jobAmount = 0;
		communicator = theCommunicator;
	}
	
	/* In the main method the button listeners are created to listen to the buttons presses and command is changed depending on which one is pressed */
	
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
		/* the method is called to start it with the value -1 so that it does not trigger any of the switch cases */
		this.buttonPressed(-1);
		
	}

	/*This method changes the display based on what the user presses */
	@Override
	public void displayScreen(int buttonInput) {
		LCD.clearDisplay();
		switch (buttonInput)	{
			case Protocol.OK:
				LCD.drawString("Amount confirmed", LCD.SCREEN_WIDTH/2, LCD.SCREEN_HEIGHT/2);
				LCD.refresh();
				/* The number of jobs is sent*/
				if(waiting)	{
					communicator.sendCommand(jobAmount);
					waiting = false;
				}
				else	{
					LCD.drawString("Error: Robot not waiting for command", LCD.SCREEN_WIDTH/2, LCD.SCREEN_HEIGHT/2);
					LCD.refresh();
				}
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

	/*This method is called at the end of the main and loops continuously changing which switch case it is depending on which button has been pressed */
	@Override
	public void buttonPressed(int command) {
		while (true)	{
			switch(command)	{
				case Protocol.CANCEL:
						communicator.sendCommand(Protocol.CANCEL);
						command = -1;
				case LEFT:
						displayScreen(command);
						command = -1;
				case RIGHT:
						displayScreen(command);
						command = -1;
				case Protocol.OK:
						communicator.sendCommand(Protocol.PICKUP);
						displayScreen(command);
						command = -1;
			}	
		}
	}

	@Override
	public void pickup() {
		
	}


}
