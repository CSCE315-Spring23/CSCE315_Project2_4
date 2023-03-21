import java.util.Vector;
import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.collections.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;

public class ManagerController {
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    ObservableList<ObservableList<String>> inventoryData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> restockReportData = FXCollections.observableArrayList();
    ObservableList<ObservableList<String>> menuData = FXCollections.observableArrayList();
    @FXML
    private TableView inventoryTableView;
    @FXML
    private TableView restockReportTableView;
    @FXML
    private TableView menuTableView;

    public void initialize() {
        setTableResult(db.getInventory(), inventoryData, inventoryTableView);
        setTableResult(db.getRestockReport(), restockReportData, restockReportTableView);
        setTableResult(db.getMenu(), menuData, menuTableView);
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
            table.getColumns().remove(0);

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
        }
    }

}
