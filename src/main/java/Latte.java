import java.util.HashMap;

public class Latte implements Coffee {
    private final static int PRICE_PER_CUP = 7;

    private enum Ingredient {
        WATER_PER_CUP("water", 350),
        MILK_PER_CUP("milk", 75),
        CUPS("cups", 1),
        COFFEE_BEANS_PER_CUP("coffee beans", 20);

        private final String name;
        private final int amount;

        Ingredient(String name, int amount) {
            this.name = name;
            this.amount = amount;
        }

        public String getName() {
            return name;
        }

        public int getAmount() {
            return amount;
        }
    }

    @Override
    public int getPrice() {
        return PRICE_PER_CUP;
    }

    @Override
    public HashMap<String, Integer> getIngredients() {
        HashMap<String, Integer> ingredients = new HashMap<String, Integer>();
        for (Ingredient ingredient : Ingredient.values()) {
            ingredients.put(ingredient.getName(), ingredient.getAmount());
        }
        return ingredients;
    }
}
