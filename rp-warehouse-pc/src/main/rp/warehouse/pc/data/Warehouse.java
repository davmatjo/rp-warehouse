package rp.warehouse.pc.data;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LineMap;

public class Warehouse {

	public static GridMap build() {
		final float height = 2.44f;
		final float width = 3.67f;

		final float xInset = 0.17f, yInstet = 0.155f;
		final int gridWitdth = 12, gridHeight = 8;
		final float cellSize = 0.30f;

		final Line[] lineArray = new Line[] { new Line(0f, 0f, width, 0f), new Line(width, 0f, width, height),
				new Line(width, height, 0f, height), new Line(0f, height, 0f, 0f),
				new Line(0.31f, 0.29f, 0.31f, height - 0.61f), new Line(0.61f, 0.29f, 0.61f, height - 0.61f),
				new Line(0.31f, 0.29f, 0.61f, 0.29f), new Line(0.31f, height - 0.61f, 0.61f, height - 0.61f),
				new Line(1.24f, 0.28f, 1.24f, height - 0.62f), new Line(1.54f, 0.28f, 1.54f, height - 0.62f),
				new Line(1.24f, 0.28f, 1.54f, 0.28f), new Line(1.24f, height - 0.62f, 1.54f, height - 0.62f),
				new Line(2.14f, 0.29f, 2.14f, height - 0.61f), new Line(2.44f, 0.29f, 2.44f, height - 0.61f),
				new Line(2.14f, 0.29f, 2.44f, 0.29f), new Line(2.14f, height - 0.61f, 2.44f, height - 0.61f),
				new Line(3.08f, 0.30f, 3.08f, height - 0.60f), new Line(3.38f, 0.30f, 3.38f, height - 0.60f),
				new Line(3.08f, 0.30f, 3.38f, 0.30f), new Line(3.08f, height - 0.60f, 3.38f, height - 0.60f) };

		return new GridMap(gridWitdth, gridHeight, xInset, yInstet, cellSize,
				new LineMap(lineArray, new Rectangle(0, 0, width, height)));
	}
}
