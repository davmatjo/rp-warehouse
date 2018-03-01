package rp.warehouse.pc.data;

import java.util.ArrayList;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LineMap;

public class Warehouse {

	public static GridMap build() {
		float height = 2.44f;
		float width = 3.67f;

		float xInset = 0.17f, yInstet = 0.155f;
		int gridWitdth = 12, gridHeight = 8;
		float cellSize = 0.30f;

		// First ins 36 39 56 188

		ArrayList<Line> lines = new ArrayList<Line>();

		// these are the walls for the world outline
		lines.add(new Line(0f, 0f, width, 0f));
		lines.add(new Line(width, 0f, width, height));
		lines.add(new Line(width, height, 0f, height));
		lines.add(new Line(0f, height, 0f, 0f));

		lines.add(new Line(0.31f, 0.29f, 0.31f, height - 0.61f));
		lines.add(new Line(0.61f, 0.29f, 0.61f, height - 0.61f));
		lines.add(new Line(0.31f, 0.29f, 0.61f, 0.29f));
		lines.add(new Line(0.31f, height - 0.61f, 0.61f, height - 0.61f));

		lines.add(new Line(1.24f, 0.28f, 1.24f, height - 0.62f));
		lines.add(new Line(1.54f, 0.28f, 1.54f, height - 0.62f));
		lines.add(new Line(1.24f, 0.28f, 1.54f, 0.28f));
		lines.add(new Line(1.24f, height - 0.62f, 1.54f, height - 0.62f));

		lines.add(new Line(2.14f, 0.29f, 2.14f, height - 0.61f));
		lines.add(new Line(2.44f, 0.29f, 2.44f, height - 0.61f));
		lines.add(new Line(2.14f, 0.29f, 2.44f, 0.29f));
		lines.add(new Line(2.14f, height - 0.61f, 2.44f, height - 0.61f));

		lines.add(new Line(3.08f, 0.30f, 3.08f, height - 0.60f));
		lines.add(new Line(3.38f, 0.30f, 3.38f, height - 0.60f));
		lines.add(new Line(3.08f, 0.30f, 3.38f, 0.30f));
		lines.add(new Line(3.08f, height - 0.60f, 3.38f, height - 0.60f));

		Line[] lineArray = new Line[lines.size()];

		lines.toArray(lineArray);

		return new GridMap(gridWitdth, gridHeight, xInset, yInstet, cellSize,
				new LineMap(lineArray, new Rectangle(0, 0, width, height)));
	}
}
