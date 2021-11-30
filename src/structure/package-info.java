/**
 * This package creates the entire Tree Augmented Structure. It's constituted by the following classes:
 * 		<p>- Node
 * 		<p>- Edge
 * 		<p>- Graph
 * 		<p>- Tree
 *
 *<p> - 	The class Node is used to create an object Node for each feature of the Train Set. This object stores
 * all the information about the said feature, including it's Index, Range and Name as a constructor.
 * 
 * <p> -	The class Edge establishes a link (aka an edge) between every pair of node, this link is an object
 * that in the graph will be attached to the parent Node, considering this, the edge object contains 
 * the Node of the Child and the weight of the edge.
 * 
 *<p> -	The Graph class builds the entire graph, which means it contains all the information regarding all the 
 * Nodes and the Edges between them. This function is called to add the nodes and create the edges, and then
 * stores them in a Hash Map, which is a java structure. A typical Hash Map has structure (k,v), in our case
 * k is a parent Node, and v is the list of edges between this parent Node, and all the other nodes considering 
 * the remaining Nodes as the child. So our Hash Map is of type Map(Node, List(Edge)).
 *<p> 	It starts by initialising and incrementing the several Ns. Follows by computing the weights of half of the
 * edges, only half because the weights are the same independent of the direction of the edge (this is done to
 * improve efficiency by saving time), and finally it creates the complete graph by creating the remaining
 * edges and copying the weights from the previous computed ones.
 * 
 *<p> -	The Tree class extends the Graph, this happens because a tree is a type of graph but minimally connected.
 * It starts by applying the Prim algorithm, it assigns the root to the first feature of the HashMap and goes 
 * through all the edges picking the one with the heaviest weight for every connection. With the final tree completed
 * we compute the Theta_ijkc and Theta_c for every connection, to be later used in the Naive Bayes Classifier.
 */
package structure;

