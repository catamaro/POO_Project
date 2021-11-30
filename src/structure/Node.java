package structure;
import java.util.Objects;

/**
 * Object that stores the information about each Feature of the Train Set.
 * 
 * @author Group 18
 *
 */
public class Node{
	
	private final String key;	
	private final int range;
	private final int index;
	private boolean isVisited = false;

	public double[][][][] Nijkc;
	public double[][] Nijc;
	public double[] Nc;
	public double[] cCounts;
	
	
	public double[][][][] theta;
	
	/**
	 * Getter for the Nc
	 * @return : Array counting number of times each class variable is occurs
	 */
	public double[] getNc() {
		return Nc;
	}
	public double [] theta_c;
	
	/**
	 * Constructor for the Node class.
	 * @param key : Name of the Feature,
	 * @param range : Range of the Feature,
	 * @param index : Index of the Feature.
	 */
	public Node(String key, int range, int index){
		this.key = key;
		this.range = range;
		this.index = index;
	}
	
	/**
	 * Getter for the Index
	 * @return : Index of the Feature
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * Checks if the node as been visited (used in Prim algorithm)
	 * @return : boolean if node has been visited
	 */
	public boolean isVisited() {
		return isVisited;
	}
	
	/**
	 * Sets the current state of the visited/not visited node
	 * @param isVisited : boolean if node has been visited
	 */
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
	
	/**
	 * Getter for the key of the node
	 * @return : Key of the node (name of the Feature)
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Getter for the Range of the node
	 * @return : Range of the node
	 */
	public int getRange() {
		return range;
	}
	
	/**
	 * Override hashcode so that the nodes are ordered form 1 to n
	 */
	@Override
	public int hashCode() {
		return Objects.hash(index+1);
	}

	/**
	 * Override of java's equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return index == other.index;
	}
	
	/**
	 * Override of toString to print Key
	 */
	@Override
	public String toString() {
		return key;
	}

}
	

	
