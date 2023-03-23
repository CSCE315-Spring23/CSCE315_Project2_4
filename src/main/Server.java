import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The Server class is an implementation of the JavaFX Application class that
 * launches a GUI for a server.
 */
public class Server extends Application {
	/**
	 * The start method is called when the JavaFX application is launched. It loads
	 * the GUI for the server
	 * from an FXML file, sets up the scene, and displays the stage.
	 *
	 * @param primaryStage The primary stage for this application, onto which the
	 *                     application scene can be set.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			HBox root = (HBox) FXMLLoader.load(getClass().getResource("server.fxml"));
			Scene scene = new Scene(root, 635, 430);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method launches the JavaFX application by calling the launch method
	 * with the given arguments.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
