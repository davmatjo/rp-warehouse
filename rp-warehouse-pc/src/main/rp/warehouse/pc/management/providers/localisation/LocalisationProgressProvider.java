package rp.warehouse.pc.management.providers.localisation;

import rp.warehouse.pc.data.robot.utils.RobotLocation;

import javax.swing.*;
import java.util.List;
import java.util.stream.Stream;

public class LocalisationProgressProvider implements LocalisationListener {
    private JProgressBar progressBar;
    private int initialReading = 0;

    /**
     * Creates a LocalisationProgressProvider that sets a given JProgressBar
     * @param progressBar progressBar to set
     */
    public LocalisationProgressProvider(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * When new possible points are received, sets the progress bar level based on number of possible locations
     * remaining compared to initial possible locations
     * @param points possible points
     */
    @Override
    public void newPoints(List<Stream<RobotLocation>> points) {
        int sum = totalPoints(points);
        if (initialReading == 0) {
            initialReading = sum;
            progressBar.setMaximum(initialReading - 1);
        }
        progressBar.setValue(Math.abs(sum - initialReading));
    }

    /**
     * sums number of possible points
     * @param points possible points
     * @return number of possible points
     */
    private int totalPoints(List<Stream<RobotLocation>> points) {
        int sum = 0;
        for (Stream<RobotLocation> point : points) {
            sum += point.count();
        }
        return sum;
    }
}
