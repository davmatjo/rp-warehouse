package rp.warehouse.nxt.interaction;

public interface RobotInterface	{
	
	boolean buttonPressed();
	void displayScreen();

	// Wait for user to pick up the correct number of items, then return true if they do and
    // false if they don't
	boolean pickup(int countToPickup);
}