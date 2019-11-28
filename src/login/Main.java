package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("oursql.fxml"));
            primaryStage.setTitle("OURSQL-登录");
            primaryStage.setScene(new Scene(root, 600, 500));
            primaryStage.show();
        }


        public static void main(String[] args) {
            launch(args);
        }
}


