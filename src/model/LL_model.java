package model;

import structure.*;

/**
 * Computes weight of the edge using the Log-Likelihood (LL) Method
 * 
 * @author Group 18
 *
 */
public class LL_model implements ScoreModel{
	
	/**
	 *  Override of the Interface completed to do the LL Score
	 * @param edge : Edge containing the child whose weight is going to be calculated
	 * @param node : Parent node
	 * @param N : total number of Instances (lines in the Train File)
	 * @param s : Class variable range
	 * @return	returns the weight of the edge
	 */
	@Override
	public double calc_weight(Edge edge, Node node, int N, int s) {
		double weight = 0;
		int qi = node.getRange();	// Parent range
		int ri = edge.getChild().getRange();	// Child range
		
		for( int j = 0; j < qi+1; j++ ) { // Parent range
			
			for( int k = 0; k < ri+1; k++ ) { // Child range

				for( int c = 0; c <= s; c++ ) { // Class range

					double Nijkc = node.Nijkc[edge.getChild().getIndex()][j][k][c]; 	
					double Nc = node.Nc[c];		
					double NikcJ = edge.getChild().Nijc[k][c];	
					double NijcK = node.Nijc[j][c];
					
					if (Nijkc != 0 && Nc != 0) {	
						weight += (double) Nijkc/N *log2((double)(Nijkc*Nc)/(double)(NikcJ*NijcK));
					}
				}
			}
		}
		return weight;	
	}
	
	/**
	 * Function that calculates the logarithm of base 2
	 * @param number : log_2(number), number whose logarithm is to be calculated
	 * @return : returns the logarithm of the said number
	 */
	protected final double log2(double number) {
		double log2Value = Math.log(number) / ScoreModel.ln2;
		if (Double.isNaN(log2Value))
			throw new RuntimeException("Error calculating log2(" + number + "): NaN");
		return log2Value;
	}
		
}
