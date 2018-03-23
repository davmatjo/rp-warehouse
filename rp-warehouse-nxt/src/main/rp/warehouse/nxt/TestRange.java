package rp.warehouse.nxt;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.OpticalDistanceSensor;
import rp.warehouse.nxt.communication.Protocol;
import rp.warehouse.nxt.interaction.Timeout;

/**
 * Class to view ranges read by robot - not part of main system
 * @deprecated - dxj786
 * @author dxj786
 */
public class TestRange {

    private static OpticalDistanceSensor sensor = new OpticalDistanceSensor(SensorPort.S2);

    public static void main(String[] args) {
        Button.ENTER.addButtonListener(new ButtonListener()	{
            @Override
            public void buttonPressed(Button b) {
                System.out.println("Range: " + sensor.getRange());
                System.out.println("Distance: " + sensor.getDistance());
            }
            @Override
            public void buttonReleased(Button b) {
            }
        });
        while(true) {}
    }
}
