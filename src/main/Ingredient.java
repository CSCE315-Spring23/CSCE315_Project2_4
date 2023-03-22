public class Ingredient {

    private final String id;
    private final String name;
    private final String currAmt;
    private final String unit;
    private final String minAmt;
    private final String cost;

    public Ingredient() {
        this(null, null, null, null, null, null);
    }

    public Ingredient(String id, String name, String currAmt, String unit, String minAmt, String cost) {
        this.id = id;
        this.name = name;
        this.currAmt = currAmt;
        this.unit = unit;
        this.minAmt = minAmt;
        this.cost = cost;
    }
}