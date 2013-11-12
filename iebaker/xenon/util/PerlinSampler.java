package iebaker.xenon.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * PerlinSampler is a class which can create a dense grid of a certain (but expandable) intiial width and 
 * initial height, with a set vector density, and set dimensionality.  Information about Perlin Noise used 
 * in this implementation taken from 
 * <a href='http://webstaff.itn.liu.se/~stegu/TNM022-2005/perlinnoiselinks/perlin-noise-math-faq.html'> The Perlin Noise Math FAQ</a>
 */ 
public class PerlinSampler {

	/**
	 * This is a class I created just because in Java you can't have an array of a generic type.
	 */
	private class FloatList extends java.util.ArrayList<Float>{} //I'm a bad person.

	/**
	 * This one's fun.  It is a 2D array of List objects which will store a grid of arbitrary length vectors
	 */
	private FloatList[][] grid;

	/**
	 * The seed to use for the random number generator
	 */
	private long randomSeed; 

	/**
	 * The seed of the random number generator.
	 */
	private Random random;

	/**
	 * The floating point sidelength of a single grid square.
	 */
	private float gridwidth;

	/**
	 * The number of grid squares in the y direction.
	 */
	private int width;

	/**
	 * The number of grid squares in the x direction.
	 */
	private int height;

	/**
	 * The dimensionality of the noise generated
	 */
	private int dimension;

	/**
	 * Constructor. Creates the perlin sampler with a set density, width, height, and dimension.  To understand this:
	 * The parameter for density sets the side length of a single Perlin grid square.  The width sets the number of 
	 * these squares in the x dimension and the height sets the number of these squares in the y dimension.
	 *
	 * @param gridwidth 		The width, a floating point value, of each gridsquare
	 * @param initialwidth 		The number of grid squares in the x dimension
	 * @param initialheight		The number of grid squares in the y dimension
	 * @param dimension 		The dimension of the random vectors chosen.
	 */
	public PerlinSampler(float gridwidth, int initialwidth, int initialheight, int dimension) {
		grid = new FloatList[initialwidth][initialheight]; //Disgusting.  In Java you can't make arrays of generic types.
		random = new Random();
		this.gridwidth = gridwidth;
		this.width = initialwidth;
		this.height = initialheight;
		this.dimension = dimension;
		populateGrid();
	}

	/**
	 * Constructor. Creates a perlin sampler with a set density, width, height, dimension, and seed for random number
	 * generation.  Use this one to create perlinSamplers which will generate the same values (seeded).
	 *
	 * @param gridwidth 		The width, a floating point value, of each gridsquare
	 * @param initialwidth 		The number of grid squares in the x dimension
	 * @param initialheight		The number of grid squares in the y dimension
	 * @param dimension 		The dimension of the random vectors chosen.
	 * @param seed 				A Seed to be used by the random number generator
	 */
	public PerlinSampler(float gridwidth, int initialwidth, int initialheight, int dimension, long seed) {
		grid = new FloatList[initialwidth][initialheight]; //I feel so bad about this.
		random = new Random(seed);
		this.gridwidth = gridwidth;
		this.width = initialwidth;
		this.height = initialheight;
		this.dimension = dimension;
		populateGrid();
	}

	/**
	 * Populates the gradient grid with random vectors.  Vectors are represented as lists of length D where
	 * D is the dimension of the Perlin Noise.  
	 */
	private void populateGrid() {

		//Iterate through the grid
		for(int i = 0; i < grid.length; ++i) {
			for(int j = 0; j < grid[i].length; ++j) {

				//Create a temporary list
				FloatList temp = new FloatList();
				float sqsum = 0;

				//FIll the list with random floats
				for(int pos = 0; pos < dimension; ++pos) {
					Float f = new Float(2 * random.nextFloat() - 1);
					sqsum += f*f;
					temp.add(f);
				}	

				//Normalize the vector
				float veclength = (float) Math.sqrt(sqsum);
				for(int k = 0; k < temp.size(); ++k) {
					temp.set(k, temp.get(k)/veclength);
				}

				//Set that value
				grid[i][j] = temp;
			}
		}
	}


	/** 
	 * Returns a floating point value for the noise function at the point (x, y) by sampling the Perlin noise
	 * function.
	 *
	 * @param x 	The x position at which to sample
	 * @param y 	The y position at which to sample
	 * @return 		The floating point value of the noise function at (x, y)
	 */
	public float sample(float x, float y) {
		//Calculate which grid square (x, y) falls into
		int x_index = (int) Math.round(Math.floor(x/gridwidth));
		int y_index = (int) Math.round(Math.floor(y/gridwidth));

		if(x_index > grid.length) {
			return Float.MIN_VALUE;
			//TODO Deal with x resizing
		}

		if(y_index > grid[0].length) {
			return Float.MIN_VALUE;
			//TODO Deal with y resizing
		}

		//System.out.prin
		java.util.List<Float> vector_of_point = new ArrayList<Float>();
		vector_of_point.add(x);
		vector_of_point.add(y);

		//Collect relevant points' gradient values
		java.util.List<Float> ul_gradient = grid[x_index][y_index];
		java.util.List<Float> ur_gradient = grid[x_index + 1][y_index];
		java.util.List<Float> ll_gradient = grid[x_index][y_index + 1];
		java.util.List<Float> lr_gradient = grid[x_index + 1][y_index + 1];

		//Calculate influence from each corner vector
		float ul_influence = dotProduct(ul_gradient, subVector(vector_of_point, Arrays.asList(new Float[]{x_index * gridwidth, y_index * gridwidth})));
		float ur_influence = dotProduct(ur_gradient, subVector(vector_of_point, Arrays.asList(new Float[]{(x_index + 1) * gridwidth, y_index * gridwidth})));
		float ll_influence = dotProduct(ll_gradient, subVector(vector_of_point, Arrays.asList(new Float[]{x_index * gridwidth, (y_index + 1) * gridwidth})));
		float lr_influence = dotProduct(lr_gradient, subVector(vector_of_point, Arrays.asList(new Float[]{(x_index + 1) * gridwidth, (y_index + 1) * gridwidth})));
		
		//Easing stuff
		float E_x = easingFunction((float)((x % gridwidth)/gridwidth));
		float x_avg_upper = ul_influence + (E_x * (ur_influence - ul_influence));
		float x_avg_lower = ll_influence + (E_x * (lr_influence - ll_influence));

		float E_y = easingFunction((float)((y % gridwidth)/gridwidth));
		float final_answer = x_avg_upper + (E_y * (x_avg_lower - x_avg_upper));
		
		//Return
		return 10 * final_answer / (1.4142f * (float)gridwidth);
	}


	/** 
	 * Returns the dot product of two vectors represented by Lists of Float
	 *
	 * @param v1 	The first vector
	 * @param v2 	The second vector
	 * @return 		The dot product of the vectors
	 */
	private float dotProduct(java.util.List<Float> v1, java.util.List<Float> v2) {
		float acc = 0;
		for(int i = 0; i < v1.size(); ++i) {
			acc += (v1.get(i) * v2.get(i));
		}
		return acc;
	}


	/**
	 * Performs component-wise subtraction of the two vectors given.
	 *
	 * @param minuend 		The vector from which the subtrahend is subtracted
	 * @param subtrahend 	The vector to be subtracted from the minuend 
	 */
	private java.util.List<Float> subVector(java.util.List<Float> minuend, java.util.List<Float> subtrahend) {
		java.util.List<Float> result = new ArrayList<Float>();
		for(int i = 0; i < minuend.size(); ++i) {
			result.add(i,  minuend.get(i) - subtrahend.get(i));
		}
		return result;
	}


	/**
	 * Returns the easing function of arg (which is restricted to the domain [0,1]).  The easing function gently pushes
	 * a value between 0 and 1 towards either 0 or 1 depending on which one it is closer to.
	 *
	 * @param arg 		The argument to be eased
	 * @return 			The floating point value of the easing function at arg
	 */
	private float easingFunction(float arg) {
		if(arg > 1) arg = 1;
		if(arg < 0) arg = 0;

		return (3 * arg * arg) - (2 * arg * arg * arg);
	}


	/**
	 * Returns a colorful string representation of the terrain values for debugging purposes
	 *
	 * @return 		a string which is a "#" character, and a color.  Color scheme credit to Adam Scherlis.
	 */
	private String toPrintable(float f) {
		if(f < -0.66) return "\033[34m\033[44m#";
		if(f < -0.33) return "\033[46m\033[36m#";
		if(f < 0) return "\033[45m\033[35m#";
		if(f < 0.33) return "\033[42m\033[32m#";
		if(f < 0.86) return "\033[42m\033[32m#";
		return "\033[43m\033[33m#";
	}

	/**
	 * Creates a test implementation of an 11 x 11 perlin grid of sidewidth 20.0, and dimension 2
	 */
	public static void main(String... args) {
		PerlinSampler ps = new PerlinSampler(20f, 11, 11, 2);
		for(float y = 0; y < 45; ++y) {
			for(float x = 0; x < 100; ++x) {
				System.out.print(ps.toPrintable(ps.sample(x, y)));
			}
			System.out.println("\033[0m");
		}
	}
}
