package logic;

/**
 * Library of static methods for conversion between joules, kilojoules,
 * calories and kilocalories. 
 * Methods limited to those relevant for application in nutritional table creation for food labelling purposes.
 * The factor of 4184 is applied when converting.
 * 
 * Take note to choose correct method for required output, i.e. attention should
 * be paid to whether the chosen method will work with joules or kilojoules.
 * 
 * @author ballsies
 *
 */
public class Converter {

	// conversion factor of a single calorie of food-energy
	// to a single joule of energy
	private static final double ENERGY_CONVERSION_FACTOR = 4184;

	public static double convertCaloriesToJoules(double calories) {
		return calories * ENERGY_CONVERSION_FACTOR;
	}

	public static double convertJoulesToCalories(double joules) {
		return joules / ENERGY_CONVERSION_FACTOR;
	}

	public static double convertCaloriesToKiloJoules(double calories) {
		return convertCaloriesToJoules(calories) / 1000;
	}

	public static double convertKiloJoulesToCalories(double Kj) {
		return convertJoulesToCalories(Kj) * 1000;
	}

	public static void main(String[] args) {
		// required only for testing
	}

}
