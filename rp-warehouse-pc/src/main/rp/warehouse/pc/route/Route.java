package rp.warehouse.pc.route;

import org.apache.log4j.Logger;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.robot.Robot;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * This clas exists to help route planning, by storing the locations along with the instructions, we can save
 * computation when planning
 * @author dxj786
 */
public class Route implements Iterable<Integer> {
    private static final Logger logger = Logger.getLogger(Route.class);
    private final LinkedList<Location> locations;
    private final Queue<Integer> route;

    /**
     * Creates a route from a given route
     * @param route route to copy
     */
    public Route(Route route) {
        locations = new LinkedList<>(route.locations);
        this.route = new LinkedList<>(route.route);
    }

    /**
     * Creates a route from a list of nodes
     * @param nodes nodes to create the route
     * @param pickup whether the route is a pickup or a dropoff
     * @param goalNode the intended goal node
     */
    Route(List<Node> nodes, boolean pickup, Node goalNode) {
        // Add locations into linkedlist, limiting to 3 steps
        locations = new LinkedList<>();
        nodes.stream().limit(3).forEach((n) -> locations.add(n.toLocation()));

        route = new LinkedList<>();

        // Create list of directions from the nodes
        for (int i = 1; i < nodes.size() && i < 3; i++) {
            Node prev = nodes.get(i - 1);
            Node curr = nodes.get(i);

            if (prev.getX() == curr.getX() - 1) {
                route.add(Protocol.EAST);
            }

            if (prev.getX() == curr.getX() + 1) {
                route.add(Protocol.WEST);
            }

            if (prev.getY() == curr.getY() + 1) {
                route.add(Protocol.SOUTH);
            }

            if (prev.getY() == curr.getY() - 1) {
                route.add(Protocol.NORTH);
            }

        }

        /* If the route is small enough, check that we have the dropoff, if we did, add pickup or dropoff to the route,
           if not, make the robot wait as we will be next to a location currently occupied by a different robot
         */
        if (nodes.size() < 3) {
            if (!nodes.get(nodes.size() - 1).equals(goalNode)) {
                route.add(Protocol.WAITING);
            } else {
                route.add(pickup ? Protocol.PICKUP : Protocol.DROPOFF);
            }
        }

        // Logging
        StringBuilder routeString = new StringBuilder();
        routeString.append("[ ");
        for (int i : route) {
            routeString.append(Robot.getDirectionString(i)).append(", ");
        }
        routeString.append("]");
        logger.debug(routeString.toString());
        logger.debug(locations.toString());
    }

    /**
     * get the first instruction, and remove it
     * @return the first instruction in the route
     * @throws NullPointerException No more elements
     * @see LinkedList#poll()
     */
    public int poll() throws NullPointerException {
        locations.poll();
        return route.poll();
    }

    /**
     * Number of steps in the route
     * @return size of the route
     */
    public int size() {
        return route.size();
    }

    /**
     * Gets the location this plan expects the robot to be at a certain arbitrary 'tick' of time
     * @param i number of ticks into the plan
     * @return Planned location
     * @see LinkedList#get(int)
     */
    public Location getLocation(int i) {
        return locations.get(i);
    }

    /**
     * get the first instruction, but don't remove it
     * @return First instruction of the route
     * @see LinkedList#peek()
     */
    public int peek() {
        return route.peek();
    }

    /**
     * Checks if the route has any more steps
     * @return true if there are no more steps
     */
    public boolean isEmpty() {
        return route.isEmpty();
    }

    @Override
    public String toString() {
        return locations.toString() + "\n" + route.toString();
    }

    /**
     * Iterator over the route steps
     * @return iterator over the directions
     * @see LinkedList#iterator()
     */
    @Override
    public Iterator<Integer> iterator() {
        return route.iterator();
    }
}
