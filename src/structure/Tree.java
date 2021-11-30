package structure;

import files.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This Class extends the Graph, this happens because a tree is a type of graph but minimally connected.
 * It starts by applying the Prim algorithm, it assigns the root to the first feature of the HashMap and goes 
 * through all the edges picking the one with the heaviest weight for every connection. With the final tree completed
 * we compute the Theta_ijkc and Theta_c for every connection, to be later used in the Naive Bayes Classifier.
 * 
 * @author Group 18
 *
 */
public class Tree extends Graph{
	
	Node root;
	private Map <Node, List<Edge>> inputDAG;
	TrainSet TrainData;
	
	
	/**
	 * Tree constructor.
	 * @param inputDAG: DAG from which the tree will be constructed
	 * @param classNode: class node of the graph that the tree will be based on
	 */
	public Tree( Map<Node, List<Edge>> inputDAG, Node classNode ) {
		super();
		super.classNode = classNode;
		this.inputDAG = inputDAG;
	}
	/**
	 * Function that calls all the functions that create the tree
	 * @param TrainData : contains the entire data from the train file
	 */
	public void doTree(TrainSet TrainData) {
		this.TrainData = TrainData;
		applyPrim();
		createTAN();
		calcThetas();
	}
	/**
	 * Function that applies the Prim algorithm 
	 */
	public void applyPrim() {
		
		// retrieves the first entry of the hash map to assign it as the root
		Map.Entry<Node,List<Edge>> firstEntry = inputDAG.entrySet().stream().findFirst().get();
		Set<Node> keys = inputDAG.keySet();
				
		root = firstEntry.getKey();
		Node parent = root;		
		Node child = root;

		// sets the node as visited meaning is already added to the tree
		root.setVisited(true);	
		// adds the root to the tree
		super.addNode(root);
		
		// stops when all the nodes are added to the tree 
		while (isDisconnected()) {
			// auxiliary variables
			Edge maximumEdge = new Edge(null, 0);
			Edge candidateEdge = new Edge(null, 0);
			
			// iterates through all the nodes that are already on the tree to take the maximum  
			// edge of all it's connections
			for (Node key : keys) {
				if (key.isVisited()) {
					// gets the maximum edge of the node
					candidateEdge = getMaximum(inputDAG.get(key), key);
					// compares the candidate to maximum edge already stored from the other nodes
					if (candidateEdge.getWeight() >= maximumEdge.getWeight()) {
						// updates information of the new maximum edge
						parent = key;
						child = candidateEdge.getChild();
						maximumEdge = candidateEdge;
	                }
	        	}
	        }
			// when all the nodes are iterated the result child is set at visited
			child.setVisited(true);	
			// the edge is updated to already connected
			maximumEdge.setConnected(true);
			
			// edges add nodes are added to the tree
			super.addEdge(parent, child, maximumEdge.getWeight());
			super.addNode(child);
		}
	}
	/**
	 * Function that returns the maximum weighted edge of a specific node
	 * @param edges : edges of the parent node that are to be tested
	 * @param parent : parent node that will be tested
	 * @return maximumEdge : the edge with highest weight
	 */
	private Edge getMaximum(List<Edge> edges, Node parent) {
		
		double maximumWeigth = 0;
		Edge maximumEdge = null;
		
		// for a specific parent node runs threw to edges connected to them
		for(Edge edge: edges) {
			// gets the maximum if the child is not already visited and the edge is not already connected
			if(!edge.getChild().isVisited() && !edge.isConnected()){
				// update maximum edge if current edge has bigger weight
				if(edge.getWeight() >= maximumWeigth) {
					maximumWeigth = edge.getWeight();
					maximumEdge = edge;
				}
			}
		}	
		return maximumEdge;
	}
	
	/**
	 * Function that returns true if there is any node in the input DAG graph 
	 * the is disconnected
	 * @return boolean : true if the are any disconnect edged, false if there's not
	 */
	private boolean isDisconnected() {
		Set<Node> keys = inputDAG.keySet();
		
	    for (Node key : keys) {
	        if (!key.isVisited()) {
	            return true;
	        }
	    }
	    return false;
	}
	/**
	 * Function that completes the tree and connects all the nodes to the node class with no weight 
	 */
	public void createTAN() {
		int s = TrainData.getClassRange();
		// Initialise nodes' counts
		Set<Node> keys = DAG.keySet();
		// Initialises the Theta_C for the Class Node
		classNode.theta_c = new double [s+1];	
		
		// Adds the Class Node to the tree
		super.addNode(classNode);
		
		// Creates an Edge between every node
		for (Node key : keys) {
			super.addEdge(classNode, key, -1);
		}
	}
	
	/**
	 * Function that computes all the thetas for the final tree in all the possible situations
	 * The situations include the root and all the sets of parent/child, also calls the function
	 * that computes the Theta_C for the class node.
	 * <p> Stores every Theta in the parent node, same as done before for computing the Ns.
	 */
	public void calcThetas() {

		// Initialise nodes' counts
		Set<Node> keys = super.DAG.keySet();
		double Nlinha = 0.5;
		int s = classNode.getRange();
		calcThetaC();
		
		//Runs every node as parent
		for (Node key : keys) {
			// Initialises the size of theta in the parent node
			key.theta = new double [TrainData.get_n()+1][key.getRange()+1][TrainData.getMaxRange()+1][s+1];
			// Class node now belongs in the keys, we need to stop the loop when it finds the class node
			if(key.getIndex() == -1) {	// Since the class node has index -1, it's always in the end
				break;
			}	
			
			// if the current node is the root
			if(key.equals(root)) {				
				for( int k = 0; k < key.getRange()+1; k++ ) {
					for( int c = 0; c <= s; c++ ) {
						// Since it has no father Nijkc is the same as just Nkc
						double Nijkc = key.Nijc[k][c];				
						// Since it has no father Nijc is the same as calculating just Nc
						double Nijc_K = classNode.Nc[c];
						/* Puts the theta for the root in position [root.index][root.index] since it has no
						father and here we're not accounting for children*/
						key.theta[key.getIndex()][key.getIndex()][k][c] = (Nijkc + Nlinha) / (Nijc_K + (key.getRange()+1)*Nlinha);	
					}
				}					
			}
			
			// Runs every child of the parent node
			for(int i = 0; i < DAG.get(key).size(); i++) {
				
				Node child = DAG.get(key).get(i).getChild();
								
				int qi = key.getRange(); // Parent's range
				double ri = child.getRange();	// Child's range
		
				for( int j = 0; j < qi+1; j++ ) { // Parent's range
					
					for( int k = 0; k < ri+1; k++ ) { // Child's range
	
						for( int c = 0; c <= s; c++ ) { // Class range
		
							double Nijkc = key.Nijkc[child.getIndex()][j][k][c];							
							double Nijc_K = key.Nijc[j][c];
							// Computes the theta
							key.theta[child.getIndex()][j][k][c] = (double)(Nijkc + Nlinha) / (double)(Nijc_K + (double)(ri+1)*(double)Nlinha);								
						}						
					}
				} 
			}
		}
	}
	/**
	 * Function that computes the Theta_C value for the Class Node
	 */
	private void calcThetaC() {
		double N = TrainData.get_N();
		double Nlinha = 0.5;
		double s = classNode.getRange()+1;
		
	 	for ( int c = 0; c < s; c++ ){
	 		double Nc = classNode.Nc[c];
	 		// Updates the Theta_C in the Class Node
	 		classNode.theta_c[c] = ( Nc + Nlinha )/( N + s*Nlinha);
		}
	}
	/**
	 * Getter for the root of the tree
	 * @return : Returns the node that is the root of the tree
	 */
	public Node getRoot() {
		return root;
	}
	
	@Override
	public String toString() {
		String listS = new String("Tree \n");			
		for (Node N: DAG.keySet()){
			listS += N.toString() + "=" + DAG.get(N).toString() + "\n";
		} 		
		return listS;
	}
	
}
