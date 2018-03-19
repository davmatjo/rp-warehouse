package rp.warehouse.pc.management.panels.localisation;

import javax.swing.*;

public class LocaliserProgressPanel extends JPanel {
    JButton nextRobot;


    public LocaliserProgressPanel(Object nextRobotTrigger, String name) {
        JProgressBar progressBar = new JProgressBar();

        nextRobot = new JButton("Next robot");
        nextRobot.setEnabled(false);


    }

    public void finishedLocalising() {
        nextRobot.setEnabled(true);
    }
}
