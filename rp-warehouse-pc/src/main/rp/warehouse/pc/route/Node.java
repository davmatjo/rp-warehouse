package rp.warehouse.pc.route;

import java.util.*;

import org.apache.log4j.Logger;
import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;
import rp.warehouse.pc.data.robot.Robot;

public class Node {
	private static final Logger logger = Logger.getLogger(Node.class);
	private static List<Robot> robotList;
	private int x;
	private int y;
	private int h_cost;
	private int g_cost;
	private int f_cost;
	private Node parent;
	
	static void setRobots(List<Robot> robots) {
		robotList = robots;
	}

	Node (int x, int y) {
		this.x = x;
		this.y = y;
		//parent is null until set; original starting node has null parent.
		parent = null;
		
	}
	
	private String printCoordinates(Node node) {
		if (node.getParent() == null) {
			return node.toString();
		}
		
		else return printCoordinates(node.getParent()) + ", " + node.toString();
	}
	
	public Route plan(Node start, Node end) {
		
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		
		Node endNodeWithParentPointersSorted = start.navigate(start, end, openList, closedList);
		
		ArrayList<Node> nodes = returnNodes(endNodeWithParentPointersSorted);
		
		return new Route(nodes);
	}
	
	/**
	 * Returns an array list of all the nodes in the path in the correct order
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
	
	private Node navigate(Node currentNode, Node goalNode, List<Node> openList, List<Node> closedList) {
		
		openList.remove(currentNode);
		closedList.add(currentNode);
		
		if (currentNode.getX() == goalNode.getX() && currentNode.getY() == goalNode.getY()) {
			return currentNode;
		}
		
		Node northNode = new Node(currentNode.getX(), currentNode.getY() + 1);
		Node eastNode = new Node(currentNode.getX() + 1, currentNode.getY());
		Node southNode = new Node(currentNode.getX(), currentNode.getY() - 1);
		Node westNode = new Node(currentNode.getX() - 1, currentNode.getY());
		
		addToOpenList(northNode, currentNode, goalNode, openList, closedList);
		addToOpenList(eastNode, currentNode, goalNode, openList, closedList);
		addToOpenList(southNode, currentNode, goalNode, openList, closedList);
		addToOpenList(westNode, currentNode, goalNode, openList, closedList);
		
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
		return navigate (lowestF_ValueNode, goalNode, openList, closedList);
		
	}
	
	private void addToOpenList(Node node, Node parentNode, Node goalNode, List<Node> openList, List<Node> closedList) {
		node.setG_cost(parentNode.getG_cost() + 1);
		if (isValid(node, openList, closedList)) {
			node.setParent(parentNode);
			node.setH_cost(Math.abs(goalNode.getX() - node.getX()) + Math.abs(goalNode.getY() - node.getY()));
			node.computeF_Cost();
			openList.add(node);
		}
	}
	
	private boolean isValid(Node node, List<Node> openList, List<Node> closedList) {
		
		List<Location> blockedNodes = Warehouse.getBlockedLocations();

		HashSet<Location> tempBlocked = getTempBlockedLocations(robotList, node.g_cost);

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

	private static HashSet<Location> getTempBlockedLocations(List<Robot> robots, int tick) {
		HashSet<Location> blocked = new HashSet<>();

		for (Robot robot : robots) {
			logger.debug("New robot blocked locations");
			Route directions = robot.getRoute();

			try {
				blocked.add(directions.getLocation(tick - 1));
			} catch (IndexOutOfBoundsException e) {
				logger.debug("Ignoring robot blocked location");
			}

			try {
				blocked.add(directions.getLocation(tick));
			} catch (IndexOutOfBoundsException e) {
				logger.debug("Ignoring robot blocked location");
			}

			try {
				blocked.add(directions.getLocation(tick + 1));
			} catch (IndexOutOfBoundsException e) {
				logger.debug("Ignoring robot blocked location");
			}
		}
		return blocked;
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

}