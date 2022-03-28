package food;

import java.io.Serializable;

/**
 * Object to represent each food ingredient, and store that ingredient's
 * information regarding: FDC ID (the American FDC unique identifier), cost per
 * 100 grams, and a NutrientSet Object which contains the nutritional values
 * required for formulation of a standard nutritional facts table.
 * 
 * @author ballsies
 *
 */
public class Ingredient implements Serializable, Comparable<Ingredient> {

	private static final long serialVersionUID = -2870338830093782444L;
	private String name;
	private String fdcID;
	private String desc; // description, taken from fdc full name
	private double cost;
	public NutrientSet nutrition;

	public Ingredient(String name, double cost, String fdcID, double[] nutrition) {
		this.name = name;
		this.cost = cost;
		this.fdcID = fdcID;
		this.nutrition = new NutrientSet(nutrition);
	}

	public Ingredient(String name, double cost, String fdcID) {
		this.name = name;
		this.cost = cost;
		this.fdcID = fdcID;
		nutrition = new NutrientSet();
	}

	public Ingredient(String name, String fdcID) {
		this(name, 0.0, fdcID);
	}

	public Ingredient(String name, String fdcID, NutrientSet info) {
		this.name = name;
		this.fdcID = fdcID;
		this.cost = 0.0;
		this.nutrition = info;
	}
	
	public String toString() {
		return name + ", FDC: " + fdcID;
	}

	public String getName() {
		return name;
	}

	/**
	 * The FDC ID is a unique identifier, usually a string of numerical digits. The
	 * IDs are maintained by an American federal department.
	 * 
	 * @return this food item's FDC ID
	 */
	public String getFDC_ID() {
		return fdcID;
	}

	/**
	 * Gets the price in Czech Koruna (Kc) we are currently paying per 100 grams.
	 * 
	 * @return Kc per 100g
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Set the cost of this food item. Cost is to be given as Czech Koruna (Kc /
	 * Crowns) per 100 grams.
	 * 
	 * @param cost Czech Kc per 100g
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public int compareTo(Ingredient other) {
		if (this.fdcID.equals(other.fdcID)) {
			System.out.println("[ATTENTION]: multiple entries for FDC ID " + this.fdcID + " exist in the database as: "
					+ this.name + " and " + other.name);
			return 0;
		}
		return this.name.compareTo(other.name);
	}

}
