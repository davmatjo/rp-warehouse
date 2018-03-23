package rp.warehouse.pc.assignment;

import rp.warehouse.pc.data.Task;

import java.util.Queue;

/**
 * Order of items for a robot
 *
 * @author Dylan
 */
class ItemOrder {

    private int pathCost;
    private Queue<Task> order;

    /**
     * @param pathCost The cost of the order
     * @param order    The order
     */
    ItemOrder(int pathCost, Queue<Task> order) {
        this.pathCost = pathCost;
        this.order = order;
    }

    public Queue<Task> getOrder() {
        return order;
    }

    public int getPathCost() {
        return pathCost;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Task task : order) {
            s.append("(").append(task.getItem().getLocation().getX()).append(", ").append(task.getItem().getLocation().getY()).append(") ");
        }
        return s.toString();
    }

}
