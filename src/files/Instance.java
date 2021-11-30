package files;

/**
 * Instance of a file, to be used in a Linked List containing all Instances.
 * 
 * @author Group 18
 *
 */
public class Instance {
	
	private int [] values; 		// Line of values
	protected int class_variable;	// Class variable
	int range_Cvar = 0;		// Max value of class variable
	
	/**
	 * Receives a String[] containing the elements of a line from the file and converts
	 * each element to an integer so it can be used 
	 * 
	 * @param line : Line from the file
	 */
	public Instance(String line) {
		
		String[] elements = line.split("\\s*,\\s*"); 
		int size = elements.length;

	    values = new int [ size - 1 ];
	    
	    for(int i = 0; i < size -1; i++) {	    	
	         values[i] = Integer.parseInt(elements[i]);   
	    }	    
	    class_variable = Integer.parseInt(elements[size-1]); //last position	    
	}
	
	/**
	 * Gets the value of the Class Variable for this line
	 * 
	 * @return class variable from line k
	 */
	public  int getClassVariable() {
		
		return class_variable;
	}
	
	/**
	 * Gets the value x_ik for this line
	 * @param i : feature index from the line
	 * @return	returns the value of the corresponding feature from this line
	 */
	public int getValue(int i) {
		return values[i];
	}
	
	/**
	 * Gets full line from the input file
	 * @return array with values from a line in the input file
	 */
	public int[] getArray(){
		return values;
	}
}
