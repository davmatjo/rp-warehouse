package rp.warehouse.pc.route;

import org.apache.log4j.Logger;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.data.robot.Robot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Node {
    private static final Logger logger = Logger.getLogger(Node.class);
    private static List<Robot> robotList;
    private final Robot robot;
    private int x;
    private int y;
    private int h_cost;
    private int g_cost;
    private int f_cost;
    private Node parent;

    static void setRobots(List<Robot> robots) {
        robotList = robots;
    }

    Node(int x, int y, Robot robot) {
        this.robot = robot;
        this.x = x;
        this.y = y;
        //parent is null until set; original starting node has null parent.
        parent = null;

    }

    private String printCoordinates(Node node) {
        if (node.getParent() == null) {
            return node.toString();
        } else return printCoordinates(node.getParent()) + ", " + node.toString();
    }

    /**
     * Plans a route from a given start node to a given end node, avoiding blocked locations.
     * @param start start node
     * @param end end node
     * @param pickup whether this plan is for pickup or dropoff
     * @return A route that goes from the start to end locations
     */
    Route plan(Node start, Node end, boolean pickup) {

        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();

        Node endNodeWithParentPointersSorted = start.navigate(start, end, openList, closedList);

        ArrayList<Node> nodes = returnNodes(endNodeWithParentPointersSorted);

        return new Route(nodes, pickup, end);
    }

    /**
     * Returns an array list of all the nodes in the path in the correct order
     *
     * @param node
     * @return
     */
    private ArrayList<Node> returnNodes(Node node) {
        ArrayList<Node> nodes = new ArrayList<Node>();

        nodes = returnNodesBackwards(node, nodes);

        Collections.reverse(nodes);

        return nodes;
    }

    /**
     * Returns an array list of all the nodes in the path but in reverse order
     *
     * @param node
     * @param nodes
     * @return
     */
    private ArrayList<Node> returnNodesBackwards(Node node, ArrayList<Node> nodes) {

        if (node.getParent() == null) {
            nodes.add(node);
            return nodes;
        }

        nodes.add(node);

        return returnNodesBackwards(node.getParent(), nodes);
    }

    /**
     * Uses the A* algorithm to find the shortest route from the current node to the goal node
     * @param currentNode current node
     * @param goalNode goal node
     * @param openList list of nodes open for consideration
     * @param closedList list of closed nodes
     * @return the final node, which stores all previous nodes in the route
     */
    private Node navigate(Node currentNode, Node goalNode, List<Node> openList, List<Node> closedList) {

        openList.remove(currentNode);
        closedList.add(currentNode);

        if (currentNode.getX() == goalNode.getX() && currentNode.getY() == goalNode.getY()) {
            return currentNode;
        }

        List<Node> possibleNodes = new ArrayList<>(4);
        possibleNodes.add(new Node(currentNode.getX(), currentNode.getY() + 1, robot));
        possibleNodes.add(new Node(currentNode.getX() + 1, currentNode.getY(), robot));
        possibleNodes.add(new Node(currentNode.getX(), currentNode.getY() - 1, robot));
        possibleNodes.add(new Node(currentNode.getX() - 1, currentNode.getY(), robot));

        // Add the possible nodes to the open list, if the node is the goal node but blocked, we just return the route
        for (Node node : possibleNodes) {
            if (addToOpenList(node, currentNode, goalNode, openList, closedList)) {
                return currentNode;
            }
        }

        int lowestF_Value = 10000000;
        Node lowestF_ValueNode = null;

        for (Node openNode : openList) {
            if (openNode.getF_Cost() < lowestF_Value) {
                lowestF_ValueNode = openNode;
                lowestF_Value = openNode.getF_Cost();
            }
        }

        //in case more than one node shares the same f value, we want to visit the one with the lowest heuristic (h) value
        List<Node> nodesWithSameLowestF_Value = new ArrayList<Node>();

        for (Node openNode : openList) {
            if (openNode.getF_Cost() == lowestF_Value) {
                nodesWithSameLowestF_Value.add(openNode);
            }
        }

        //if more than one node shares the same f value, we want to visit the one with the lowest heuristic (h) value
        if (nodesWithSameLowestF_Value.size() > 1) {

            int lowestHueristic = 10000000;
            Node lowestHueristicNode = null;

            for (Node node : nodesWithSameLowestF_Value) {
                if (node.getH_cost() < lowestHueristic) {
                    lowestHueristicNode = node;
                    lowestHueristic = node.getH_cost();
                }
            }
            lowestF_ValueNode = lowestHueristicNode;
        }

        //now we have the next node to move to
        return navigate(lowestF_ValueNode, goalNode, openList, closedList);

    }

    /**
     * Attempts to add the node to the open list, checking whether it's valid.
     * @param node Node in consideration
     * @param parentNode node considered previously
     * @param goalNode the node we are planning to
     * @param openList list of open nodes
     * @param closedList list of closed nodes
     * @return true if we need to finish route planning because we are next to the goal node and the goal node is blocked
     */
    private boolean addToOpenList(Node node, Node parentNode, Node goalNode, List<Node> openList, List<Node> closedList) {
        node.setG_cost(parentNode.getG_cost() + 1);
        node.setParent(parentNode);
        node.setH_cost(Math.abs(goalNode.getX() - node.getX()) + Math.abs(goalNode.getY() - node.getY()));
        node.computeF_Cost();
        if (isValid(node, openList, closedList)) {
            openList.add(node);
            return false;
        } else return node.equals(goalNode);
    }

    /**
     * Checks blocked locations to see whether a node is considered valid for traversal
     * @param node node in question
     * @param openList list of open nodes
     * @param closedList list of closed nodes
     * @return true if the node is valid
     */
    private boolean isValid(Node node, List<Node> openList, List<Node> closedList) {

        List<Location> blockedNodes = Warehouse.getBlockedLocations();

        HashSet<Location> tempBlocked = getTempBlockedLocations(node.g_cost);

        boolean nodeNotBlocked = true;

        // @Ali - this could be changed to use equals method - david
        for (Location blockedNode : blockedNodes) {
            if (node.getX() == blockedNode.getX() && node.getY() == blockedNode.getY()) {
                nodeNotBlocked = false;
            }
        }

        boolean nodeUnopened = true;
        for (Node openNode : openList) {
            if (node.getX() == openNode.getX() && node.getY() == openNode.getY()) {
                nodeUnopened = false;
            }
        }

        boolean nodeNotClosed = true;
        for (Node closedNode : closedList) {
            if (node.getX() == closedNode.getX() && node.getY() == closedNode.getY()) {
                nodeNotClosed = false;
            }
        }

        return nodeNotBlocked && nodeUnopened && nodeNotClosed
                && node.getX() <= 11 && node.getX() >= 0 && node.getY() <= 7 && node.getY() >= 0
                && !tempBlocked.contains(new Location(node.x, node.y));

    }

    /**
     * Returns all the predicted locations blocked by other robots at a given point in time
     * @param tick number of steps into the future
     * @return HashSet containing all predicted blocked locations
     */
    private HashSet<Location> getTempBlockedLocations(int tick) {
        HashSet<Location> blocked = new HashSet<>();
        if (tick > 3) {
            return blocked;
        } else {
            List<Robot> others = new ArrayList<>(robotList);
            others.remove(robot);

            for (Robot robot : others) {
                logger.debug("New robot blocked locations");
                Route directions = robot.getRoute();

                if (tick == 1) {
                    blocked.add(robot.getPreviousLocation());
                }

                try {
                    blocked.add(directions.getLocation(tick - 1));
                    blocked.add(directions.getLocation(tick));
                } catch (IndexOutOfBoundsException e) {
                    logger.trace("Robot has no plan this far");
                    blocked.add(robot.getLocation());
                } catch (NullPointerException e) {
                    logger.trace("Robot has no route");
                }
            }
            return blocked;
        }
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private int getH_cost() {
        return h_cost;
    }

    private void setH_cost(int h_cost) {
        this.h_cost = h_cost;
    }

    private int getG_cost() {
        return g_cost;
    }

    private void setG_cost(int g_cost) {
        this.g_cost = g_cost;
    }

    private void computeF_Cost() {
        f_cost = g_cost + h_cost;
    }

    private int getF_Cost() {
        return f_cost;
    }

    private Node getParent() {
        return parent;
    }

    private void setParent(Node parent) {
        this.parent = parent;
    }

    public Location toLocation() {
        return new Location(this.x, this.y);
    }

    public String toString() {
        return x + "," + y;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == Node.class) {
            Node n = (Node) o;
            return n.x == this.x && n.y == this.y;
        } else if (o.getClass() == Location.class) {
            Location l = (Location) o;
            return l.getX() == this.x && l.getX() == this.y;
        } else {
            return false;
        }
    }
}