package files;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class extends the FileSet class so it opens and correctly divides the Test File.
 * <p> Opposite to the Train Set, these values don't need to be stored since they're only
 * to be used once.
 * 
 * @author Group 18
 *
 */
public class TestSet extends FileSet{
	
	private double[] countClass;
	private int[] classification;
	
	/**
	 * TestSet constructor
	 * @param file : Input testing file
	 * @throws FileNotFoundException : Exception for no input file
	 */
	public TestSet(File file) throws FileNotFoundException {
		// Implements everything done in the super FileSet	
		super(file);
		
		countClass = new double [getClassRange()+1];
		classification = new int [Instances.size()];
		// Goes through each Instance and updates the Array with the class variable
		// and the counts of each C
		for(int i = 0; i < Instances.size(); i++) {
			int c = Instances.get(i).getClassVariable();
			classification[i] = c;	
			countClass[c]++;
		}		
	}
	
	/**
	 * Getter for the array with the class variable, to be latter altered to have the predictions
	 * @return : Array with the Class variable
	 */
	public int[] getClassification() {
		return classification;
	}
	
	/**
	 * Getter for the counts of times each C appears in the TestSet
	 * @return : Array containing the counts for each C
	 */
	public double[] getCountClass() {
		return countClass;
	}
}
