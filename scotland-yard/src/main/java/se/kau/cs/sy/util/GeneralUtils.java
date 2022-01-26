package se.kau.cs.sy.util;

import java.util.Random;

/**
 * Utility class for arbitrary helper functions.
 * @author Sebastian Herold
 *
 */
public class GeneralUtils {

	public static int[] randomizeArray(int[] array){
		Random rgen = new Random(); 			
 
		for (int i = 0; i < array.length; i++) {
		    int randomPos = rgen.nextInt(array.length);
		    int temp = array[i];
		    array[i] = array[randomPos];
		    array[randomPos] = temp;
		}
 		return array;
	}
}
