package rp.warehouse.pc.localisation;

import javax.swing.JFrame;

import rp.robotics.mapping.GridMap;
import rp.robotics.visualisation.GridMapVisualisation;
import rp.robotics.visualisation.KillMeNow;
import rp.robotics.visualisation.MapVisualisationComponent;
import rp.warehouse.pc.data.Warehouse;

public class VisualiseWarehouse {

	public static void main(String[] args) {
		// Grid map configuration
		GridMap gridMap = Warehouse.build();
		GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, gridMap, 150f);
		displayVisualisation(mapVis);
	}

	public static JFrame displayVisualisation(MapVisualisationComponent viz) {
		// Create a frame to contain the viewer
		JFrame frame = new JFrame("Simulation Viewer");

		// Add visualisation to frame
		frame.add(viz);
		frame.addWindowListener(new KillMeNow());

		frame.pack();
		frame.setSize(viz.getMinimumSize());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		return frame;
	}

}
