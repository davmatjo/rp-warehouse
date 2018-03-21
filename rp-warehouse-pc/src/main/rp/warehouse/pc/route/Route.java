package rp.warehouse.pc.route;

import org.apache.log4j.Logger;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.robot.Robot;

import java.util.*;

public class Route implements Iterable<Integer> {
    private static final Logger logger = Logger.getLogger(Route.class);
    private final LinkedList<Location> locations;
    private final Queue<Integer> route;

    public Route(Route route) {
        locations = new LinkedList<>(route.locations);
        this.route = new LinkedList<>(route.route);
    }

    Route(List<Node> nodes, boolean pickup, Node goalNode) {
        locations = new LinkedList<>();
        nodes.stream().limit(3).forEach((n) -> locations.add(n.toLocation()));

        route = new LinkedList<>();

        for (int i = 1; i < nodes.size() && i < 3; i ++) {

            if (nodes.get(i-1).getX() == nodes.get(i).getX() - 1) {
                route.add(Protocol.EAST);
            }

            if (nodes.get(i-1).getX() == nodes.get(i).getX() + 1) {
                route.add(Protocol.WEST);
            }

            if (nodes.get(i-1).getY() == nodes.get(i).getY() + 1) {
                route.add(Protocol.SOUTH);
            }

            if (nodes.get(i-1).getY() == nodes.get(i).getY() - 1) {
                route.add(Protocol.NORTH);
            }

        }
        if (nodes.size() < 3) {
            if (!nodes.get(nodes.size() - 1).equals(goalNode)) {
                route.add(Protocol.WAITING);
            } else {
                route.add(pickup ? Protocol.PICKUP : Protocol.DROPOFF);
            }
        }

        StringBuilder routeString = new StringBuilder();
        routeString.append("[ ");
        for (int i : route) {
            routeString.append(Robot.getDirectionString(i)).append(", ");
        }
        routeString.append("]");
        logger.debug(routeString.toString());
        logger.debug(locations.toString());
    }

    public int poll() throws NullPointerException {
        locations.poll();
        return route.poll();
    }

    public int size() {
        return route.size();
    }

    public Location getLocation(int i) {
        return locations.get(i);
    }

    public int peek() {
        return route.peek();
    }

    public boolean isEmpty() {
        return route.isEmpty();
    }

    @Override
    public String toString() {
        return locations.toString() + "\n" + route.toString();
    }

    @Override
    public Iterator<Integer> iterator() {
        return route.iterator();
    }
}
