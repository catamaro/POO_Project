package files;

import structure.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class extends the FileSet class so it opens and correctly divides the Train File.
 * <p> It created a Node for every Feature, with it's name, range and index; and this node is then
 * added to the graph.
 * 
 * @author Group 18 
 *
 */
public class TrainSet extends FileSet{
	// Creates a new Graph
	private Graph graph = new Graph();
	
	/**
	 * Getter for graph
	 * @return : Graph created with the Train Data
	 */
	public Graph getGraph() {
		return graph;
	}

	private Node newNode;
	private int[] ranges;
	private int maxRange;
	
	/**
	 * TrainSet constructor.
	 * @param file : Input training file
	 * @throws FileNotFoundException : Exception for no input file
	 */
	public TrainSet(File file) throws FileNotFoundException {
		// Implements everything done in the super FileSet
		super(file);
		
		// Check the ranges for each Xi
		ranges = new int [nFeatures];
		// Checks the max value of each node
		for(int i = 0; i < Instances.size()  ; i++) {			
			for(int j = 0; j < nFeatures; j++) {
				if(Instances.get(i).getValue(j) > ranges[j]) {
					ranges[j] = Instances.get(i).getValue(j);
					if(ranges[j] > maxRange) {
						maxRange = ranges[j];
					}
				}
			}
		}
		// Creates a new node for each feature and adds it to the graph
		for(int a = 0; a < nFeatures ; a++) {
			newNode = new Node(features[a], ranges[a], a);
			graph.addNode(newNode);
		}	
	}
	
	/**
	 * Getter for the maximum range of all the Features. Used to initialise
	 * the K value of the theta array
	 * @return : Maximum range of all features
	 */
	public int getMaxRange() {
		return maxRange;
	}

	/**
	 * Gets max range of a Feature
	 * @param i : Index of Feature
	 * @return Maximum value available in the file for that feature
	 */
	public int getRange(int i) {
		return ranges[i];
	}
	
	/**
	 * Gets max ranges of every Feature
	 * @return Array with ranges of every feature
	 */
	public int[] getRanges() {
		return ranges;
	}
	
	/**
	 * Gets every feature's name
	 * @return String array containing features heading
	 */
	public String[] getFeatures() {
		return features;
	}
}
