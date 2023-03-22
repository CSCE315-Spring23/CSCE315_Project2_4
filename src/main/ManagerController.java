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
import javafx.scene.control.Alert.AlertType;

public class ManagerController {
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    ObservableList<ObservableList<String>> inventoryData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> restockReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> excessReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> menuData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> salesReportData = FXCollections.observableArrayList();
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

    public void initialize() {
        setTableResult(db.getInventory(), inventoryData, inventoryTableView);
        setTableResult(db.getRestockReport(), restockReportData, restockReportTableView);
        setTableResult(db.getMenu(), menuData, menuTableView);
        // db.getSalesReport(null, null);
    }

    @FXML
    private void getDateExcessReport(ActionEvent event) {
        excessReportTableView.getItems().clear();
        excessReportTableView.getColumns().clear();
        Node node = (Node) event.getSource();
        LocalDate date = dateExcessReport.getValue();
        System.out.println("Get date " + date.toString() + " 00:00");
        setTableResult(db.getExcessReport(date.toString() + " 00:00:00"), excessReportData, excessReportTableView);
    }

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

    @FXML
    private void generateXReport(ActionEvent event) {
        System.out.println("Manager has tried to generate an X Report");
        setTableResult(db.getXReport(), xReportData, xReportTableView);
    }

    @FXML
    private void generateZReport(ActionEvent event) {
        System.out.println("Manager has tried to generate a Z Report");
        setTableResult(db.getXReport(), zReportData, zReportTableView);
    }

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

    @FXML
    void deleteSelectedMenuItem(ActionEvent event) {
        Object rowDataObj = menuTableView.getSelectionModel().getSelectedItems().get(0);
        String rowDataStr = rowDataObj.toString();
        String idStr = rowDataStr.substring(1, rowDataStr.indexOf(','));
        int id = Integer.parseInt(idStr);
        System.out.println("MenuItemId = " + id);
        db.deleteMenuItem(id);
        refreshMenuTable(event);
    }

    @FXML
    void refreshMenuTable(ActionEvent event) {
        menuTableView.getItems().clear();
        menuTableView.getColumns().clear();
        setTableResult(db.getMenu(), menuData, menuTableView);
    }

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

    private boolean isClassIdValid(int classId) {
        if (classId < 6 && classId > 0) {
            return true;
        }
        return false;
    }
}
