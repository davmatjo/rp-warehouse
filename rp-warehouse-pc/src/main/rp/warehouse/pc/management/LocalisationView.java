package rp.warehouse.pc.management;

import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.panels.localisation.LocaliserMapPanel;
import rp.warehouse.pc.management.panels.localisation.LocaliserProgressPanel;

import javax.swing.*;
import java.awt.*;

public class LocalisationView extends JFrame {
    private LocaliserProgressPanel progressPanel;

    /**
     * Creates a frame to visualise a given localiser for a robot with a given name
     * @param localiser localiser to visualise
     * @param robotName name of the robot
     */
    public LocalisationView(Localiser localiser, String robotName) {
        super("Localising");

        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add(new LocaliserMapPanel(localiser), BorderLayout.CENTER);

        this.progressPanel = new LocaliserProgressPanel(localiser, robotName);
        top.add(progressPanel, BorderLayout.SOUTH);

        this.add(top);
        this.setSize(top.getMinimumSize());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    /**
     * Informs the gui that localisation is complete
     */
    public void finishedLocalising() {
        progressPanel.finishedLocalising();
    }
}
