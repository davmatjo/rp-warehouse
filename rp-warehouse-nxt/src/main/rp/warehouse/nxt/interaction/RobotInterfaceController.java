package rp.warehouse.nxt.interaction;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import rp.warehouse.nxt.communication.Communication;
import rp.warehouse.nxt.communication.Protocol;

/**
 * @author Harry Pratlett This class controls the interface on the robot and
 *         handles the button presses. The user can send an amount of items to
 *         pickup by using the arrow buttons and confirm it using the middle
 *         button. This is all reflected by the display.
 */

public class RobotInterfaceController {

	/*
	 * Two final variables are created to represent left and right in switch
	 * statements
	 */

	private final static int LEFT = 10;
	private final static int RIGHT = 11;
	private final static int TEXT_WIDTH = 0;
	private final static int TEXT_HEIGHT = 4;
	private int jobAmount;
	private int toPickup;
	private boolean waiting;
	private Timeout timer;

	/* A communicator is created so that commands can be sent */
	Communication communicator;

	/**
	 * @param theCommunicator
	 *            This is the communicator that the class will use in order to send
	 *            commands about how many items to pick up
	 **/
	public RobotInterfaceController(Communication theCommunicator) {
		waiting = true;
		jobAmount = 0;
		communicator = theCommunicator;
		main();
	}

	/**
	 * In the main method the button listeners are created to listen to the buttons
	 * presses and the command sent is changed depending on which one is pressed
	 */
	private void main() {
		timer = new Timeout(communicator);
		Button.ENTER.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(Protocol.OK);
			}

			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(LEFT);
			}

			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				buttonEvent(RIGHT);
			}

			@Override
			public void buttonReleased(Button b) {
			}
		});
		Button.ESCAPE.addButtonListener(new ButtonListener() {
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

	/**
	 * This method changes the display based on what the user presses
	 * 
	 * @param buttonInput
	 *            This is the button that has been pressed, represented in an
	 *            integer form
	 **/
	private void displayScreen(int buttonInput) {
		LCD.clear();
		LCD.refresh();
		switch (buttonInput) {
		case Protocol.OK:
			LCD.drawString("Amount confirmed", TEXT_WIDTH, TEXT_HEIGHT);
			/* The number of jobs is sent */
			if (waiting) {
				communicator.sendCommand(Protocol.PICKUP);
				communicator.sendCommand(jobAmount);
				waiting = false;
				jobAmount = 0;
			} else {
				LCD.drawString("Error: Robot not waiting for command", TEXT_WIDTH, TEXT_HEIGHT);
			}
			break;
		case LEFT:
			if (jobAmount > 0) {
				LCD.drawString("Pickup amount: " + toPickup, TEXT_WIDTH, TEXT_HEIGHT);
				LCD.drawString("Amount: " + (--jobAmount), TEXT_WIDTH, TEXT_HEIGHT + 1);
			} else {
				LCD.drawString("Error: Items cannot go below zero", TEXT_WIDTH, TEXT_HEIGHT);
			}
			break;
		case RIGHT:
			LCD.drawString("Pickup amount: " + toPickup, TEXT_WIDTH, TEXT_HEIGHT);
			LCD.drawString("Amount: " + (++jobAmount), TEXT_WIDTH, TEXT_HEIGHT + 1);
			break;
		}
	}

	/**
	 * This method is called at the end of the main and loops continuously changing
	 * which switch case it is depending on which button has been pressed
	 * 
	 * @param command
	 *            This is the command that is sent by the button press detected by
	 *            the listeners, it is handled differently depending on which one it
	 *            is
	 **/
	private void buttonEvent(int command) {
		switch (command) {
		case Protocol.CANCEL:
			timer.interrupt();
			communicator.sendCommand(Protocol.CANCEL);
			break;
		case LEFT:
			timer.interrupt();
			timer = new Timeout(communicator);
			timer.start();
			displayScreen(command);
			break;
		case RIGHT:
			timer.interrupt();
			timer = new Timeout(communicator);
			timer.start();
			displayScreen(command);
			break;
		case Protocol.OK:
			timer.interrupt();
			displayScreen(command);
			break;
		}
	}

	/**
	 * This method is called by other classes when the robot is ready to pickup an
	 * item, this prevents the robot from performing it whilst doing a job
	 */
	public void pickup(int amount) {
		toPickup = amount;
		LCD.clear();
		LCD.refresh();
		if (toPickup < 1) {
			LCD.drawString("Confirm dropoff", TEXT_WIDTH, TEXT_HEIGHT);
			Button.waitForAnyPress();
			communicator.sendCommand(Protocol.PICKUP);
			communicator.sendCommand(0);
		} else {
			waiting = true;
			LCD.drawString("Pickup amount: " + toPickup, TEXT_WIDTH, TEXT_HEIGHT);
			timer = new Timeout(communicator);
			timer.start();
		}
	}
}
