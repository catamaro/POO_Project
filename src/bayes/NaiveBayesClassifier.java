package bayes;

import files.*;
import structure.*;

import java.util.List;
import java.util.Set;

/**
 * In the NaiveBayesClassifier class we go through each Instance of the Test File and
 * compute the joint probability P_B. Depending on the value of this probability, a prediction
 * is done for the Class Variable of that instance. Later all the predictions are compared with 
 * the real values in the Test Set and an accuracy for the TANBC is created. Other requested
 * evaluation metrics are also computed in this class.
 * 
 * @author Group 18
 *
 */
public class NaiveBayesClassifier {
	
	private int[] classification;
	private double[][][] confMatrix;
	private final TestSet TestData;
	private final Tree tree;
	private final List<Instance> Instances;
	
	/**
	 * Naive Bayes Classifier Constructor
	 * @param TestData : Entire Data from the Test File
	 * @param tree : Completed and final tree computed based on Train File
	 */
	public NaiveBayesClassifier(TestSet TestData, Tree tree) {
		this.TestData = TestData;
		this.tree = tree;
		
		Instances = TestData.getInstances();
	}
	
	/**
	 * Function that computes the joint Probability needed to classify the Trained tree
	 * @param tree : Completed and final tree computed based on Train File
	 * @param Instances : Instance (line) of the Test File
	 * @param idx : index of the current instance
	 */
	public void calcPB(Tree tree, Instance Instances, int idx) {
		
		Node classNode =  tree.getClassNode(); 
		double highestProb = 0;
		Node root = tree.getRoot();		
		int[] TestInst = Instances.getArray();
		
		// Run through all the Cs
		for (int c = 0; c < TestData.getClassRange()+1; c++) {
			boolean flag = false;
			double P_B = 1;
			
			// Account P_B(C)
			P_B *= classNode.theta_c[c];
			
			Set<Node> keys = tree.getDAG().keySet();
			// Get the parent
			for (Node key : keys) {
				
				// If we have reached the class node
				if(key.getIndex() == -1) {
					break;
				}
				// Running through the edges of a certain node
				for(int i = 0; i < tree.getDAG().get(key).size(); i++) {					
					int j;
					int k;
					// Get the child					
					Node child = tree.getDAG().get(key).get(i).getChild();
					// If we're in the root
					if(key.equals(root) && !flag) {
						k = TestInst[key.getIndex()];
						P_B *= key.theta[key.getIndex()][key.getIndex()][k][c];	
						flag = true; // Makes it so that we only account the root once					
					}					
					// gets from the test file the yi equivalent to the parent's index
					j = TestInst[key.getIndex()];					
					// gets from the test file the yi equivalent to the son's index
					k = TestInst[child.getIndex()];
					//Accounts the P_B(Xi|father,C)
					P_B *= key.theta[child.getIndex()][j][k][c];						
				}					
			}
			// If P_B is higher than the previous higher probability
			if (P_B > highestProb) {
				// Replaces the C with the current one
				highestProb = P_B;
				classification[idx] = c;
			}
		}
	}
	/**
	 * Function that iterates threw all the instances to calculate their probability and class
	 */
	public void instanceCalcPB() {
		
		classification = TestData.getClassification();
		int i = 0;
		
		for(Instance inst: Instances) {
			calcPB(tree, inst, i);
			i++;
		}		
		
		getMetrics();
	}
	/**
	 * Function that computed all of the TN, TP, FP and FN matrix to use in the metrics below the function 
	 * is private because is only use inside the package when the probabilities are all calculated
	 */
	private void getMetrics() {
		int range = TestData.getClassRange()+1;
		// stores for each possible value of C the a 2x2 confusion matrix with TP, TN, FP and FN
		confMatrix = new double[range][2][2];
		int i = 0;
		// iterates threw all the instances in the test set
		for(Instance inst: Instances) {
			// stores the classified value
			int output = classification[i];
			// stores the actual classification value
			int actual = inst.getClassVariable();
			
			// if the classification is correct
			if(output == actual) {
				// updates in the matrix of the value, that is correctly classified, as a TP
				confMatrix[actual][0][0]++; //true positive
				// iterates to all the other matrix and updates as a TN 
				for(int j = 0; j < range; j++) {
					// does not consider value that was already updated with the TP
					if(j != actual) confMatrix[j][0][1]++; //true negative
				}
			}
			// if the classification is not correct
			if(output != actual) {
				// updates in the matrix of the classified value as a FP
				confMatrix[output][1][0]++; //false positive
				// updates in the matrix of the value that was supposed to be classified as a FN
				confMatrix[actual][1][1]++; //false negative
			}

			i++;
		}		
	}
		
	/**
	 * Function to compute the accuracy per class and his average based on the confusion matrix	
	 * @return result : String with the results of the accuracy to be printed in the terminal
	 */
	public String getAccuracy(){
		int range = TestData.getClassRange()+1;
		
		double accuracy = 0;
		double total = 0;
		String result = new String("accuracy : ");
		
		// iterates threw all the possible value of c
		for(int i = 0; i < range; i++) {
			// computes the specificity as TP/N
			accuracy = confMatrix[i][0][0]/TestData.get_N();
			// sums the accuracy of all the possible values of c
			total += accuracy;
		}		
		// adds the result to the string
		result +=  total;
		
		return result;
	}
	/**
	 * Function to compute the specificity per class and his average based on the confusion matrix	
	 * @return result : String with the results of the specificity to be printed in the terminal
	 */
	public String getSpecificity(){
		
		double[] countClass = TestData.getCountClass();
		int range = TestData.getClassRange()+1;
		
		String result = new String("specificity : [ ");
		double specificity = 0;
		double average = 0;
		
		// iterates threw all the possible value of c
		for(int i = 0; i < range; i++) {
			// computes the specificity as TN/(TN+FP)
			specificity = confMatrix[i][0][1]/(confMatrix[i][0][1] + confMatrix[i][1][0]);
			// stores the result in the string
			result += i + ": " + specificity + ",  ";
			// adds to the average the specificity of this value of c multiplied by the 
			// number of times that the value appears in the test set
			average += specificity * countClass[i];
		}
		
		result += average/TestData.get_N() + "]";
		
		return result;
	}
	/**
	 * Function to compute the sensitivity per class and his average based on the confusion matrix	
	 * @return result : String with the results of the sensitivity to be printed in the terminal
	 */
	public String getSensitivity(){
		double[] countClass = TestData.getCountClass();
		int range = TestData.getClassRange()+1;
		
		String result = new String("sensitivity : [ ");
		double sensitivity = 0;
		double average = 0;
		
		// iterates threw all the possible value of c
		for(int i = 0; i < range; i++) {
			// computes the sensitivity as TP/(TP+FN)
			sensitivity = confMatrix[i][0][0]/(confMatrix[i][0][0] + confMatrix[i][1][1]);
			// stores the result in the string
			result += i + ": " + sensitivity + ",  ";	
			// adds to the average the sensitivity of this value of c multiplied by the 
			// number of times that the value appears in the test set
			average += sensitivity * countClass[i];
		}
		
		// computes the final average and adds it to the string
		result += average/TestData.get_N() + "]";
		
		return result;
	}
	
	/**
	 * Function to compute the f1score per class and his average based on the confusion matrix	
	 * @return result : String with the results of the f1score to be printed in the terminal
	 */
	public String getF1score(){
		double[] countClass = TestData.getCountClass();
		int range = TestData.getClassRange()+1;
		
		String result = new String("f1score : [ ");
		double sensitivity = 0;
		double precision = 0;
		double f1score = 0;
		double average = 0;
		
		//considering positive the 0 
		for(int i = 0; i < range; i++) {
			sensitivity = confMatrix[i][0][0]/(confMatrix[i][0][0] + confMatrix[i][1][1]);
			precision = confMatrix[i][0][0]/(confMatrix[i][0][0] + confMatrix[i][1][0]);
			// computes the f1score based on the harmonic mean between the precision and sensitivity
			f1score = 2*(precision*sensitivity)/(precision+sensitivity);
			// adds to the average the f1score of this value of c multiplied by the 
			// number of times that the value appears in the test set 
			average += f1score * countClass[i];
			// stores the result in the string
			result += i + ": " + f1score + ",  ";	
		}
		// computes the final average and adds it to the string
		result += average/TestData.get_N() + "]";
		
		return result;
	}
	
	/**
	 * Override of toString to print the Instances and the predicted classification
	 */
	@Override
	public String toString() {
		String result = new String();

		for(int i = 0; i < TestData.get_N(); i++) {
			result += "\n   -> instance " + (i+1) + ":	" + classification[i];
		}
			
		return result;
	}
	
}
