package logic;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import food.Ingredient;
import food.NutrientSet;

public class NutritionTable {

	private int nutIndex;
	
	public String createWith(List<Selection> selections) {
		Map<Ingredient, Double> converted = new HashMap<Ingredient, Double>();
		for (Selection sel : selections) {
			converted.put(sel.ingredient, sel.amount);
		}
		return createWith(converted);
	}
	
	// private helper class which populates the selections list, before calculation
	// of the nutritional facts table
//	private class Selection {
//
//		public Ingredient ingredient;
//		public double amount;
//
//		public Selection(Ingredient in, Double amount) {
//			this.ingredient = in;
//			this.amount = amount;
//		}
//
//		public String toString() {
//			return ingredient.getName() + " " + amount + " grams";
//		}
//
//	}
	
	public String createWith(Map<Ingredient, Double> ingredients) {

		// initialise required helper variable
		nutIndex = NutrientSet.NUT_INDEX;
		double[] finalValues = new double[nutIndex];
		double totalWeight = 0.0;

		// determine total weight of everything in given hashmap 'ingredients'
		for (Double weight : ingredients.values()) {
			totalWeight += weight;
		}

		// for each ingredient, increment proportion of each of the seven nutritional
		// componenents (e.g.
		// salt, protein) to the array 'finalValues'
		for (Ingredient ing : ingredients.keySet()) {

			for (int i = 0; i < nutIndex; i++) {

				double[] ingValues = ing.nutrition.getAllValues();

				// here be magic -- TODO test the validity of this formula, and that it works
				// for both energy AND weights
				if (ingValues[i] > 0.0005) {
					finalValues[i] += (ingredients.get(ing) / totalWeight) * ingValues[i];
				}
			}
		}

		// set the locale to CZ to ensure decimal numbers will be separated by a comma
		// (does not necessarily need to be CZ, many other european locales would output
		// same decimal format)
		Locale.setDefault(Locale.forLanguageTag("cs-CZ"));

//		@formatter:off
		
		// construct and return the string from summed figures held in finalValues
		// TODO make this string more responsive to changes made to nutIndex
		return "\nNUTRITIONAL FACTS (100g):\n"
				+ "\nenergy\t\t" + String.format("%9s", (int) finalValues[0] + " kJ  ")
				+ "\n\t\t" + String.format("%9s", (int) finalValues[1] + " kcal")
				+ "\nfat\t\t" + String.format("%9s", roundDouble(finalValues[2]) + " g   ")
				+ "\n- saturates\t" + String.format("%9s", roundDouble(finalValues[3]) + " g   ")
				+ "\ncarbohydrates\t" + String.format("%9s", roundDouble(finalValues[4]) + " g   ")
				+ "\n- sugars\t" + String.format("%9s", roundDouble(finalValues[5]) + " g   ")
				+ "\nprotein\t\t" + String.format("%9s", roundDouble(finalValues[6]) + " g   ")
				+ "\nsalt\t\t" + String.format("%9s", roundDoubleMilligram(finalValues[7] / 1000) + " g   ");
	}
	
//	@formatter:on 

	// formatting helper
	private String roundDouble(double val) {
		DecimalFormat df = new DecimalFormat("#.#");
		return df.format(val);
	}

	// formatting helper
	private String roundDoubleMilligram(double val) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(val);
	}

}
