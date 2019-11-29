package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Mainapp extends Application {
        private Stage stage;
        private Scene scene;
        @Override
        public void start(Stage primaryStage) throws Exception {
            stage=primaryStage;
            Parent root = FXMLLoader.load(getClass().getResource("signin.fxml"));
            stage.setTitle("OURSQL-登录");
            stage.setScene(new Scene(root, 600, 500));
            stage.show();
        }

        public void showForgetView() throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("forget.fxml"));
            stage.setTitle("Forget");
            stage.setScene(new Scene(root,1240,750));
            stage.show();
        }
        public void showSqlView(){

        }
        public static void main(String[] args) {
            launch(args);
        }
}


