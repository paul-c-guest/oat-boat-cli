package logic;

import java.util.ArrayList;

import food.Ingredient;
import food.NutrientSet;

public class ingredientCompiler {

	private final int energy_kJ = 1062;
	private final int energy_kcal = 1008;
	private final int fat = 1004;
	private final int saturates = 1258;
	private final int carbohydrates = 1005;
	private final int sugars = 2000;
	private final int protein = 1003;
	private final int salt = 1093;
	private ArrayList<Integer> errors;

	/**
	 * method attempts to create an ingredient object from as much information that
	 * it can collect from local csv files
	 * 
	 * @param name   the simple name for the ingredient
	 * @param fdc_id the unique 6-digit FDC identifier
	 * @return an Ingredient object
	 */
	public Ingredient create(String name, int fdc_id) {
		String fdc = "" + fdc_id;
		return new Ingredient(name, fdc, create(fdc_id));
	}

	public NutrientSet create(int fdc_id) {
		// initialise helpers
		errors = new ArrayList<Integer>();

		// an array to populate with found nutritional values
		double[] nutritionalValues = new double[NutrientSet.NUT_INDEX];
		int[] idValues = new int[] { energy_kJ, energy_kcal, fat, saturates, carbohydrates, sugars, protein, salt };

		// for each possible nutritional value, attempt to retrieve a value from
		// food_nutrient.csv
		for (int i = 0; i < NutrientSet.NUT_INDEX; i++) {
			nutritionalValues[i] = getNutritionalValue(fdc_id, idValues[i]);
		}

		if (errors.size() > 0)
			return new NutrientSet(nutritionalValues, errors);

		else
			return new NutrientSet(nutritionalValues);
	}

	// return a nutritional value, otherwise return -1.0
	private double getNutritionalValue(int fdc_id, int nutrient_id) {
		double value;
		try {
			value = FDC_DB_Reader.getNutritionalValue(fdc_id, nutrient_id);

		} catch (Exception e) {
			value = 0.0;
			errors.add(nutrient_id);
		}

		return value;
	}
}

// TODO check id 1258 is correct value for saturated fatty acids
// TODO check which sugars to use
// TODO check each values metric i.e. gram or milligram
