import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CoffeeMachine {
    private enum Message {
        HOW_MUCH_WATER_NEED("Write how many ml of water you want to add: \n"),
        HOW_MUCH_MILK_NEED("Write how many ml of milk you want to add: \n"),
        HOW_MUCH_COFFEE_BEANS_NEED("Write how many grams of coffee beans you want to add: \n"),
        HOW_MUCH_CUPS_NEED("Write how many disposable cups you want to add: \n"),
        ENOUGH_STOCKS("I have enough resources, making you a coffee! \n"),
        NOT_ENOUGH_STOCKS("Sorry, not enough ");

        String value;

        Message(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private int moneyInStock;
    private HashMap<String, Integer> ingredientsInStock = new HashMap<>();

    CoffeeMachine() {
        moneyInStock = 550;

        ingredientsInStock.put("cups", 9);
        ingredientsInStock.put("coffee beans", 120);
        ingredientsInStock.put("milk", 540);
        ingredientsInStock.put("water", 400);
    }

    public static void main(String[] args) {
        CoffeeMachine machine = initialize();
        machine.controller();
    }

    /**
     * Creates a new instance
     * of coffee machine
     */
    private static CoffeeMachine initialize() {
        return new CoffeeMachine();
    }

    /**
     * Shows current amount of ingredients
     * and money available in coffee machine
     */
    private void showStocks() {
        int waterInStock = ingredientsInStock.get("water");
        int milkInStock = ingredientsInStock.get("milk");
        int coffeeBeansInStock = ingredientsInStock.get("coffee beans");
        int cupsInStock = ingredientsInStock.get("cups");

        System.out.printf(
                "The coffee machine has:\n" +
                        "%d ml of water\n" +
                        "%d ml of milk\n" +
                        "%d g of coffee beans\n" +
                        "%d disposable cups\n" +
                        "$%d of money\n", waterInStock, milkInStock, coffeeBeansInStock, cupsInStock, moneyInStock);
        System.out.println();
        controller();
    }

    /**
     * Calls coffee machine function
     * by given user input
     */
    private void controller() {
        System.out.println("Write action (buy, fill, take, remaining, exit):");
        String function = askUserInput();
        System.out.println();
        switch (function) {
            case "buy" -> sellCoffee();
            case "fill" -> fillIngredients();
            case "take" -> giveMoney();
            case "remaining" -> showStocks();
            case "exit" -> {
                break;
            }
            default -> wrongInput();
        }
    }

    /**
     * Accepts user's input
     */
    private String askUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Sells a coffee chosen by user
     * if machine has enough ingredients
     * otherwise displays missing ingredients
     */
    private void sellCoffee() {
        String coffeeType = chooseCoffee();

        if (coffeeType.equals("back")) {
            System.out.println();
            controller();
        } else {
            Coffee coffee = getCoffee(Integer.valueOf(coffeeType));

            CheckStocksResult result = checkMissingIngreds(coffee);
            if (result.enoughStocks) {
                System.out.print(result.message);
                System.out.println();
                makeCoffee(coffee);
            } else {
                System.out.print(result.message);
                System.out.println();
            }
            controller();
        }
    }

    /**
     * Returns coffee type by given user's input
     */
    private String chooseCoffee() {
        System.out.println("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:");
        String coffeeType = askUserInput();
        return coffeeType;
    }

    /**
     * Returns coffee by given coffee type
     */
    private Coffee getCoffee(int coffeeType) {
        CoffeeFactory coffeeFactory = new CoffeeFactory();
        Coffee coffee = coffeeFactory.getCoffee(coffeeType);
        return coffee;
    }

    /**
     * Checks if coffee machine has enough ingredients
     * to make chosen coffee and prepare list of missing ingredients
     */
    private CheckStocksResult checkMissingIngreds(Coffee coffee) {
        CheckStocksResult result = new CheckStocksResult();
        HashMap<String, Integer> ingredsRequired = coffee.getIngredients();

        for (String ingredient : ingredientsInStock.keySet()) {
            if (ingredientsInStock.get(ingredient) < ingredsRequired.get(ingredient)) {
                result.missingIngreds.add(ingredient);
            }
        }

        if (result.missingIngreds.isEmpty()) {
            result.enoughStocks = true;
            result.message = Message.ENOUGH_STOCKS.getValue();
        } else {
            for (String ingredient : result.missingIngreds) {
                if (result.message.isEmpty()) {
                    result.message += ingredient;
                } else if (ingredient != null && !ingredient.isEmpty()) {
                    result.message += " and " + ingredient;
                }
            }
        }

        result.message = result.enoughStocks ? Message.ENOUGH_STOCKS.getValue() : Message.NOT_ENOUGH_STOCKS.getValue() + result.message + "!" + "\n";
        return result;
    }

    /**
     * Makes chosen coffee
     */
    private Coffee makeCoffee(Coffee coffee) {
        updateStocks(coffee);
        return coffee;
    }

    /**
     * Updates coffee machine stocks after a coffee was made
     */
    private void updateStocks(Coffee coffee) {
        for (String ingredient : ingredientsInStock.keySet()) {
            int currentAmount = ingredientsInStock.get(ingredient);
            int consumedAmount = coffee.getIngredients().get(ingredient);

            ingredientsInStock.put(ingredient, currentAmount - consumedAmount);
        }
        moneyInStock += coffee.getPrice();
    }

    /**
     * Updates ingredients in coffee machine stock
     */

    private void fillIngredients() {

        System.out.print(Message.HOW_MUCH_WATER_NEED.getValue());
        ingredientsInStock.put("water", ingredientsInStock.get("water") + Integer.valueOf(askUserInput()));

        System.out.print(Message.HOW_MUCH_MILK_NEED.getValue());
        ingredientsInStock.put("milk", ingredientsInStock.get("milk") + Integer.valueOf(askUserInput()));

        System.out.print(Message.HOW_MUCH_COFFEE_BEANS_NEED.getValue());
        ingredientsInStock.put("coffee beans", ingredientsInStock.get("coffee beans") + Integer.valueOf(askUserInput()));

        System.out.print(Message.HOW_MUCH_CUPS_NEED.getValue());
        ingredientsInStock.put("cups", ingredientsInStock.get("cups") + Integer.valueOf(askUserInput()));

        System.out.println();
        controller();
    }

    /**
     * Displays a message if a user filled incorrect input
     */
    private void wrongInput() {
        System.out.println("Wrong input!");
        controller();
    }

    /**
     * Gives all cash to the caller
     */
    private void giveMoney() {
        System.out.printf("I gave you $%d\n", moneyInStock);
        moneyInStock = 0;
        System.out.println();

        controller();
    }

    /**
     * Get current ingredients amount in coffee machine
     */
    public HashMap<String, Integer> getIngredientsInStock() {
        return ingredientsInStock;
    }

    /**
     * Get current money amount in coffee machine
     */
    public int getMoneyInStock() {
        return moneyInStock;
    }

    private class CheckStocksResult {
        Boolean enoughStocks = false;
        ArrayList<String> missingIngreds = new ArrayList<>();
        String message = "";
    }
}
