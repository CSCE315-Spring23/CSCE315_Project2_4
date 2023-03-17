<<<<<<< HEAD
=======
// package server;

>>>>>>> e3a35979c5fd412aff0ed06d644858acf7a70e95
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Manager extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            HBox root = (HBox) FXMLLoader.load(getClass().getResource("managerView.fxml"));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
