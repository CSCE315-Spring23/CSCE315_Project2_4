import java.time.*;

public class orderLineItem {
    // attributes
    private int lineItemID;
    private int itemID;
    private float price;
    private int orderID;

    // constructor(s)
    public orderLineItem(int lineItemID, int itemID, float price, int orderID) {
        this.lineItemID = lineItemID;
        this.itemID = itemID;
        this.price = price;
        this.orderID = orderID;
    }

    // getters and setters
    public int getLineItemID() {
        return this.lineItemID;
    }

    public void setLineItemID(int lineItemID) {
        this.lineItemID = lineItemID;
    }

    public int getItemID() {
        return this.itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getOrderID() {
        return this.orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    // NOTES:
    // prices will be gathered from sql qurries(and can be intitalized to zero,
    // using sql to set the price) whie totalPrice will be calculated from
    // locally stored data in the orderlineitem object instances
}