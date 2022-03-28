package logic;

import food.Ingredient;

public class Selection {

	public Ingredient ingredient;
	public double amount;

	public Selection(Ingredient in, Double amount) {
		this.ingredient = in;
		this.amount = amount;
	}

	public String toString() {
		return ingredient.getName() + " " + amount + " grams";
	}

}
