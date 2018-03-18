package rp.warehouse.pc.route;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import rp.warehouse.pc.communication.Protocol;
import rp.warehouse.pc.data.Location;
import rp.warehouse.pc.data.Warehouse;

public class Node {
	private int x;
	private int y;
	private int h_cost;
	private int g_cost;
	private int f_cost;
	private Node parent;
	
	
	Node (int x, int y) {
		this.x = x;
		this.y = y;
		//parent is null until set; original starting node has null parent.
		parent = null;
		
	}
	
	public String printCoordinates(Node node) {
		if (node.getParent() == null) {
			return node.toString();
		}
		
		else return printCoordinates(node.getParent()) + ", " + node.toString();
	}
	
	public Queue<Integer> plan(Node start, Node end) {
		
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		
		Node endNodeWithParentPointersSorted = start.navigate(start, end, openList, closedList);
		
		ArrayList<Node> nodes = returnNodes(endNodeWithParentPointersSorted);
		
		Queue<Integer> plan = new LinkedList<Integer>();
		
		for (int i = 1; i < nodes.size(); i ++) {
			
			if (nodes.get(i-1).getX() == nodes.get(i).getX() - 1) {
				plan.add(Protocol.EAST);
			}
			
			if (nodes.get(i-1).getX() == nodes.get(i).getX() + 1) {
				plan.add(Protocol.WEST);
			}
			
			if (nodes.get(i-1).getY() == nodes.get(i).getY() + 1) {
				plan.add(Protocol.SOUTH);
			}
			
			if (nodes.get(i-1).getY() == nodes.get(i).getY() - 1) {
				plan.add(Protocol.NORTH);
			}
			
		}
		
		return plan;
	}
	
	/**
	 * Returns an array list of all the nodes in the path in the correct order
	 * @param node
	 * @return
	 */
	public ArrayList<Node> returnNodes(Node node) {
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
	public ArrayList<Node> returnNodesBackwards(Node node, ArrayList<Node> nodes) {
		
		if (node.getParent() == null) {
			nodes.add(node);
			return nodes;
		}
		
		nodes.add(node);
		
		return returnNodesBackwards(node.getParent(), nodes);
	}
	
	public Node navigate(Node currentNode, Node goalNode, List<Node> openList, List<Node> closedList) {
		
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
		
		for (int i = 0; i < openList.size(); i ++) {
			if (openList.get(i).getF_Cost() < lowestF_Value) {
				lowestF_ValueNode = openList.get(i);
				lowestF_Value = openList.get(i).getF_Cost();
			}
		}
		
		//in case more than one node shares the same f value, we want to visit the one with the lowest heuristic (h) value
		List<Node> nodesWithSameLowestF_Value = new ArrayList<Node>();
		
		for (int i = 0; i < openList.size(); i ++) {
			if (openList.get(i).getF_Cost() == lowestF_Value) {
				nodesWithSameLowestF_Value.add(openList.get(i));
			}
		}
		
		//if more than one node shares the same f value, we want to visit the one with the lowest heuristic (h) value
		if (nodesWithSameLowestF_Value.size() > 1) {
			
			int lowestHueristic = 10000000;
			Node lowestHueristicNode = null;
			
			for (int i = 0; i < nodesWithSameLowestF_Value.size(); i++) {
				if (nodesWithSameLowestF_Value.get(i).getH_cost() < lowestHueristic) {
					lowestHueristicNode = nodesWithSameLowestF_Value.get(i);
					lowestHueristic = nodesWithSameLowestF_Value.get(i).getH_cost();
				}
			}
			lowestF_ValueNode = lowestHueristicNode;	
		}
		
		//now we have the next node to move to
		return navigate (lowestF_ValueNode, goalNode, openList, closedList);
		
	}
	
	public void addToOpenList(Node node, Node parentNode, Node goalNode, List<Node> openList, List<Node> closedList) {
		if (isValid(node, openList, closedList)) {
			node.setParent(parentNode);
			node.setG_cost(parentNode.getG_cost() + 1);
			node.setH_cost(Math.abs(goalNode.getX() - node.getX()) + Math.abs(goalNode.getY() - node.getY()));
			node.computeF_Cost();
			openList.add(node);
		}
	}
	
	public boolean isValid(Node node, List<Node> openList, List<Node> closedList) {
		
		List<Location> blockedNodes = Warehouse.getBlockedLocations();
		
		boolean nodeNotBlocked = true;
		
		for (int i = 0; i < blockedNodes.size(); i++) {
			if (node.getX() == blockedNodes.get(i).getX() && node.getY() == blockedNodes.get(i).getY()) {
				nodeNotBlocked = false;
			}
		}
		
		boolean nodeUnopened = true;
		for (int i = 0; i < openList.size(); i++) {
			if (node.getX() == openList.get(i).getX() && node.getY() == openList.get(i).getY()) {
				nodeUnopened = false;
			}
		}
		
		boolean nodeNotClosed = true;
		for (int i = 0; i < closedList.size(); i++) {
			if (node.getX() == closedList.get(i).getX() && node.getY() == closedList.get(i).getY()) {
				nodeNotClosed = false;
			}
		}

		if (nodeNotBlocked && nodeUnopened && nodeNotClosed
				&& node.getX() <= 11 && node.getX() >= 0 && node.getY() <= 7 && node.getY() >= 0) {
			return true;
		}
		
		else {
			return false;
		}
		
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getH_cost() {
		return h_cost;
	}

	public void setH_cost(int h_cost) {
		this.h_cost = h_cost;
	}

	public int getG_cost() {
		return g_cost;
	}

	public void setG_cost(int g_cost) {
		this.g_cost = g_cost;
	}

	public void computeF_Cost() {
		f_cost = g_cost + h_cost;
	}
	
	public int getF_Cost() {
		return f_cost;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public String toString() {
		return x + "," + y;
	}		

}