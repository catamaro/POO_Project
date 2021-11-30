package model;

import structure.*;

/**
 * Computes weight of the edge using the Minimum Description Length (MDL) Method
 * 
 * @author Group 18
 *
 */
public class MDL_model extends LL_model{
	
	/**
	 *  Override of the LL model completed to do the MDL Score
	 * @param edge : Edge containing the child whose weight is going to be calculated
	 * @param node : Parent node
	 * @param N : total number of Instances (lines in the Train File)
	 * @param s : Class variable range
	 * @return	returns the weight of the edge
	 */
	@Override
	public double calc_weight(Edge edge, Node node, int N, int s) {
		
		// Gets the weight of the supper
		double LL_weight = super.calc_weight(edge, node, N, s);
		
		double weight = 0;
		int qi = node.getRange();	// Parent range
		int ri = edge.getChild().getRange();	// Child range
		
		// Adds penalty to the supper's weight
		weight = LL_weight - (s*(ri-1)*(qi-1)) / 2 * Math.log(N); // log refers to base e, same as ln
		
		return weight;
	}
}
