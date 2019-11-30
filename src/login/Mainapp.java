package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
            stage.setTitle("Forget");
            stage.getIcons().clear();
            //stage.getIcons().add(new Image("file:images/regist.png"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Mainapp.class.getResource("login/forget.fxml"));
            BorderPane bp = (BorderPane)loader.load();
            scene = new Scene(bp);
            stage.setScene(scene);
            stage.setResizable(false);
            forgetController forController = (forgetController) loader.getController();
            System.out.println(forController);
            forController.setMainapp(this);
        }
        public void showSqlView(){

        }
        public static void main(String[] args) {
            launch(args);
        }
}


