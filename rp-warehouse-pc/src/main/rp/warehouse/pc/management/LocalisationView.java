package rp.warehouse.pc.management;

import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.panels.LocaliserMapVisualisation;

import javax.swing.*;
import java.awt.*;

public class LocalisationView extends JFrame {

    public LocalisationView(Localiser localiser) {
//        JPanel top = new JPanel();
//        top.setBackground(Color.WHITE);
        this.add(new LocaliserMapVisualisation(Warehouse.build(), Warehouse.build(), 200f, localiser));

        this.pack();
        this.setSize(800, 580);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
