package console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import database.DBIO;
import database.Database;
import food.Ingredient;
import logic.NutritionTable;

public class NutritionTableCreator {

	private Database db;
	private static Scanner in;

	public static void main(String[] args) {

		NutritionTableCreator ntc = new NutritionTableCreator();
//		@formatter:off
		// two options below are for: regular scanner use; or some fast input for testing (requires a working local database of at least 14 items)
		in = new Scanner(System.in);
//		in = new Scanner("13\n" + "55\n" + "12\n" + "5\n" + "10\n" + "3\n" + "9\n" + "40\n" 
//				+ "8\n" + "55\n" + "7\n" + "25\n" + "6\n" + "0.5\n" + "4\n" + "25\n" + "-1\n");
//		@formatter:on

		// attempts to load an existing database, or creates a new one.
		ntc.loadDatabase();

		// get user input, and either add an ingredient to database or create a
		// nutrition table
		// if command == 'a' add new ingredient
		ntc.addIngredientToDB();

		// if command == 't' select ingredients and create table
		System.out.println(ntc.createNutritionTable(ntc.selectIngredients()));

		// always save the state of the db on exit
		ntc.saveDatabase();

		// close the scanner
		in.close();
	}

	// attempt to load a local database file otherwise create a new db
	private void loadDatabase() {
		db = new DBIO().load();
		if (db == null) {
			db = new Database();
		}
	}

	private void saveDatabase() {
		new DBIO().save(db);
	}

	/**
	 * asks user for necessary and optional information then adds ingredient to
	 * Database
	 */
	private void addIngredientToDB() {
	}

	/**
	 * lists available ingredients, adds chosen ingredient with a weight to a
	 * collection
	 * 
	 * @return a hashmap of chosen ingredients and their weights
	 */
	private Map<Ingredient, Double> selectIngredients() {
		// the map to return
		Map<Ingredient, Double> selections = new HashMap<Ingredient, Double>();

		// get a copy of all currently available ingredients
		List<Ingredient> availableIngredients = db.getIngredientList();

		// local helpers for next while loop
		int index = 0;

		// print list and ask user which they would like to add to selections, with a
		// weight in grams
		System.out.println("Ingredients available to add:\n");
		while (true) {
			// print all available ingredients
			for (Ingredient ing : availableIngredients) {
				System.out.println(availableIngredients.indexOf(ing) + ": " + ing.getName());
			}

			// get user input
			System.out.print("\nEnter a number to add that ingredient, or enter -1 to finish: ");

			// get valid integer input from user
			while (true) {
				try {
					index = Integer.parseInt(in.nextLine());
					break;
				} catch (Exception e) {
					System.out.print("invalid input - try again: ");
				}
			}

			// break from the routine if input is -1
			if (index == -1) {
				break;
			}

			// add the ingredient to the selections (with a weight) and remove those
			// available
			if (index < availableIngredients.size()) {

				// ask for weight of this ingredient
				System.out.print("how many grams of " + availableIngredients.get(index).getName() + ": ");
				double value = Double.parseDouble(in.nextLine());
				System.out.println();

				// add to selections
				selections.put(availableIngredients.get(index), value);

				// remove from available
				availableIngredients.remove(index);

			} else {
				// otherwise assume index given is out of range
				System.out.println("\nthat index is not in the available range\n");
			}

			// break from the routine if the last item has been added from those available
			if (availableIngredients.size() == 0)
				break;

		}

		return selections;
	}

	// once ingredient selections have been made, create the text for a nutritional
	// facts table
	private String createNutritionTable(Map<Ingredient, Double> selectedIngredients) {
		if (selectedIngredients.size() == 0) {
			return "No table to display";
		}

		// make a nutritional table with the given data
		return new NutritionTable().createWith(selectedIngredients);
	}

}
