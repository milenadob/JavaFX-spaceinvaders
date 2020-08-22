package game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class extending javaFX application class
 */
public class Main extends Application {
    /**
     * Starting application
     * @param primaryStage main application stage
     * @throws Exception exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        primaryStage.setTitle("SPACE INVADERS");
        Scene scene1 = new Scene(root, 1024, 675);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    /**
     * main method
     * @param args arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
