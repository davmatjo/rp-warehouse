package rp.warehouse.nxt.interaction;

public interface RobotInterface	{
	
	boolean buttonPressed();
	void displayScreen();

	// Wait for user to pick up a number of items, then return it
	void pickup();
}