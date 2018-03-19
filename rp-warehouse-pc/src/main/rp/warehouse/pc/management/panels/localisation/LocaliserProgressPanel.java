package rp.warehouse.pc.management.panels.localisation;

import rp.warehouse.pc.data.Warehouse;

import javax.swing.*;
import java.awt.*;

public class LocaliserProgressPanel extends JPanel {
    JButton nextRobot;


    public LocaliserProgressPanel(Object nextRobotTrigger, String name) {
        this.setLayout(new BorderLayout());

        JProgressBar progressBar = new JProgressBar(0, 100);

        nextRobot = new JButton("Next robot");
        nextRobot.setEnabled(false);

        nextRobot.addActionListener((e) -> {
            synchronized (nextRobotTrigger) {
                nextRobotTrigger.notify();
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
