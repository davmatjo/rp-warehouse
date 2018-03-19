package rp.warehouse.nxt.interaction;
import rp.warehouse.nxt.communication.*;
import lejos.nxt.*;

/**
 * @author Harry Pratlett
 * 
 * This class controls the interface on the robot and handles the button presses. The user can send an amount of items to pickup
 * by using the arrow buttons and confirm it using the middle button. This is all reflected by the display.
 *
 */

public class RobotInterfaceController {
	
	/* Two final variables are created to represent left and right in switch statements */
	
	private final static int LEFT = 10;
	private final static int RIGHT = 11;
	private final static int MIDDLE_SCREEN_WIDTH = 48;
	private final static int MIDDLE_SCREEN_HEIGHT = 32;
	private int jobAmount;
	private int toPickup;
	private boolean waiting;
	
	
	
	/*A communicator is created so that commands can be sent*/
	Communication communicator;
	
	/** @param theCommunicator This is the communicator that the class will use in order to send commands about how many items to pick up **/
	public RobotInterfaceController(Communication theCommunicator)	{
		waiting = true;
		jobAmount = 0;
		communicator = theCommunicator;
		main();
	}
	
	/* In the main method the button listeners are created to listen to the buttons presses and the
	 * command sent is changed depending on which one is pressed */
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
	/** @param buttonInput This is the button that has been pressed, represented in an integer form **/
	private void displayScreen(int buttonInput) {
		LCD.clearDisplay();
		LCD.refresh();
		switch (buttonInput)	{
			case Protocol.OK:
				LCD.drawString("Amount confirmed", MIDDLE_SCREEN_WIDTH, MIDDLE_SCREEN_HEIGHT);
				/* The number of jobs is sent*/
				if(waiting)	{
					communicator.sendCommand(Protocol.PICKUP);
					communicator.sendCommand(jobAmount);
					waiting = false;
					jobAmount = 0;
				}
				else	{
					LCD.drawString("Error: Robot not waiting for command", MIDDLE_SCREEN_WIDTH, MIDDLE_SCREEN_HEIGHT);
				}
				break;
			case LEFT:
				if (jobAmount > 0)	{
					LCD.drawString("Amount: " + (--jobAmount), MIDDLE_SCREEN_WIDTH, MIDDLE_SCREEN_HEIGHT);
				}
				else	{
					LCD.drawString("Error: Items cannot go below zero", MIDDLE_SCREEN_WIDTH, MIDDLE_SCREEN_HEIGHT);
				}
				break;
			case RIGHT:
				LCD.drawString("Amount: " + (++jobAmount), MIDDLE_SCREEN_WIDTH, MIDDLE_SCREEN_HEIGHT);
				break;
		}
	}

	/* This method is called at the end of the main and
	 loops continuously changing which switch case it is depending on which button has been pressed */
	
	/** @param command This is the command that is sent by the button press detected by the listeners, it is handled differently depending
	  on which one it is **/
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
	/* This method is called by other classes when the robot is ready to pickup an item, this prevents the robot from performing it whilst
	 * doing a job
	 */
	public void pickup(int amount) {
		toPickup = amount;
		if (toPickup < 0) {
			LCD.drawString("Confirm dropoff", MIDDLE_SCREEN_WIDTH,MIDDLE_SCREEN_HEIGHT);
			Button.waitForAnyPress();
			communicator.sendCommand(Protocol.PICKUP);
			communicator.sendCommand(0);
		} else {
			waiting = true;
		}
	}
<<<<<<< HEAD
=======
	//add dropoff on middle button
	//add value display
	//display when picking up etc


>>>>>>> branch 'RobotInterface' of git@git.cs.bham.ac.uk:dxj786/rp-warehouse.git
}
