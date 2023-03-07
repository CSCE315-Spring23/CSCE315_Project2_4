import java.time.*;
import java.util.*;

public class order{
	// attributes
    private int orderID;
    private LocalDateTime orderTime;
    private int totalPrice = 0;
    private int employeeID;
    private List<orderLineItem> orderItems = new ArrayList<orderLineItem>();

    // constructor(s)
    public order(int id, int empID){
        orderID = id;
        employeeID = empID;
    }

    // getters and setters
    public int getOrderID(){
        return orderID;
    }
    public LocalDateTime getOrderTime(){
        return orderTime;
    }
    public int getTotalPrice(){
        return totalPrice;
    }
    public int getEmployeeID(){
        return employeeID;
    }
    public List<orderLineItem> getOrderItems(){
        return orderItems;
    }
    public order getFinalizedOrder(){
        orderTime = LocalDateTime.now();
        totalPrice = sumPrices();
        return this;
    }
    
    public void setOrderID(int id){
        orderID = id;
    }
    public void getOrderTime(LocalDateTime time){
        orderTime = time;
    }
    public void getTotalPrice(int price){
        totalPrice = price;
    }
    public void getEmployeeID(int id){
        employeeID = id;
    }

    // helper functs
    public void addOrderItem(orderLineItem item){
        orderItems.add(item);
    }
    public void deleteOrderItem(orderLineItem item){
        orderItems.remove(item);
    }
    public int sumPrices(){
        int sum = 0;
        for(orderLineItem item : orderItems){
            sum += item.price;
        }
        return sum;
    }

    // NOTES:
        // prices will be gathered from sql qurries(and can be intitalized to zero, 
        // using sql to set the price) whie totalPrice will be calculated from 
        // locally stored data in the orderlineitem object instances
}