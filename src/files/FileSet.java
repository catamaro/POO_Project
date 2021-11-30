package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Receives the file and decomposes them line by line, sending it to the correct place.
 * It can receive a train set or a test set.
 * 
 * @author Group 18
 *
 */
public class FileSet {
	
	List<Instance> Instances = new ArrayList<>();
	int nFeatures = 0;
	String[] features;
	private int classRange;

	/**
	 * File Set constructor.
	 * @param file : CSV type of file formated with a heading containing the attributes of the random variables 
	 * (X1, ... , Xn, C) and bellow the elements
	 */
	public FileSet(File file) {

        BufferedReader br = null;   
        String line = "";
        String cvsSplitBy = ",";
        
        try {
        	br = new BufferedReader(new FileReader(file));
        	boolean fileHeading = false;
            while ((line = br.readLine()) != null) {
            	if(!fileHeading) { //it hasn't reached the heading with the name of the variables
            		if(line.equals("")) {//line's still empty
            			continue;
            		}
            		else { //found line with the heading names
            			fileHeading = true;
            			// use comma as separator
                        features = line.split(cvsSplitBy);
                        // Removes the Class variable from the number of Features
                        nFeatures = features.length -1;   
            		}
            		continue;//keeps reading until it finds the heading
            	}
            	
                // Has found the heading and now it's reading the values
            	if(!line.equals("")) {//line has something written

            		Instance Instance = new Instance(line);
            		
            		Instances.add(Instance);   
            		
            		// Updates the max range of the class_variable
                    if( Instance.class_variable > classRange) {
                        classRange = Instance.class_variable;
                    }                    
            	}	
            	else // in case the line with the values isn't right after the headline
            		continue;
            
            }
        // Exceptions in case any error occurs
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } 
        }  
	}
	
	/**
	 * Function to get
	 * @return entire list of instances
	 */
	public List<Instance> getInstances() {
		return Instances;
	}
	
	/**
	 * Adds a list to the Linked List Instance with a line of integers corresponding to the
	 * values for each instance of the file
	 * 
	 * @param instance function that converts to the array of integers
	 */
	protected void addInstance(Instance instance) {	
		Instances.add( instance );
	}
	
	/**
	 * Gets the number of Features n in the file
	 * 
	 * @return number of features n
	 */
	public int get_n() {
		return nFeatures;
	}
	
	/**
	 * Gets the value ri representing the total lines of values
	 * 
	 * @return size of the Linked List created with all the instances
	 */
	public int get_N() {
		return Instances.size();
	}
	
	/**
	 * Getter for class variable range.
	 * @return : Range of the class variable
	 */
	public int getClassRange() {
		return classRange;
	}
	
	
}