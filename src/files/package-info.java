/**
 * 	This package opens all the files and stores all the information contained by them.
 *<p>  It's constituted by the following classes:
 * 		<p> - FileSet
 * 		<p> - Instance
 * 		<p> - TrainSet
 * 		<p> - TestSet
 *<p>  	Both TrainSet and TestSet extend the class FileSet, this is due to the fact that both files are used
 * for different things therefore different actions are taken on each one of them.
 *<p>  	FileSet opens the input file and looks for the Heading containing the features's names. When it finds them,
 * stores it in a String array, and then proceeds to look for the values. Each line of values is stores in a 
 * linked list of instances, each Instance contains a line of values.
 *<p>  	In the TrainSet a Node is created for every Feature, with it's name, range and index; and this node is then
 * added to the graph.
 *<p>  	Whereas in the TestSet, since it's values only need to be used once in the classification, they aren't stored 
 * in order to save memory.
 */
package files;