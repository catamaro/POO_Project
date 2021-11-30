package model;

import structure.*;

/**
 * Interface, it's incomplete and general and therefore needs to be implemented
 * and overwritten by actual score models
 * 
 * @author Group 18
 *
 */
public interface ScoreModel {
	
	/**
	 *  Abstract method to be completed in the Models implementing it
	 * @param edge : Edge containing the child whose weight is going to be calculated
	 * @param node : Parent node
	 * @param N : total number of Instances (lines in the Train File)
	 * @param s : Class variable range
	 * @return	returns the weight of the edge
	 */
	public abstract double calc_weight(Edge edge, Node node, int N, int s);
	
	public static final double ln2 = Math.log(2);
}
