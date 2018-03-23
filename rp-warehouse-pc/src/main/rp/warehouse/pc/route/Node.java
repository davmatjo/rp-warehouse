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

    /**
     * A method to add the robots
     * @param robots the list of Robots to be set
     */
    static void setRobots(List<Robot> robots) {
        robotList = robots;
    }

    /**
     * The overloaded constructor to create a Node object
     * @param x the x-coordinate of the node
     * @param y the y-coordinate of the node
     * @param robot the robot of the node
     */
    Node(int x, int y, Robot robot) {
        this.robot = robot;
        this.x = x;
        this.y = y;
        //parent is null until set; original starting node has null parent.
        parent = null;

    }

    /**
     * A method which was used for testing purpouses, to print the coordinates of the nodes.
     * @param node the goal node with all parent pointer set for the nodes before it, including itself.
     */
    private String printCoordinates(Node node) {
        if (node.getParent() == null) {
            return node.toString();
        } else return printCoordinates(node.getParent()) + ", " + node.toString();
    }

    /**
     * The method used to return a new Route for the Robot to use
     * @param start the starting location of the Robot
     * @param end the ending (goal) location of the Robot
     * @param pickup whether pickup is true or false
     * @return returns a new Route object based on the given parameters.
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
     * @param node the final (goal) node in the path, but with all parent pointer set, including its parent pointer
     * @return returns the nodes in the path in the correct order
     */
    private ArrayList<Node> returnNodes(Node node) {
        ArrayList<Node> nodes = new ArrayList<Node>();

        nodes = returnNodesBackwards(node, nodes);

        Collections.reverse(nodes);

        return nodes;
    }

    /**
     * Returns an array list of all the nodes in the path but in reverse order
     * @param node the final (goal) node in the path, but with all parent pointer set, including its parent pointer
     * @param nodes an initially empty, but later filled, array list of all the nodes, in reverse order.
     * @return returns the nodes in the route but in the reverse order.
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
     * A method which finds the Route for a robot from its starting node to its ending (goal) node
     * @param currentNode the node the robot is currently at.
     * @param goalNode the node the robot wants to go to.
     * @param the list of nodes that have been opened.
     * @param the list of nodes that have been closed.
     * @return returns the the final (goal) node in the path, but with all parent pointer set, including its parent pointer.
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
     * A method to add a specified node to the open list.
     * @param node the node we're at.
     * @param parentNode the node which we were at before.
     * @param goalNode the node we want to get to.
     * @param the list of nodes that have been opened.
     * @param the list of nodes that have been closed.
     * @return true if node we're at is the goal node.
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
     * A method to check if a node is valid.
     * @param node the node we're currently at.
     * @param the list of nodes that have been opened.
     * @param the list of nodes that have been closed.
     * @return true if the node is valid, false otherwise.
     */
    private boolean isValid(Node node, List<Node> openList, List<Node> closedList) {

        List<Location> blockedNodes = Warehouse.getBlockedLocations();

        HashSet<Location> tempBlocked = getTempBlockedLocations(node.g_cost);

        boolean nodeNotBlocked = true;

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
     * A method to get the temporarily-blocked locations being used by other Robots
     * @return returns the HashSet of temporarily-blocked locations
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

    /**
     * A method to get the x-coordinate of the node.
     * @return returns x
     */
    public int getX() {
        return x;
    }
    
    /**
     * A method to get the y-coordinate of the node.
     * @return returns y
     */
    public int getY() {
        return y;
    }

    /**
     * A method to get the heuristic of the node.
     * @return returns the heuristic.
     */
    private int getH_cost() {
        return h_cost;
    }

    /**
     * A method to set the heuristic of the node.
     */
    private void setH_cost(int h_cost) {
        this.h_cost = h_cost;
    }

    /**
     * A method to get the g-cost of the node.
     * @return returns the g-cost.
     */
    private int getG_cost() {
        return g_cost;
    }

    /**
     * A method to set the g-cost of the node
     */
    private void setG_cost(int g_cost) {
        this.g_cost = g_cost;
    }

    /**
     * A method to compute the f-cost of the node - it sums the g-cost and the h-cost (heuristic)
     */
    private void computeF_Cost() {
        f_cost = g_cost + h_cost;
    }

    /**
     * A method to get the f-cost of the node
     * @return returns the f-cost of the node
     */
    private int getF_Cost() {
        return f_cost;
    }

    /**
     * A method to get the parent of the current node
     * @return returns the parent node (the node before)
     */
    private Node getParent() {
        return parent;
    }

    /**
     * A method to set the parent node of the current node
     * @param parent the previous node we were at
     */
    private void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * A method to convert the node into a Location object equivalent
     * @return returns a Location equivalent of the node
     */
    public Location toLocation() {
        return new Location(this.x, this.y);
    }

    /**
     * A method to print the node value to the console
     * @return returns the x and y coordinate, seperated by a comma ONLY.
     */
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