import java.sql.*;
import java.util.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class ServerController {
    private Vector<Integer> currOrder = new Vector<Integer>();
    private ObservableList<String> currOrderNames = FXCollections.observableArrayList();
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    private int employeeID = 0; // TODO get session user's (employee) id
    @FXML private Label totalPriceField;
    @FXML private ListView<String> orderListView;
    @FXML private GridPane comboGridPane;
    @FXML private GridPane entreeGridPane;
    @FXML private GridPane drinkGridPane;
    @FXML private GridPane sweetGridPane;
    @FXML private GridPane sideGridPane;

    public void initialize() {
        populateButtons(1, comboGridPane);
        populateButtons(2, entreeGridPane);
        populateButtons(3, drinkGridPane);
        populateButtons(4, sweetGridPane);
        populateButtons(5, sideGridPane);
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

    private void addItemToOrder(int id) {
        currOrder.add(id);
        currOrderNames.add(db.getItemName(id));
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

    private void populateButtons(int classID, GridPane pane) {
        try {
            List<Button> buttons = new ArrayList<Button>();
            ResultSet r = db.getClassItems(classID);
            while (r.next()) {
                Button x = new Button(r.getString(2));
                int id = r.getInt(1);
                x.setWrapText(true);
                x.setMinWidth(98);
                x.setMinHeight(28);
                x.setOnAction(e -> addItemToOrder(id));
                buttons.add(x);
            }
            pane.getChildren().clear();
            int row = 0, col = 0;
            for (Button x : buttons) {
                pane.add(x, col, row);
                col++;
                if (col == 5) {
                    col = 0;
                    row++;
                }
            }
            pane.getChildren().addAll(buttons);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
