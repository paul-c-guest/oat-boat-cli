package console;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import database.DBIO;
import database.Database;
import food.Ingredient;
import food.NutrientSet;
import logic.FDC_DB_Reader;
import logic.ingredientCompiler;

/**
 * A text based UI to allow user to add and remove the Ingredient objects from
 * their Database, nad search through a local copy of the FDC database
 * 
 * @author ballsies
 *
 */
public class DB_Editor {

	private Scanner reader;
	private Map<Integer, String> fdcFoodList;
	private Database db;
	private NutrientSet nutrInfo;
	private boolean dbChanged, userFinished, collectorFinished;

	public static void main(String[] args) {

		DB_Editor editor = new DB_Editor();

		// create the map of all available foods and load in the local .db file
		editor.initialise();

		// run the main loop
		editor.editLocalDatabase();

	}

	private void initialise() {
		// set the initial state of the flag for database changes to false
		dbChanged = false;

		// attempt to get a list of all available foods from the FDC food.csv database
		fdcFoodList = FDC_DB_Reader.getFoodList();
		if (fdcFoodList != null) {
			System.out.println("FDC food list loaded");
		}

		// attempt to load the local .db of Ingredients
		try {
			db = new DBIO().load();
			if (db != null) {
				System.out.println("local database loaded");
			}

			else {
				db = new Database();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

//		 for testing correct creation of hashmap
//		 should return an entry for a Pork product with fdc_id 168285
//		System.out.println("db records: " + foods.size());
//		System.out.println(foods.get(168285));

	}

	// main loop for editing local ingredient db
	private void editLocalDatabase() {
		reader = new Scanner(System.in);

		// print Ingredients db, add or remove an Ingredient
		while (true) {
			printInstructions();

			// get the first character from user input
			char command = getChar();

			if (command == 'x')
				break;

			else if (command == 'p')
				printIngredients();

			else if (command == 'i')
				getIngredientDetails();

			else if (command == 'a')
				addIngredientFDC();

			else if (command == 'm')
				addIngredientManually();

			else if (command == 'd')
				deleteIngredient();

			else if (command == 's')
				searchFDCFoodList();

			else
				System.out.println("\nunknown command: " + command);

		}

		// offer user chance to save database when changes have been made
		if (dbChanged)
			askToSave();

		// close the Scanner
		reader.close();
	}

	// the list of instructions to show user at the start of each main program loop
	private void printInstructions() {
		System.out.println();
		System.out.println("[p] Print Ingredient Database");
		System.out.println("[i] Show Ingredient details");
		System.out.println("[a] Add an Ingredient by FDC ID");
		System.out.println("[m] Add an Ingredient manually");
		System.out.println("[d] Delete an Ingredient");
		System.out.println("[s] Search FDC Food List");
		System.out.println("[x] Exit");
		System.out.print("\nEnter option: ");
	}

	private void printIngredients() {
		printList(db.getIngredientList(), true);
	}

	private void getIngredientDetails() {

		// get a list of ingredients for reference
		List<Ingredient> list = db.getIngredientList();

		printList(list, false);

		System.out.print("\nEnter the index of the ingredient to display: ");

		int command = Integer.parseInt(getString());
		System.out.println();

		if (command >= 0 && command < list.size()) {
			System.out.println(list.get(command));
			System.out.println(list.get(command).nutrition.toString());
		} else {
			System.out.println("Not in the list");
		}

	}

	private void deleteIngredient() {

		// get a list of ingredients for reference
		List<Ingredient> list = db.getIngredientList();

		printList(list, false);

		int choice;
		while (true) {
			try {
				System.out.print("\nEnter the index of the ingredient to remove or -1 to return: ");
				choice = getInteger();

				if (choice == -1)
					break;

				else if (choice > -1 && choice < list.size()) {
					System.out.print("Delete the ingredient " + list.get(choice).getName() + "? [y/n]: ");

					if (getChar() == 'y') {
						db.delete(list.get(choice).getFDC_ID());
						dbChanged = true;
						break;

					} else
						break;

				}

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

		}

	}

	// helper method prints each ingredient in a list with either the fdc id or the
	// list index
	private void printList(List<Ingredient> list, boolean fdc_id) {

		System.out.println("\nIngredients in local OatBoat Database:\n");

		for (Ingredient in : list) {
			if (fdc_id)
				System.out.print(in.getFDC_ID());

			else
				System.out.print(list.indexOf(in));

			System.out.println(": " + in.getName());
		}

	}

	private void searchFDCFoodList() {
		// get a search term
		System.out.print("\nEnter one keyword: ");
		String term = getString();

		// print the fdc_id and the food name if the search term matches
		for (Integer entry : fdcFoodList.keySet()) {
			if (fdcFoodList.get(entry).toLowerCase().contains(term.toLowerCase())) {
				System.out.println(entry + ": " + fdcFoodList.get(entry));
			}
		}

	}

	/**
	 * method asks for the fcd_id of a food to add as an ingredient to the local
	 * database. it then checks with user that the food to add is correct, and asks
	 * for a simple name to use as the internal reference name.
	 */
	private void addIngredientFDC() {
		// ask for an FDC ID
		System.out.print("\nEnter the 6-digit FDC ID or -1 to return: ");

		// get a valid integer for the fdc id
		int fdcID;
		while (true) {
			try {
				fdcID = Integer.parseInt(getString());
				break;
			} catch (Exception e) {
				System.out.print("invalid format, try again: ");
			}
		}

		// check that the given fdc id matches the correct food item to add
		if (fdcFoodList.containsKey(fdcID)) {
			// output the information with the corresponding food item
			System.out.println("\nFound: " + fdcFoodList.get(fdcID));
			System.out.print("Add this food to the local database of Ingredients? [y/n]: ");

			// if user enters 'y', add the food item as an Ingredient object to Database,
			// with all found values from fdc nutrient database
			if (getChar() == 'y') {
				// start a background thread to go and create a nutritional info object
				userFinished = false;
				collectorFinished = false;
				Thread nutrientSetCreator = new Thread(new NutrientCollector(fdcID));
				nutrientSetCreator.start();

				// get a simple name for the ingredient from the user, then notify other thread
				// that user is done
				System.out.print("\nEnter the name that will be used when displaying this Ingredient: ");
				String name = getString();
				userIsFinished();

				while (!collectorFinished) {
					try {
						wait();
					} catch (Exception e) {
					}
				}

				// create the new ingredient and add it to the DB
				db.add(new Ingredient(name, Integer.toString(fdcID), nutrInfo));
				dbChanged = true;

				// give the helper thread a chance to stop 
				nutrientSetCreator.interrupt();

			}

		} else {
			System.out.println("no record");
		}

	}

	private void addIngredientManually() {
		String name, fdc_id;
		double energyKJ, energyKcal, protein, salt, fat, sugar, carbs, saturates;

		System.out.print("\nName for new ingredient: ");
		name = getString();

		System.out.print("\nEnter values only if known, press enter to skip\n\nFDC ID: ");
		fdc_id = getString();

		System.out.print("Energy (kJ):\t");
		energyKJ = getDouble(true);

		System.out.print("Energy (kcal):\t");
		energyKcal = getDouble(true);

		System.out.print("Protein:\t");
		protein = getDouble(true);

		System.out.print("Salt:\t\t");
		salt = getDouble(true);

		System.out.print("Fat:\t\t");
		fat = getDouble(true);

		System.out.print("Sugar:\t\t");
		sugar = getDouble(true);

		System.out.print("Carbohydrates:\t");
		carbs = getDouble(true);

		System.out.print("Saturates:\t");
		saturates = getDouble(true);

		Ingredient newIng = new Ingredient(name, fdc_id,
				new NutrientSet(energyKcal, energyKJ, fat, saturates, carbs, sugar, protein, salt));

		System.out.println("\nIngredient entered as:\n\n" + newIng + newIng.nutrition);
		System.out.print("\nAdd this Ingredient? [y/n] ");

		if (getChar() == 'y') {
			dbChanged = true;
			db.add(newIng);
		}
	}

	// attempts to return a user string
	private String getString() {
		while (true) {
			try {
				return reader.nextLine().trim();

			} catch (Exception e) {
				System.out.print("Something went wrong, try again: ");
			}
		}
	}

	// attempts to parse and return an integer from user input
	private int getInteger() {
		while (true) {
			try {
				return Integer.parseInt(reader.nextLine());

			} catch (Exception e) {
				System.out.print("Not a valid input, try again: ");
			}
		}
	}

	/**
	 * attempts to parse user input and return a Double.
	 * 
	 * @param returnZero when true, an error will result in returning 0.0
	 * @return a double from the given input, or 0.0
	 */
	private double getDouble(boolean returnZero) {
		while (true) {
			try {
				return Double.parseDouble(reader.nextLine());

			} catch (Exception e) {
				if (returnZero)
					return 0.0;
				else
					System.out.print("Something went wrong, try again: ");
			}
		}
	}

	// method returns first valid character (in lower case) from user input
	private char getChar() {
		return reader.nextLine().trim().toLowerCase().charAt(0);
	}

	// method called at end of main routine if the flag dbChanged is true
	private void askToSave() {
		System.out.print("Save database changes before exiting? [y/n]: ");

		if (getChar() == 'y') {
			new DBIO().save(db);
		}
	}

	/**
	 * class to use as background thread which will collect nutritional data from
	 * nutrient CSV while user manually enters other information
	 * 
	 * @author ballsies
	 *
	 */
	private class NutrientCollector implements Runnable {

		private int fdcID;

		public NutrientCollector(int fdcID) {
			this.fdcID = fdcID;
		}

		public void run() {
			nutrInfo = new ingredientCompiler().create(fdcID);
			collectorFinished = true;
			waitForUser();
		}

		private synchronized void waitForUser() {
			while (!userFinished) {
				try {
					wait();

				} catch (InterruptedException e) {
				}
			}
			notifyAll();
		}

	}

	private synchronized void userIsFinished() {
		userFinished = true;
		notifyAll();
	}

}
