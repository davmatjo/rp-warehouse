package rp.warehouse.pc.data;

import java.util.ArrayList;

import lejos.geom.Line;
import lejos.geom.Rectangle;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LineMap;

public class Warehouse {

	private static final ArrayList<Location> blockedLocations = new ArrayList<Location>();
	private static final float height = 2.48f, width = 3.68f, xInset = 0.14f, yInset = 0.15f, cellSize = 0.30f;
	private static final int gridWidth = 12, gridHeight = 8;

	private static final GridMap map = new GridMap(gridWidth, gridHeight, xInset, yInset, cellSize,
			new LineMap(
					new Line[] { new Line(0f, 0f, width, 0f), new Line(width, 0f, width, height),
							new Line(width, height, 0f, height), new Line(0f, height, 0f, 0f),
							new Line(0.28f, 0.31f, 0.28f, 1.83f), new Line(0.58f, 0.31f, 0.58f, 1.83f),
							new Line(0.28f, 0.31f, 0.58f, 0.31f), new Line(0.28f, 1.83f, 0.58f, 1.83f),
							new Line(1.20f, 0.28f, 1.20f, 1.85f), new Line(1.50f, 0.28f, 1.50f, 1.85f),
							new Line(1.20f, 0.28f, 1.50f, 0.28f), new Line(1.20f, 1.85f, 1.50f, 1.85f),
							new Line(2.13f, 0.29f, 2.13f, 1.84f), new Line(2.44f, 0.29f, 2.44f, 1.84f),
							new Line(2.13f, 0.29f, 2.44f, 0.29f), new Line(2.13f, 1.84f, 2.44f, 1.84f),
							new Line(3.05f, 0.30f, 3.05f, 1.83f), new Line(3.35f, 0.30f, 3.35f, 1.83f),
							new Line(3.05f, 0.30f, 3.35f, 0.30f), new Line(3.05f, 1.83f, 3.35f, 1.83f) },
					new Rectangle(0, 0, width, height)));

	public static GridMap build() {
		return map;
	}

	public static final ArrayList<Location> getBlockedLocations() {
		return blockedLocations;
	}

}