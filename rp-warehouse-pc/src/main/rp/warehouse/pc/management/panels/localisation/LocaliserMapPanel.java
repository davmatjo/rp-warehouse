package rp.warehouse.pc.management.panels.localisation;

import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.localisation.implementation.Localiser;

import javax.swing.*;
import java.awt.*;

/**
 * Contains a LocaliserMapVisualisation
 * @author dxj786
 * @see LocaliserMapVisualisation
 */
public class LocaliserMapPanel extends JPanel {

    /**
     * Creates a panel that contains a LocaliserMapVisualisation
     * @param localiser localiser being visualised
     */
    public LocaliserMapPanel(Localiser localiser) {
        LocaliserMapVisualisation mapVisualisation = new LocaliserMapVisualisation(Warehouse.build(), Warehouse.build(), 200f);

        this.add(mapVisualisation);
        mapVisualisation.setPreferredSize(new Dimension(800, 580));

        localiser.addListener(mapVisualisation);

        // Makes the background unified
        this.setBackground(Color.WHITE);
    }
}
