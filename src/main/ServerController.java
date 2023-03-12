import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;

public class ServerController {
    private Vector<Integer> currOrder = new Vector<Integer>();
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    private int employeeID = 0; // TODO get session user's (employee) id
    @FXML private Label totalPriceField;

    public void initialize() {}

    @FXML
    private void addItemToOrder(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int menuID = Integer.parseInt(data);
        currOrder.add(menuID);
        updateTotal(db.getOrderTotal(currOrder));
    }

    @FXML
    private void confirmOrder() {
        int orderID = db.getNewOrderId();
        db.updateOrdersAndOrderLineItemsTable(currOrder, employeeID, orderID);
        db.updateInventoryTransactionsAndInventoryTable(orderID);
        currOrder = new Vector<Integer>();
        updateTotal(db.getOrderTotal(currOrder));
    }

    private void updateTotal(float orderTotal) {
        totalPriceField.setText(String.format("%.2f", orderTotal));
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
