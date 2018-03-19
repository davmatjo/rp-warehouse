package rp.warehouse.pc.management;

import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.panels.localisation.LocaliserMapPanel;
import rp.warehouse.pc.management.panels.localisation.LocaliserProgressPanel;
import rp.warehouse.pc.management.panels.main.InfoPanel;
import rp.warehouse.pc.management.panels.main.WarehouseMapPanel;

import javax.swing.*;
import java.awt.*;

public class LocalisationView extends JFrame {
    private LocaliserProgressPanel progressPanel;

    public LocalisationView(Localiser localiser, String robotName, Object nextRobotTrigger) {
        super("Localising");

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add(new LocaliserMapPanel(localiser), BorderLayout.CENTER);

        this.progressPanel = new LocaliserProgressPanel(localiser, nextRobotTrigger, robotName);
        top.add(progressPanel, BorderLayout.SOUTH);

        this.add(top);
        this.setSize(top.getMinimumSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void finishedLocalising() {
        progressPanel.finishedLocalising();
    }
}
