package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Libraries and methods for reading data from CSV files retrieved from
 * fdc.nal.usda.gov
 * 
 * @author ballsies
 */
public class FDC_DB_Reader {

	/**
	 * Retrieves a value from a local copy of the fdc food nutrients database. the
	 * returned value requires interpretation by the user as either kcal, kJ, grams,
	 * or milligrams.
	 * <p>
	 * The value will typically be understood intuitively if it is an energy value;
	 * will be in grams if it is a common component such as protein; or will be in
	 * milligrams if it is an chemical element such as 'sodium'.
	 * 
	 * make sure the file food_nutrient.csv is discoverable at the location ~/fdc.
	 * 
	 * @param fdcID      a unique food item identifier
	 * @param nutrientID standardised identifiers to recognise correct values in
	 *                   database
	 * @return a double, the amount of the nutritional component per 100g of an
	 *         ingredient
	 */
	public static double getNutritionalValue(int fdcID, int nutrientID) {

		try {
			File foods = new File("fdc/food_nutrient.csv");

			if (foods.isFile()) {
				BufferedReader reader = new BufferedReader(new FileReader(foods));
				String row = "";

				// while the reader has another line to look at, split each row in the file to
				// an array
				while ((row = reader.readLine()) != null) {
					String[] lineData = row.split(",");

					// look for a match for both arguments and return the corresponding value
					// Strings from linedata indices are surrounded by quote marks which are trimmed
					// off by use of the substring method below
					if (fdcID == trimInteger(lineData[1]) && nutrientID == trimInteger(lineData[2])) {
						return Double.parseDouble(lineData[3].substring(1, lineData[3].length() - 1));
					}
				}
				reader.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// if the method gets here, return 'not a number' for use by caller
		return Double.NaN;
	}

	/**
	 * method which scrapes a local copy of the FDC food items database and produces
	 * a hashmap of fdc_id Integers and food descriptions as Strings
	 * 
	 * @return a map of Integers and Strings
	 */
	public static Map<Integer, String> getFoodList() {

		Map<Integer, String> mapToReturn = new HashMap<Integer, String>();

		try {
			File csv = new File("fdc/food.csv");

			if (csv.isFile()) {
				BufferedReader csvReader = new BufferedReader(new FileReader(csv));
				String row = "";

				while ((row = csvReader.readLine()) != null) {
					String[] lineData = row.split("\",\"");
					int fdcID = Integer.parseInt(lineData[0].substring(1));
					String foodDesc = lineData[2];

					// place a food item to the local map with key: fdc_id and value: the food's
					// description
					mapToReturn.put(fdcID, foodDesc);
				}

				csvReader.close();
				return mapToReturn;

			} else {
				System.out.println("The file food.csv wasn't loaded. Is it present in the folder 'fdc'?");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static int trimInteger(String input) {
		return Integer.parseInt(input.substring(1, input.length() - 1));
	}

}
