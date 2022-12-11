public class CoffeeFactory {
    public Coffee getCoffee(int coffeeType) {
        if (coffeeType == 1) {
            return new Espresso();
        }
        if (coffeeType == 2) {
            return new Latte();
        }
        if (coffeeType == 3) {
            return new Cappuccino();
        }
        return null;
    }
}
