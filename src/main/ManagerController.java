import java.util.Vector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.collections.*;

public class ManagerController {
    private jdbcpostgreSQL db = new jdbcpostgreSQL();
    public void initialize() {
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

    private void restockReport() {
    Vector<String> restockTable = db.getStockReport();
}

}
