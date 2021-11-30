package main;

import files.*;
import model.*;
import structure.*;
import bayes.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Set;

/**
 * This class calls every other class by the correct order, receives the inputs from the users, and prints the correct outcome.
 * 
 * @author Group 18
 *
 */
public class Main {
	
	public static Graph graph = new Graph(); 
		
	public static void main(String[] args) {
				
		// Exits if arguments are not correctly input
		if(args.length != 3) { 
			System.err.println("Necessary arguments: TrainFile TestFile Score(LL or MDL)");
			System.exit(-1);
		}
		
		// Start counting the time it takes to build the TANBC
		long starttime1 = System.currentTimeMillis();
		
		File TrainFile = new File(args[0]);		
		TrainSet TrainData = null;
		try {	
			TrainData = new TrainSet(TrainFile);
			// update in the main the graph and nodes created with the information from the Train File
			graph = TrainData.getGraph(); 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Reads the input from the arguments and executes the requested Score Model
		ScoreModel scoreModel = null;
		if ("LL".equals(args[2])) {
			scoreModel = new LL_model();
		} else if ("MDL".equals(args[2])) {
			scoreModel = new MDL_model();
		} else {
			System.err.println("Third argument must be LL or MDL to pick Score Model.");
			System.exit(-1);
		}
		
		// Does the complete Graph class
		graph.doGraph(TrainData, scoreModel);
		
		// Does the complete Tree class
		Tree tree = new Tree(graph.getDAG(), graph.getClassNode());	
		tree.doTree(TrainData);
		
		
		long endtime1 = System.currentTimeMillis();
		// PRINT THE CLASSIFIER //
		Set<Node> keys = tree.getDAG().keySet();
		String []child_name;
		System.out.println("\nClassifier: \n	Parent : Child");		
		for(Node key:keys) {
			child_name = new String [tree.getDAG().get(key).size()];
			for(int i = 0; i < tree.getDAG().get(key).size(); i++) {				
				Node child = tree.getDAG().get(key).get(i).getChild();
				child_name[i] = child.getKey();
			}
			System.out.println( "	" + key + " :	" + Arrays.toString(child_name));
		}
		
		// PRINT TIME TO BUILD CLASSIFIER //
		System.out.println("\nTime to build:\n	" + (endtime1 - starttime1) / 1000.0 + " seconds");
				
		// CLASSIFY //
		long starttime2 = System.currentTimeMillis();
		// Opens the Test File
		File TestFile = new File(args[1]);		
		TestSet TestData = null;
		try {	
			TestData = new TestSet(TestFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Starts testing the classifier
		NaiveBayesClassifier classifier = new NaiveBayesClassifier(TestData, tree);
		classifier.instanceCalcPB();
		
		long endtime2 = System.currentTimeMillis();		
			
		System.out.println("\nTesting the classifier:");		
		// PRINTS THE CLASSIFICATION RESULT //
		System.out.println(classifier);
		// PRINTS TIME TO TEST THE CLASSIFIER // 
		System.out.println("\nTime to test:\n	" + (endtime2 - starttime2) / 1000.0 + " seconds");	
		// PRINTS RESUME //
		System.out.println("Resume: \n   " + classifier.getAccuracy() + "\n   " + classifier.getSpecificity() + "\n   " + classifier.getSensitivity() + "\n   " + classifier.getF1score());
				
	}
}