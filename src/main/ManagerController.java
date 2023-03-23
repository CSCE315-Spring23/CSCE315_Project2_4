import java.sql.*;
import java.time.LocalDate;
import java.util.Vector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * 
 * The ManagerController class represents the controller for the manager GUI,
 * which allows a manager to view and
 * manipulate data in the restaurant's inventory and menu, and generate
 * different sales reports.
 */
public class ManagerController {
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    ObservableList<ObservableList<String>> inventoryData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> restockReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> excessReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> menuData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> salesReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> salesFrequentReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> xReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> zReportData = FXCollections.observableArrayList();

    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private DatePicker dateExcessReport;

    @FXML
    private TableView inventoryTableView;
    @FXML
    private TableView restockReportTableView;
    @FXML
    private TableView menuTableView;
    @FXML
    private TableView salesReportTableView;
    @FXML
    private TableView salesFrequentReportTableView;
    @FXML
    private TableView excessReportTableView;

    @FXML
    private TableView xReportTableView;
    @FXML
    private TableView zReportTableView;

    @FXML
    private TextField newMenuNameField;
    @FXML
    private TextField newMenuIDField;
    @FXML
    private TextField newMenuPriceField;
    @FXML
    private TextField newMenuClassField;
    @FXML
    private TextField changeMenuPriceField;
    @FXML
    private TextField inventoryRestockField;
    @FXML
    private TextField inventoryReduceField;
    @FXML
    private TextField inventoryChangeMinAmtField;

    /**
     * Initializes the inventory, restock report, and menu tables with data from the
     * database.
     */
    public void initialize() {
        setTableResult(db.getInventory(), inventoryData, inventoryTableView);
        setTableResult(db.getRestockReport(), restockReportData, restockReportTableView);
        setTableResult(db.getMenu(), menuData, menuTableView);
    }

    /**
     * Generates a sales report based on the selected start and end dates.
     * 
     * If no dates are selected or the start date is after the end date, an error
     * message is displayed.
     * 
     * @param event the ActionEvent triggered by clicking the "Generate Sales
     *              Report" button
     */
    @FXML
    private void generateSalesReport(ActionEvent event) {
        System.out.println("Manager has tried to generate a Sales Report");

        salesReportTableView.getItems().clear();
        salesReportTableView.getColumns().clear();
        Alert a = new Alert(AlertType.NONE);

        // Error handling
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            System.out.println("Error: No dates selected");
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error: No dates selected");
            a.show();
            return;
        }
        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            System.out.println("Start date is after end date");
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error: Start date is after end date");
            a.show();
            return;
        }
        if (startDatePicker.getValue().isEqual(endDatePicker.getValue())) {
            System.out.println("Start date is equal to end date");
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error: Start date is equal to end date");
            a.show();
            return;
        }

        Date startDate = Date.valueOf(startDatePicker.getValue());
        Date endDate = Date.valueOf(endDatePicker.getValue());
        setTableResult(db.getSalesReport(startDate, endDate), salesReportData, salesReportTableView);
    }

    /**
     * Generates an X Report and populates the X Report table with data from the
     * database.
     * 
     * @param event the ActionEvent triggered by clicking the "Generate X Report"
     *              button
     */
    @FXML
    private void generateXReport(ActionEvent event) {
        System.out.println("Manager has tried to generate an X Report");
        setTableResult(db.getXReport(), xReportData, xReportTableView);
    }

    /**
     * Generates a Z Report and populates the Z Report table with data from the
     * database.
     * 
     * @param event the ActionEvent triggered by clicking the "Generate Z Report"
     *              button
     */
    @FXML
    private void generateZReport(ActionEvent event) {
        System.out.println("Manager has tried to generate a Z Report");
        setTableResult(db.getXReport(), zReportData, zReportTableView);
    }

    /**
     * Clears the excess report table view and sets it with new data from the
     * database based on the selected date.
     * 
     * @param event The event triggered by the user clicking the "Get Report"
     *              button.
     */
    @FXML
    private void getDateExcessReport(ActionEvent event) {
        excessReportTableView.getItems().clear();
        excessReportTableView.getColumns().clear();
        Node node = (Node) event.getSource();
        LocalDate date = dateExcessReport.getValue();
        System.out.println("Get date " + date.toString() + " 00:00");
        setTableResult(db.getExcessReport(date.toString() + " 00:00:00"), excessReportData, excessReportTableView);
    }

    /**
     * Clears the restock report table view and sets it with new data from the
     * database.
     * 
     * @param event The event triggered by the user clicking the "Refresh" button.
     */
    @FXML
    private void refreshRestockReport(ActionEvent event) {
        restockReportTableView.getItems().clear();
        restockReportTableView.getColumns().clear();
        setTableResult(db.getRestockReport(), restockReportData, restockReportTableView);
    }

    /**
     * Increases the quantity of the selected inventory item and updates the
     * inventory table view accordingly.
     * 
     * @param event The event triggered by the user clicking the "Restock" button.
     */
    @FXML
    private void restockSelectedInventory(ActionEvent event) {
        try {
            int qty = 0;
            Object rowDataObj = inventoryTableView.getSelectionModel().getSelectedItems().get(0);
            String rowDataStr = rowDataObj.toString();
            String idStr = rowDataStr.substring(1, rowDataStr.indexOf(','));
            int id = Integer.parseInt(idStr);
            String text = inventoryRestockField.getText();
            if (!text.isEmpty()) {
                qty = Integer.parseInt(text);
                if (qty < 0) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Quantity cannot be less than 0!");
                    a.show();
                } else {
                    db.addInventory(id, qty);
                    refreshInventoryTable();
                    inventoryRestockField.setText("");
                }
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Quantity must be a positive integer!");
                a.show();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Decreases the quantity of the selected inventory item and updates the
     * inventory table view accordingly.
     * 
     * @param event The event triggered by the user clicking the "Reduce" button.
     */
    @FXML
    private void reduceSelectedInventory(ActionEvent event) {
        try {
            int qty = 0;
            Object rowDataObj = inventoryTableView.getSelectionModel().getSelectedItems().get(0);
            String rowDataStr = rowDataObj.toString();
            String idStr = rowDataStr.substring(1, rowDataStr.indexOf(','));
            int id = Integer.parseInt(idStr);
            String text = inventoryReduceField.getText();
            if (!text.isEmpty()) {
                qty = Integer.parseInt(text);
                if (qty < 0) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Quantity cannot be less than 0!");
                    a.show();
                } else {
                    db.subtractInventory(id, qty);
                    refreshInventoryTable();
                    inventoryReduceField.setText("");
                }
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Quantity must be a positive integer!");
                a.show();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles the action event for changing the minimum amount of an ingredient in
     * the inventory table.
     * Parses the selected row's ID from the table and the input minimum amount from
     * the corresponding field,
     * validates the input, and updates the database accordingly.
     * If input is invalid, displays an error message.
     * 
     * @param event the ActionEvent triggered by the user clicking the corresponding
     *              button
     */
    @FXML
    private void inventoryChangeMinAmt(ActionEvent event) {
        try {
            int minAmt = 0;
            Object rowDataObj = inventoryTableView.getSelectionModel().getSelectedItems().get(0);
            String rowDataStr = rowDataObj.toString();
            String idStr = rowDataStr.substring(1, rowDataStr.indexOf(','));
            int id = Integer.parseInt(idStr);
            String text = inventoryChangeMinAmtField.getText();
            if (!text.isEmpty()) {
                minAmt = Integer.parseInt(text);
                if (minAmt < 0) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Minimum Amount cannot be less than 0!");
                    a.show();
                } else {
                    db.updateIngredientMinAmt(id, minAmt);
                    refreshInventoryTable();
                    inventoryChangeMinAmtField.setText("");
                }
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Minimum Amount must be a positive integer!");
                a.show();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Generates a sales report of what frequently bought together as pairs based on
     * the selected start and end dates.
     * 
     * If no dates are selected or the start date is after the end date, an error
     * message is displayed.
     * 
     * @param event the ActionEvent triggered by clicking the "Generate Frequent
     *              Sales Report" button
     */
    @FXML
    private void generateFrequentSalesReport(ActionEvent event) {
        System.out.println("Manager has tried to generate a Sales Report");

        salesFrequentReportTableView.getItems().clear();
        salesFrequentReportTableView.getColumns().clear();
        Alert a = new Alert(AlertType.NONE);

        // Error handling
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            System.out.println("Error: No dates selected");
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error: No dates selected");
            a.show();
            return;
        }
        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            System.out.println("Start date is after end date");
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error: Start date is after end date");
            a.show();
            return;
        }
        if (startDatePicker.getValue().isEqual(endDatePicker.getValue())) {
            System.out.println("Start date is equal to end date");
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Error: Start date is equal to end date");
            a.show();
            return;
        }

        Date startDate = Date.valueOf(startDatePicker.getValue());
        Date endDate = Date.valueOf(endDatePicker.getValue());
        setTableResult(db.getSalesReport(startDate, endDate), salesFrequentReportData, salesFrequentReportTableView);
    }

    /**
     * Handles the action event for adding a new menu item to the menu table.
     * Parses the input name, ID, class, and price from the corresponding fields,
     * validates the input, and adds the new menu item to the database.
     * If input is invalid, displays an error message.
     * 
     * @param event the ActionEvent triggered by the user clicking the corresponding
     *              button
     */
    @FXML
    private void addMenuItem(ActionEvent event) {
        try {
            Alert a = new Alert(AlertType.ERROR);
            String name = "";
            int id = -1;
            int classId = -1;
            float price = -1.0f;

            String text = newMenuNameField.getText();
            if (text.isEmpty()) {
                a.setContentText("'Name' cannot be blank!");
                a.show();
                return;
            } else {
                name = text;
            }
            text = newMenuIDField.getText();
            if (text.isEmpty()) {
                a.setContentText("'ID' cannot be blank!");
                a.show();
                return;
            } else {
                id = Integer.parseInt(text);
            }
            text = newMenuClassField.getText();
            if (text.isEmpty()) {
                a.setContentText("'Class' cannot be blank!");
                a.show();
                return;
            } else {
                classId = Integer.parseInt(text);
            }
            text = newMenuPriceField.getText();
            if (text.isEmpty()) {
                a.setContentText("'Price' cannot be blank!");
                a.show();
                return;
            } else {
                price = jdbcpostgreSQL.round(Float.parseFloat(text), 2);
            }

            if (db.isMenuIdValid(id) && isClassIdValid(classId) && price >= 0) {
                db.addMenuItem(id, name, price, classId);
                refreshMenuTable(event);
                newMenuNameField.setText("");
                newMenuIDField.setText("");
                newMenuClassField.setText("");
                newMenuPriceField.setText("");
            } else {
                // throw popup explianing invalid criteria
                if (!db.isMenuIdValid(id)) {
                    a.setContentText(id + " is already used for an existing Menu Item!");
                } else if (!isClassIdValid(classId)) {
                    a.setContentText("Class must be an ingeter from 1 to 5!");
                } else if (price < 0) {
                    a.setContentText("Price cannot be less than 0!");
                }
                a.show();
            }
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Name, Id, Class, and Price cannot be blank!");
                a.show();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Handles the action event for changing the price of a menu item in the menu
     * table.
     * Parses the selected row's ID from the table and the input price from the
     * corresponding field,
     * validates the input, and updates the database accordingly.
     * If input is invalid, displays an error message.
     * 
     * @param event the ActionEvent triggered by the user clicking the corresponding
     *              button
     */
    @FXML
    private void menuChangePrice(ActionEvent event) {
        try {
            float price = 0;
            Object rowDataObj = menuTableView.getSelectionModel().getSelectedItems().get(0);
            String rowDataStr = rowDataObj.toString();
            String idStr = rowDataStr.substring(1, rowDataStr.indexOf(','));
            int id = Integer.parseInt(idStr);
            String text = changeMenuPriceField.getText();
            if (!text.isEmpty()) {
                price = jdbcpostgreSQL.round(Float.parseFloat(text), 2);
                if (price < 0) {
                    Alert a = new Alert(AlertType.ERROR);
                    a.setContentText("Price cannot be less than 0!");
                    a.show();
                } else {
                    db.updateMenuItemPrice(id, price);
                    refreshMenuTable(event);
                    changeMenuPriceField.setText("");
                }
            }
        } catch (Exception e) {
            if (e instanceof NumberFormatException) {
                Alert a = new Alert(AlertType.ERROR);
                a.setContentText("Minimum Amount must be a positive integer!");
                a.show();
            } else {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Deletes the selected menu item from the table and database
     * 
     * @param event ActionEvent triggered by delete button
     */
    @FXML
    private void deleteSelectedMenuItem(ActionEvent event) {
        Object rowDataObj = menuTableView.getSelectionModel().getSelectedItems().get(0);
        String rowDataStr = rowDataObj.toString();
        String idStr = rowDataStr.substring(1, rowDataStr.indexOf(','));
        int id = Integer.parseInt(idStr);
        System.out.println("MenuItemId = " + id);
        db.deleteMenuItem(id);
        refreshMenuTable(event);
        menuTableView.getSelectionModel().clearSelection();
    }

    /**
     * Refreshes the menu table with updated data from the database
     * 
     * @param event ActionEvent triggered by refresh button
     */
    @FXML
    private void refreshMenuTable(ActionEvent event) {
        menuTableView.getItems().clear();
        menuTableView.getColumns().clear();
        setTableResult(db.getMenu(), menuData, menuTableView);
    }

    /**
     * Opens the server view
     * 
     * @param event ActionEvent triggered by open server button
     */
    @FXML
    private void openServerView(ActionEvent event) {
        System.out.println("Manager has tried to open the Server View");
        try {
            Process theProcess = Runtime.getRuntime().exec(
                    "java --module-path /Users/lwilber/Downloads/javafx-sdk-19.0.2.1/lib --add-modules javafx.controls,javafx.graphics,javafx.media,javafx.fxml Server");
            System.out.println("Server View Opened Sucessfully");
        } catch (Exception e) {
            System.err.println("Failed to open Server View");
            e.printStackTrace();
        }
    }

    /**
     * Sets the result of a query to a table view
     * 
     * @param r         ResultSet containing the query results
     * @param tableData ObservableList containing the table data
     * @param table     TableView to display the data
     */
    private void setTableResult(ResultSet r, ObservableList<ObservableList<String>> tableData, TableView table) {
        try {
            int numCols = r.getMetaData().getColumnCount();
            for (int i = 0; i < numCols; i++) {
                final int j = i;
                String colName = r.getMetaData().getColumnName(j + 1);
                TableColumn newCol = new TableColumn(colName);
                newCol.setCellValueFactory(
                        new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                            public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                                return new SimpleStringProperty(param.getValue().get(j).toString());
                            }
                        });
                table.getColumns().addAll(newCol);
            }
            if (table.getColumns().get(0) == "") {
                table.getColumns().remove(0);
            }

            while (r.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= numCols; i++) {
                    // Iterate Column
                    row.add(r.getString(i));
                }
                tableData.add(row);
            }
            table.setItems(tableData);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the inventory table by clearing the current items and columns and
     * setting new data obtained from the database.
     */
    private void refreshInventoryTable() {
        inventoryTableView.getItems().clear();
        inventoryTableView.getColumns().clear();
        setTableResult(db.getInventory(), inventoryData, inventoryTableView);
    }

    /**
     * Checks if the given class ID is valid by ensuring it is between 1 and 5
     * inclusive.
     *
     * @param classId The class ID to check.
     * @return True if the class ID is valid, false otherwise.
     */
    private boolean isClassIdValid(int classId) {
        if (classId < 6 && classId > 0) {
            return true;
        }
        return false;
    }
}
