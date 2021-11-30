package structure;

import model.*;
import files.*;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Graph class builds the entire graph, which means it contains all the information regarding all the 
 * Nodes and the Edges between them. This function is called to add the nodes and create the edges, and then
 * stores them in a Hash Map, which is a java structure. A typical Hash Map has structure (k,v), in our case
 * k is a parent Node, and v is the list of edges between this parent Node, and all the other nodes considering 
 * the remaining Nodes as the child. So our Hash Map is of type Map(Node, List(Edge)).
 *<p> 	It starts by initialising and incrementing the several Ns. Follows by computing the weights of half of the
 * edges, only half because the weights are the same independent of the direction of the edge (this is done to
 * improve efficiency by saving time), and finally it creates the complete graph by creating the remaining
 * edges and copying the weights from the previous computed ones.
 * 
 * @author Group 18
 *
 */
public class Graph {

	TrainSet TrainData;
	protected Map <Node, List<Edge>> DAG;
	protected Node classNode;
	
	/**
	 * Creates the Graph constituted by a HashMap of nodes, where each Node is pointing 
	 * to a list of all the possible edges
	 */
	public Graph() {
		this.DAG = new HashMap<Node, List<Edge>>();
	}
	
	/**
	 * Add in the hash table the key newNode and creates 
	 * an empty list of edges
	 * @param newNode : node with the feature index and range
	 */
	public void addNode(Node newNode) {
		
	    this.DAG.putIfAbsent(newNode, new ArrayList<Edge>());
	}
	
	/**
	 * Adds an edge to the graph.
	 * @param parent : defines the key of the hash
	 * @param child : node that is going to be saved inside object edge
	 * @param weight : weight of that edge
	 */
	public void addEdge(Node parent, Node child, double weight) {
		Edge newEdge = new Edge(child, weight); 
	    DAG.get(parent).add(newEdge);
	}
	
	/**
	 * Removes an edge from the graph
	 * @param a : edge to be removed
	 */
	public void removeEdge(Edge a) { 
		DAG.values().stream().forEach(e -> e.remove(a)); 
	}  
	
	/**
	 * Getter for the Class Node
	 * @return : Class Node
	 */
	public Node getClassNode() {
		return classNode;
	}
	  
	/**
	 * Overrides Java's equals
	 */
	@Override
	public boolean equals(Object obj) {
		//same position in memory
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		//different class
		if (getClass() != obj.getClass())
			return false;
		//cast to Graph class
		Graph other = (Graph) obj;
		return Objects.equals(DAG, other.DAG);
	}
	
	/**
	 * Getter for the entire graph
	 * @return Returns the full graph
	 */
	public Map<Node, List<Edge>> getDAG() {
		return DAG;
	}
	
	/**
	 * Override for Java's Hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(DAG);
	}
	
	/**
	 * Override for Java's toString so it prints the entire Graph
	 */
	@Override
	public String toString() {
		String listS = new String("Graph \n");
			
		for (Node N: DAG.keySet()){
			listS += N.toString() + "=" + DAG.get(N).toString() + "\n";
		} 
		
		return listS;
	}
	
	/**
	 * Calls all the functions that create the entire Graph
	 * @param traindata : contains the entire data from the train file
	 * @param scoreModel : score model picked by the user to calculate the weights, LL or MDL
	 */
	public void doGraph(TrainSet traindata, ScoreModel scoreModel) {
		this.TrainData = traindata;
		initialiseNodeNs();	
		createHalfEdges();
		setAllWeights(scoreModel);	
		createCompleteGraph();	
	}
	
	/**
	 * Function that, for each node, considers the possibilities where it is the father and all 
	 * the remaining nodes are the son, and Initialises the several Ns for that case.
	 */
	public void initialiseNodeNs()  {
		classNode = new Node("C", TrainData.getClassRange(), -1);		

		int nrXs = TrainData.get_n(); 
		int nrClass = TrainData.getClassRange();
		
		// Initialise nodes' counts
		Set<Node> keys = DAG.keySet();
		
		// Runs by each node considering it as the father
		for (Node key : keys) {
			key.Nijkc = new double[nrXs+1][][][];
		    // Runs every node considering it the son
			for (int j = 0; j < nrXs; j++) {
				// Initialise all possible values of Nijkc for each son
				
				key.Nijkc[j] = new double[key.getRange()+1][TrainData.getRange(j)+1][nrClass+1];
			}
			// Initialise all possible values of Nijc and Nc
			key.Nijc = new double[key.getRange()+1][nrClass+1];
			key.Nc= new double [nrClass+1];	
		}
		
		int[] Inst = new int[TrainData.get_n()];
		classNode.Nc = new double[TrainData.getClassRange()+1];
		int C = 0;
		// For every line of the Train Data, increments the values of the corresponding N
		for (int k = 0; k < TrainData.get_N(); k++) {
		
			Inst = TrainData.getInstances().get(k).getArray();
			C = TrainData.getInstances().get(k).getClassVariable(); 
			
			classNode.Nc[C]++;
			//System.out.println(classNode.Nc[C]);

			computeNodeNs(Inst , C );	
			
		}
	}

	/**
	 * Function that increments the several Ns considering the given line and for every 
	 * possibility of Node with parent/son
	 * @param Inst : Instance, line of values from the Train Data
	 * @param C :  Class Variable from a certain line in the Train Data
	 */
	private void computeNodeNs(int[] Inst, int C) {

		int nrXs = TrainData.get_n();
		// Initialise nodes' counts
		Set<Node> keys = DAG.keySet();	
		

		// Runs by each node considering it as the father6t
		for (Node key : keys) {
			
			key.Nc[C]++;
			
			// Runs every node considering it the son
			for (int i = 0; i < nrXs; i++) {
				
				if (key.getIndex() == i) { // case in which Xi has no parent. We will store this case in the position where node Xi is the parent of itself					
					key.Nijkc[i][ 0 ][ Inst[i] ][C] ++;	
					continue;
				}	
				
				key.Nijkc[i][ Inst[key.getIndex()] ][ Inst[i] ][C] ++;
			}
			key.Nijc[Inst[key.getIndex()]][C] ++;
		}
	}
	
	/**
	 * Creates the necessary edges to calculate the weights, since alpha_ij = alpha_ji
	 * This means it only creates half the total edges in the whole graph, creates one 
	 * per connection
	 */
	public void createHalfEdges() {

		Set<Node> keys1 = DAG.keySet();
		
		// Runs by each node considering it as the father
		for (Node key1 : keys1) {
			
			boolean found = false;
			Set<Node> keys2 = DAG.keySet();	
			// Runs every node until finding the node we were previously looking for
			for (Node key2 : keys2) {
				// if it hasn't found key1, continues the for
				if (!found && !(key1.getKey()).equals(key2.getKey())) {
		            continue;
		        }				
		        found = true;
		        addEdge(key1, key2, 0);	
			}
		}	
	}
	
	/**
	 * Function that computes the weights of the triangular matrix
	 * 
	 * @param scoreModel : score model picked by the user to calculate the weights, LL or MDL
	 */
	public void setAllWeights(ScoreModel scoreModel) {
		
		int N = TrainData.get_N();
		int s = TrainData.getClassRange();
		Set<Node> keys = DAG.keySet();
		
		for (Node key : keys){
			
			for(int i = 0; i < DAG.get(key).size(); i++) {
				
				double weight = scoreModel.calc_weight(DAG.get(key).get(i), key, N, s);
				DAG.get(key).get(i).setWeight(weight);
			}
		}
	}
	
	/**
	 * Function to add the edges that are not already included in the graph because
	 * the weights are Symmetric , that are added to use in the Prim algorithm
	 */
	public void createCompleteGraph() {
		Edge edge;

		Set<Node> keys1 = DAG.keySet();
		Set<Node> keys2 = DAG.keySet();
		
		for (Node key1 : keys1){
			for (Node key2 : keys2){
				
				if ((key1).equals(key2)) {
					break;
				}				
				
				for(int i = 0; i < DAG.get(key2).size(); i++) {	
					
					edge = DAG.get(key2).get(i);
					if((edge.getChild()).equals(key1)) {
						
						addEdge(key1, key2, edge.getWeight());
						break;
					}
				}
			}
		}	
	}
	
	
}
