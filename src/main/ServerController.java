import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class ServerController {
    @FXML private Button hamburger;
    @FXML private Button baconHamburger;
    @FXML private Button doubleHamburger;
    @FXML private Button cheeseburger;
    @FXML private Button baconCheeseburger;
    @FXML private Button doubleCheeseburger;
    @FXML private Button bbHamburger;
    @FXML private Button baconBBHamburger;
    @FXML private Button doubleBBHamburger;
    @FXML private Button bbCheeseburger;
    @FXML private Button baconBBCheeseburger;
    @FXML private Button doubleBBCheeseburger;
    private Vector<Integer> currOrder = new Vector<Integer>();
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    private int employeeID = 0; //TODO get session user's (employee) id

    public void initialize(){
        
    }

    @FXML private void addItemToOrder(ActionEvent event) {
        Node node = (Node) event.getSource() ;
        String data = (String) node.getUserData();
        int menuID = Integer.parseInt(data);
        currOrder.add(menuID);
    }

    @FXML private void confirmOrder(){
        int orderID = db.getNewOrderId();
        db.updateOrdersAndOrderLineItemsTable(currOrder, employeeID, orderID);
        db.updateInventoryTransactionsAndInventoryTable(orderID);
        currOrder = new Vector<Integer>();
    }
}

/*
  * public class placeholderController {
        @FXML private Button hamburger;

        public void initialize(){
            hamburger.setOnAction(e -> addItemToOrder(1));
        }

        //button functions
        
        
        }

    }
  */
