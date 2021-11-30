/**
 * This package contains the Score Models used to compute the weights between the edges.
 * <p>	To implement this we created an Interface with the functions necessary to create 
 * a model, which means other models, besides the two picked, can be implemented.
 * The two models picked are the Log-Likelihood Score and the Minimum Description Length Score,
 * the MDL score extends the LL because it is a type of Log-Likelihood score with an added
 * penalty.
 */
package model;