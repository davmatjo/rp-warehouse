package rp.warehouse.nxt.localisation;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.util.Delay;
import rp.warehouse.nxt.motion.MotionController;

public class Ranges {
    private final MotionController motion;
    private final UltrasonicSensor sensor;

    public Ranges(MotionController motion) {
        this.motion = motion;
        this.sensor = new UltrasonicSensor(SensorPort.S3);
    }

    public float[] getRanges() {
        float[] ranges = new float[4];
        for (int i=0; i<4 ; i++) {
            float totalRange = 0f;
            for (int j=0; j < 10; j++) {
                Delay.msDelay(20);
                totalRange += sensor.getRange();
            }
            ranges[i] = totalRange / 10f;
            motion.rotate();
        }
        return ranges;
    }
}
