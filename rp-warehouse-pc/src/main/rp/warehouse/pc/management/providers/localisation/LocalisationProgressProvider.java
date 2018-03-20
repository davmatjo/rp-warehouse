package rp.warehouse.pc.management.providers.localisation;

import rp.warehouse.pc.data.robot.utils.RobotLocation;

import javax.swing.*;
import java.util.List;
import java.util.stream.Stream;

public class LocalisationProgressProvider implements LocalisationListener {
    private JProgressBar progressBar;
    private int initialReading = 0;

    public LocalisationProgressProvider(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void newPoints(List<Stream<RobotLocation>> points) {
        int sum = totalPoints(points);
        if (initialReading == 0) {
            initialReading = sum;
            progressBar.setMaximum(initialReading - 1);
        }
        progressBar.setValue(Math.abs(sum - initialReading));
    }

    private int totalPoints(List<Stream<RobotLocation>> points) {
        int sum = 0;
        for (Stream<RobotLocation> point : points) {
            sum += point.count();
        }
        return sum;
    }
}
