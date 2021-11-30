package structure;

import java.util.Objects;

/**
 * This class establishes a link (aka an edge) between every pair of node, this link is an object
 * that in the graph will be attached to the parent Node, considering this, the edge object contains 
 * the Node of the Child and the weight of the edge.
 * 
 * @author Group 18
 *
 */
public class Edge {       
	private final Node child;
	//TRUE if there is a child and a parent FALSE if undirected graph
	private boolean connected = false;
	private double weight; //saves the weight between two nodes based on LL or MDL
	
	/**
	 * Edge constructor.
	 * @param child : Node of the child to be associated to the edge
	 * @param weight : weight of the connection
	 */
	public Edge(Node child, double weight) {
		this.child = child;
		this.weight = weight;
	}
	
	/**
	 * Getter for the Child
	 * @return : Node of the child
	 */
	public Node getChild() {
		return child;
	}
	
	/**
	 * Checks if the edge of the graph has been added to the tree
	 * @return : boolean confirming if the edge has been connected to the tree
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * Sets the edge as Connected/Not connected to the tree
	 * @param isConnected : boolean confirming if the edge has been connected to the tree
	 */
	public void setConnected(boolean isConnected) {
		this.connected = isConnected;
	}
	
	/**
	 * Getter for the weight of the edge
	 * @return : Weight of the edge
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * Setter for the weight
	 * @param weight : weight to be set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	/**
	 * Override of Java's toString to print the edge
	 */
	@Override
	public String toString() {
		return "Edge [child=" + child + ", connected=" + connected + ", weight=" + weight + "]";
	}
	
	/**
	 * Override of Java's hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(child, connected, weight);
	}
	
	/**
	 * Override of Java's equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return Objects.equals(child, other.child) && connected == other.connected
				&& Double.doubleToLongBits(weight) == Double.doubleToLongBits(other.weight);
	}

}
