package views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class MainApp extends Application {
        private Stage stage;
        private Scene scene;
        @Override
        public void start(Stage primaryStage) throws Exception {
            stage=primaryStage;
            showSigninView();
            primaryStage.show();
        }
        public void showSigninView(){
            stage.setTitle("Sign");
            stage.getIcons().clear();
            //创建登录控制器对象
            SigninController signinController = (SigninController)replaceSceneContent("signin.fxml");
            //将主控制器的引用传给登录控制器对象
            signinController.setMainApp(this);
        }

        public void showForgetView() throws Exception {
            stage.setTitle("Forget");
            stage.getIcons().clear();
            //stage.getIcons().add(new Image("file:images/regist.png"));
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("forget.fxml"));
            AnchorPane ap = (AnchorPane)loader.load();
            scene = new Scene(ap);
            stage.setScene(scene);
            stage.setResizable(false);
            ForgetController forgetController = (ForgetController) loader.getController();
            System.out.println(forgetController);
            forgetController.setMainApp(this);
        }

        public void showSqlView() throws Exception {
            stage.setTitle("SQL");
            stage.getIcons().clear();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("SQLUsing.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            scene = new Scene(ap);
            stage.setScene(scene);
            stage.setResizable(false);
            SQLUsingController sqlUsingController = (SQLUsingController) loader.getController();
            System.out.println(sqlUsingController);
            sqlUsingController.setMainApp(this);
        }
        public void showWrong() throws IOException {
            Stage wstage=new Stage();
            wstage.setTitle("Wrong");
            Group root=new Group();
            Scene wscene=new Scene(root, 400, 250, Color.WHITE);

        }
        private Object replaceSceneContent(String fxmlFile) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(fxmlFile));
            AnchorPane ap = null;
            try {
                ap = (AnchorPane)loader.load();
            }catch(IOException e) { e.printStackTrace();
            }
            scene = new Scene(ap);
            stage.setScene(scene);
            stage.setResizable(false);
            return loader.getController();
        }
        public static void main(String[] args) {
            launch(args);
        }
}


