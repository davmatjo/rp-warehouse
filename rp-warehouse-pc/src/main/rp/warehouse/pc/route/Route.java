package rp.warehouse.pc.route;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Route {
    private final LinkedList<Location> locations;
    private final Queue<Integer> route;

    public Route(Route route) {
        locations = new LinkedList<>(route.locations);
        this.route = new LinkedList<>(route.route);
    }

    Route(List<Node> nodes) {
        locations = new LinkedList<>();
        nodes.stream().limit(4).forEach((n) -> locations.add(n.toLocation()));

        route = new LinkedList<>();

        for (int i = 1; i < nodes.size() && i < 4; i ++) {

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
    }

    public int poll() throws NullPointerException {
        locations.poll();
        return route.poll();
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

}
