package rp.warehouse.pc.management.panels.localisation;

import rp.warehouse.pc.localisation.implementation.Localiser;
import rp.warehouse.pc.management.providers.localisation.LocalisationProgressProvider;

import javax.swing.*;
import java.awt.*;

public class LocaliserProgressPanel extends JPanel {
    private JButton nextRobot;


    public LocaliserProgressPanel(Localiser localiser, String name) {
        this.setLayout(new BorderLayout());

        JProgressBar progressBar = new JProgressBar(0, 100);
        LocalisationProgressProvider provider = new LocalisationProgressProvider(progressBar);
        localiser.addListener(provider);

        nextRobot = new JButton("Next robot");
        nextRobot.setEnabled(false);

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

    public void finishedLocalising() {
        nextRobot.setEnabled(true);
    }
}
