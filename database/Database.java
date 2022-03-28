package database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import food.Ingredient;

/**
 * custom database of Ingredient objects, when saved exists as a local file
 * named oatboat.db
 * 
 * @author ballsies
 *
 */
public class Database implements Serializable {

	private static final long serialVersionUID = 8049347129796100229L;
	private Map<String, Ingredient> db;

	public Database() {
		db = new HashMap<String, Ingredient>();
	}

	/**
	 * regular wrapper method for hashmap 'put' method, to add an Ingredient to the
	 * Database
	 * 
	 * @param in the Ingredient to add
	 */
	public void add(Ingredient in) {
		if (!db.containsKey(in.getFDC_ID())) {
			db.put(in.getFDC_ID(), in);

		} else {
			System.out.print("\nThe database already has an entry matching that ID.\nUpdate the old entry? [y/n]: ");

			char command = new Scanner(System.in).nextLine().trim().charAt(0);
			if (command == 'y') {
				delete(in.getFDC_ID());
				add(in);
			}

			System.out.println();
		}
	}

	/**
	 * Remove the specified ingredient from the database. The spelling and Case need
	 * to match for success.
	 * 
	 * @param fdcID the Ingredient name to delete
	 */
	public void delete(String fdcID) {
		db.remove(fdcID);
	}

	/**
	 * method to return a list for iteration, or other interactive purposes
	 * 
	 * @return an arrayList of Ingredient objects
	 */
	public List<Ingredient> getIngredientList() {
		List<Ingredient> list = new ArrayList<Ingredient>();

		for (Ingredient in : db.values()) {
			list.add(in);
		}

		Collections.sort(list);
		return list;
	}

	public boolean isEmpty() {
		return db.isEmpty();
	}

	/**
	 * prints a list depending on given argument: [fdc, simple, detailed] fdc:
	 * fdc_id and name simple: name detailed: not implemented yet
	 * 
	 * @param arg      fdc, simple or detailed
	 * @param numbered number the list
	 */
	public void printIngredients(String arg, boolean indexed) {
		// index counter if numbering the list is requested
		int index = 0;

		if (arg == "fdc") {
			for (Ingredient item : db.values()) {
				if (indexed) {
					System.out.print(index + ": ");
				}
				System.out.println(item.getFDC_ID() + ", " + item.getName());
				index++;
			}

		} else if (arg == "simple") {
			for (Ingredient item : db.values()) {
				if (indexed) {
					System.out.print(index + ": ");
				}
				System.out.println(item.getName());
				index++;
			}

		} else if (arg == "detailed") {
			System.out.println("sorry, not implemented yet");
		}

	}

	/**
	 * default toString method returns a list of fdcID and name
	 */
	public String toString() {
		String ret = "";

		for (Ingredient item : db.values()) {
			ret += "\n" + item.getFDC_ID() + ": " + item.getName();
		}

		return ret;
	}
	

}
