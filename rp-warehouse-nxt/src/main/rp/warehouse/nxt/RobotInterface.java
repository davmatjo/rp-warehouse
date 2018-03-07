package rp.warehouse.nxt;

public interface RobotInterface	{
	
	int buttonPressed(int command);
	void displayScreen(int buttonInput);
	int pickup();
}