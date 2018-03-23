package rp.warehouse.pc.management.panels.localisation;

import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.providers.localisation.LocalisationProgressProvider;

import javax.swing.*;
import java.awt.*;

public class LocaliserProgressPanel extends JPanel {
    private JButton nextRobot;

    /**
     * Creates a panel that contains a progress bar, robot name, and button to move to the next robot
     * @param localiser localiser to model
     * @param name name of robot being localised
     */
    public LocaliserProgressPanel(Localiser localiser, String name) {
        this.setLayout(new BorderLayout());

        // Create progress bar and ProgressProvider to update it
        JProgressBar progressBar = new JProgressBar(0, 100);
        LocalisationProgressProvider provider = new LocalisationProgressProvider(progressBar);
        localiser.addListener(provider);

        nextRobot = new JButton("Next robot");
        nextRobot.setEnabled(false);

        // Allows anything waiting on the gui to continue
        nextRobot.addActionListener((e) -> {
            synchronized (localiser) {
                localiser.notify();
            }
        });

        JLabel robotName = new JLabel(name);

        this.add(robotName, BorderLayout.WEST);
        this.add(progressBar, BorderLayout.CENTER);
        this.add(nextRobot, BorderLayout.EAST);
    }

    /**
     * Called when localisation is finished to activate the button
     */
    public void finishedLocalising() {
        nextRobot.setEnabled(true);
    }
}
