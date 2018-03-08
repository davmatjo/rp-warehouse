package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.communication.*;
import rp.warehouse.nxt.RobotInterface;
import lejos.nxt.*;


public class RobotInterfaceController {
	
	/* Two final variables are created to represent left and right in switch statements */
	
	private final static int LEFT = 10;
	private final static int RIGHT = 11;
	private int jobAmount;
	private boolean waiting;
	
	
	/*An interface is created and the communicator to send it is created */
	
	Communication communicator;
	
	public RobotInterfaceController(Communication theCommunicator)	{
		waiting = true;
		jobAmount = 0;
		communicator = theCommunicator;
		main();
	}
	
	/* In the main method the button listeners are created to listen to the buttons presses and command is changed depending on which one is pressed */
	
	private void main() {
		Button.ENTER.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(Protocol.OK);
			}
			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.LEFT.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(LEFT);
			}
			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(RIGHT);
			}
			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.ESCAPE.addButtonListener(new ButtonListener()	{
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(Protocol.CANCEL);
			}
			@Override
			public void buttonReleased(Button b) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/*This method changes the display based on what the user presses */
	private void displayScreen(int buttonInput) {
		LCD.clearDisplay();
		LCD.refresh();
		switch (buttonInput)	{
			case Protocol.OK:
<<<<<<< Upstream, based on master
				LCD.drawString("Amount confirmed", 0, 0);
=======
				LCD.drawString("Amount confirmed", LCD.SCREEN_WIDTH/2, LCD.SCREEN_HEIGHT/2);
>>>>>>> 379a48a rebasing
				/* The number of jobs is sent*/
				if(waiting)	{
					communicator.sendCommand(Protocol.PICKUP);
					communicator.sendCommand(jobAmount);
					waiting = false;
					jobAmount = 0;
				}
				else	{
					LCD.drawString("Error: Robot not waiting for command", 0, 0);
				}
				break;
			case LEFT:
				LCD.drawString("Amount: " + (--jobAmount), 0, 0);
				break;
			case RIGHT:
				LCD.drawString("Amount: " + (++jobAmount), 0, 0);
				break;
		}
	}

	/*This method is called at the end of the main and loops continuously changing which switch case it is depending on which button has been pressed */
	private void buttonEvent(int command) {
		switch(command)	{
			case Protocol.CANCEL:
					communicator.sendCommand(Protocol.CANCEL);
					break;
			case LEFT:
					displayScreen(command);
					break;
			case RIGHT:
					displayScreen(command);
					break;
			case Protocol.OK:
					displayScreen(command);
					break;
		}
	}

	public void pickup() {
		waiting = true;
	}


}
