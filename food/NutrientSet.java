
package food;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Collation of the eight required nutritional information types: energy (in
 * kcal and kJoules), fat, saturates, carbohydrates, sugars, protein and salt.
 * <p>
 * Helper object for use by Ingredient objects
 *
 * @author ballsies
 */
public class NutrientSet implements Serializable {

	// the numder of elements being stored
	public static final int NUT_INDEX = 8;

	private static final long serialVersionUID = 2877603508165001519L;
	double energy_kcal;
	double energy_kJ;
	double fat;
	double saturates;
	double carbohydrates;
	double sugars;
	double protein;
	double salt;
	private List<Integer> missingValues = null;

	public NutrientSet(double energy_kJ, double energy_kcal, double fat, double saturates, double carbohydrates,
			double sugars, double protein, double salt) {
		this.energy_kJ = energy_kJ;
		this.energy_kcal = energy_kcal;
		this.fat = fat;
		this.saturates = saturates;
		this.carbohydrates = carbohydrates;
		this.sugars = sugars;
		this.protein = protein;
		this.salt = salt;
	}

	public NutrientSet() {
		energy_kJ = 0.0;
		energy_kcal = 0.0;
		fat = 0.0;
		saturates = 0.0;
		carbohydrates = 0.0;
		sugars = 0.0;
		protein = 0.0;
		salt = 0.0;
	}

	public NutrientSet(double[] inf) {
		if (inf.length == NUT_INDEX) {
			setAll(inf);
		} else {
			System.out.println("array is incorrect length, it must contain " + NUT_INDEX + " elements");
		}
	}

	public NutrientSet(double[] inf, ArrayList<Integer> errors) {
		this(inf);
		this.missingValues = errors;
	}

	/**
	 * method must be passed an array of length <b>8</b> -- otherwise set values
	 * individually.
	 * 
	 * @param inf an array of doubles representing the seven required nutritional
	 *            facts on food labelling
	 */
	private void setAll(double[] inf) {
		energy_kJ = inf[0];
		energy_kcal = inf[1];
		fat = inf[2];
		saturates = inf[3];
		carbohydrates = inf[4];
		sugars = inf[5];
		protein = inf[6];
		salt = inf[7];
	}

	public double[] getAllValues() {
		return new double[] { energy_kJ, energy_kcal, fat, saturates, carbohydrates, sugars, protein, salt };
	}

	public List<Integer> getMissingValues() {
		return missingValues;
	}

//	formatter:off
	public String toString() {
		return "\nEnergy (kJ):\t" + energy_kJ + "\nEnergy (kcal):\t" + energy_kcal + "\nFat:\t\t" + fat
				+ "\nSaturates:\t" + saturates + "\nCarbohydrates:\t" + carbohydrates + "\nSugars:\t\t" + sugars
				+ "\nProtein:\t" + protein + "\nSalt:\t\t" + salt / 1000;
	}

}
