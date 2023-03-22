import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.collections.*;

public class ServerController {
    private Vector<Integer> currOrder = new Vector<Integer>();
    private ObservableList<String> currOrderNames = FXCollections.observableArrayList();
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    private int employeeID = 0; // TODO get session user's (employee) id
    @FXML
    private Label totalPriceField;
    @FXML
    private ListView<String> orderListView;

    public void initialize() {
        orderListView.setItems(currOrderNames);
    }

    @FXML
    private void addItemToOrder(ActionEvent event) {
        Node node = (Node) event.getSource();
        String data = (String) node.getUserData();
        int menuID = Integer.parseInt(data);
        currOrder.add(menuID);
        currOrderNames.add(db.getItemName(menuID));
        updateTotal(db.getOrderTotal(currOrder));
    }

    @FXML
    private void removeItem() {
        int selectedIndex = orderListView.getSelectionModel().getSelectedIndex();
        currOrder.remove(selectedIndex);
        currOrderNames.remove(selectedIndex);
        updateTotal(db.getOrderTotal(currOrder));
        orderListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void confirmOrder() {
        int orderID = db.getNewOrderId();
        db.updateOrdersAndOrderLineItemsTable(currOrder, employeeID, orderID);
        db.updateInventoryTransactionsAndInventoryTable(orderID);
        currOrder = new Vector<Integer>();
        currOrderNames.clear();
        updateTotal(db.getOrderTotal(currOrder));
    }

    private void updateTotal(float orderTotal) {
        totalPriceField.setText(String.format("%.2f", orderTotal));
    }
}
