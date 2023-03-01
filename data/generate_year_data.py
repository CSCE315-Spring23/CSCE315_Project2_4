import csv
import random
import datetime

#menuPrices = [6.49,7.19,7.29,7.99,7.59,8.29,8.39,7.79,8.58,8.79,8.99,9.19,9.42,9.63,9.84,10.09,7.39,7.49,7.99,8.99,8.99,8.29,1.99,1.99,2.19,3.29,3.29,3.29,3.29,4.49,4.49,4.49,4.49,5.49,4.69,4.69,6.79,9.48,10.18,10.88,11.58,10.58,11.28,11.75,12.05,12.38,12.69,12.89,13.18,12.61,12.92,13.63,13.84,10.38,10.89,11.29,11.99,11.99]
#menuItemIngredients = [["bun", "burgerPatty"],["bun", "burgerPatty", "americanCheese"],["bun", "beanBurgerPatty"],["bun", "beanBurgerPatty", "americanCheese"],["bun", "burgerPatty", "bacon", "bacon"],["bun", "burgerPatty", "americanCheese", "bacon", "bacon"],["bun", "beanBurgerPatty", "bacon", "bacon"],["bun", "beanBurgerPatty", "americanCheese", "bacon", "bacon"],["bun", "burgerPatty", "burgerPatty"],["bun", "burgerPatty", "burgerPatty", "americanCheese", "americanCheese"],["bun", "beanBurgerPatty", "beanBurgerPatty"],["bun", "beanBurgerPatty", "beanBurgerPatty", "americanCheese", "americanCheese"],["bun", "burgerPatty", "burgerPatty", "bacon", "bacon"],["bun", "burgerPatty", "burgerPatty", "americanCheese", "americanCheese", "bacon", "bacon"],["bun", "beanburgerPatty", "beanburgerPatty", "bacon", "bacon"],["bun", "beanburgerPatty", "beanburgerPatty", "americanCheese", "americanCheese", "bacon", "bacon"],["breadSlice", "breadSlice", "burgerPatty", "americanCheese", "choppedOnion"],["bun", "chickenPatty"],["bun", "spicyChickenPatty"],["bun", "chickenPatty", "americanCheese", "avocado", "bacon", "bacon"],["subroll", "choppedChicken", "greenPepper", "redPepper", "choppedOnion", "provoloneCheese"],["salad"],["potato", "potato", "potato"],["drinkCup"],["bottledWater"],["iceCreamCup", "vanillaIceCream", "vanillaIceCream"],["iceCreamCup", "chocolateIceCream", "chocolateIceCream"],["iceCreamCup", "strawberryIceCream", "strawberryIceCream"],["iceCreamCup", "oreoIceCream", "oreoIceCream"],["shakeCup", "shakeLid", "vanillaIceCream", "vanillaIceCream", "milk"],["shakeCup", "shakeLid", "chocolateIceCream", "chocolateIceCream", "milk"],["shakeCup", "shakeLid", "strawberryIceCream", "strawberryIceCream", "milk"],["shakeCup", "shakeLid", "oreoIceCream", "oreoIceCream", "milk"],["shakeCup", "shakeLid", "rootBeer", "vanillaIceCream"],["chocolateChipCookie", "chocolateChipCookie", "vanillaIceCream"],["sugarCookie", "sugarCookie", "vanillaIceCream"],["chickenFinger", "chickenFinger", "chickenFinger", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "americanCheese", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "americanCheese", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "americanCheese", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "americanCheese", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "burgerPatty", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "burgerPatty", "americanCheese", "americanCheese", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "beanburgerPatty", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "beanburgerPatty", "americanCheese", "americanCheese", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "burgerPatty", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "burgerPatty", "burgerPatty", "americanCheese", "americanCheese", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "beanburgerPatty", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["bun", "beanburgerPatty", "beanburgerPatty", "americanCheese", "americanCheese", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["breadSlice", "breadSlice", "burgerPatty", "americanCheese", "choppedOnion", "drinkCup", "potato", "potato", "potato"],["bun", "chickenPatty", "drinkCup", "potato", "potato", "potato"],["bun", "spicyChickenPatty", "drinkCup", "potato", "potato", "potato"],["bun", "chickenPatty", "americanCheese", "avocado", "bacon", "bacon", "drinkCup", "potato", "potato", "potato"],["subroll", "choppedChicken", "greenPepper", "redPepper", "choppedOnion", "provoloneCheese", "drinkCup", "potato", "potato", "potato"]]
lineItemData = [["lineItemID", "menuItemID", "menuPrice", "orderID"]]
orderData = [["orderID", "orderTime", "totalPrice", "employeeID"]]

# Set the number of rows and columns in the CSV file
def generateOrderData(orderRows, currOrderID, currLineItemID, timestamps):
    #initialize needed variables
    maxLineItems = 15

    # Generate random data for orders [orderID, timestamp, totalPrice, employeeID]
    for i in range(orderRows):
        orderEntry = []
        #Generate orderId
        orderEntry.append(currOrderID)
        #Get Timestamp
        orderEntry.append(timestamps[i]) 
        #Generate items in order and get the sum of their menuPrices
        numItems = random.randint(1,maxLineItems)
        currLineItemID = generateLineItems(numItems, currOrderID, currLineItemID)
        orderEntry.append(0)
        #Generate employeeID
        orderEntry.append(random.randint(1,10))
        #Add order to the data list
        orderData.append(orderEntry)
        currOrderID += 1

    return currOrderID, currLineItemID


def generateLineItems(numItems, orderID, currLineItemID):
    itemIDs = []
    orderIngredients = []

    #Generate numItems line items [line item id, menu item id, menu price, order id]
    for i in range(numItems):
        item = []
        #lineItemID
        item.append(currLineItemID)
        itemIDs.append(currLineItemID)
        currLineItemID += 1
        #menuID
        itemMenuID = random.randint(1, 58)
        item.append(itemMenuID)
        item.append(0)
        #orderID
        item.append(orderID)
        #Append item to list of items in teh order
        lineItemData.append(item)

    return currLineItemID


# Write the data to a CSV file
def writeToCsv(data, filename):
    with open(filename, 'w', newline='') as file:
        writer = csv.writer(file)
        for row in data:
            writer.writerow(row)


if __name__ == '__main__':
    currOrderID = 0
    currLineItemID = 0

    for i in range(5): 
        numDailyOrders = random.randint(25,200)
        timestamps = []
        for j in range(numDailyOrders):
            date = datetime.datetime(2022,1,1,0,0,0)
            date += datetime.timedelta(days=i, hours=random.randint(10,21), minutes=random.randint(0,59), seconds=random.randint(0,59))
            timestamps.append(date)
        timestamps.sort()
        currOrderID, currLineItemID = generateOrderData(numDailyOrders, currOrderID, currLineItemID, timestamps)

    writeToCsv(orderData, 'Orders.csv')
    writeToCsv(lineItemData, 'OrderLineItems.csv')