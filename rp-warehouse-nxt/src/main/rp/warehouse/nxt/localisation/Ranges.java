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
		for (int i = 0; i < 4; i++) {
			float[] tempRanges = new float[20];
			for (int j = 0; j < 20; j++) {
				Delay.msDelay(20);
				tempRanges[j] = sensor.getRange();
			}
			ranges[i] = mode(tempRanges);
			motion.rotate();
		}
		return ranges;
	}

	private static float mode(float a[]) {
		float maxValue = 0;
		int maxCount = 0;
		for (int i = 0; i < a.length; ++i) {
			int count = 0;
			for (int j = 0; j < a.length; ++j) {
				if (a[j] == a[i])
					++count;
			}
			if (count > maxCount) {
				maxCount = count;
				maxValue = a[i];
			}
		}
		return maxValue;
	}
}
