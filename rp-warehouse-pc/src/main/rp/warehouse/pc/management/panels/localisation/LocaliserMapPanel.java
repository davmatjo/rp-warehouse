package rp.warehouse.pc.management.panels.localisation;

import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.localisation.implementation.Localiser;

import javax.swing.*;
import java.awt.*;

public class LocaliserMapPanel extends JPanel {

    public LocaliserMapPanel(Localiser localiser) {
        LocaliserMapVisualisation mapVisualisation = new LocaliserMapVisualisation(Warehouse.build(), Warehouse.build(), 200f, localiser);

        this.add(mapVisualisation);
        mapVisualisation.setPreferredSize(new Dimension(800, 580));

        localiser.addListener(mapVisualisation);

        this.setBackground(Color.WHITE);
    }
}
