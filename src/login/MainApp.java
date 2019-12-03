package login;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MainApp extends Application {

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        showSigninView();
        primaryStage.show();
    }

    public void showSigninView() {
        stage.setTitle("Sign");
        stage.getIcons().clear();
        //创建登录控制器对象
        SignInController signinController = (SignInController) replaceSceneContent("signin.fxml");
        //将主控制器的引用传给登录控制器对象
        signinController.setMainApp(this);
    }

    public void showForgetView() throws Exception {
        stage.setTitle("Forget");
        stage.getIcons().clear();
        //stage.getIcons().add(new Image("file:images/regist.png"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("forget.fxml"));
        AnchorPane bp = (AnchorPane) loader.load();
        scene = new Scene(bp);
        stage.setScene(scene);
        stage.setResizable(false);
        ForgetController forgetController = (ForgetController) loader.getController();
        System.out.println(forgetController);
        forgetController.setMainapp(this);
    }

    public void showSqlView() {

    }

    private Object replaceSceneContent(String fxmlFile) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource(fxmlFile));
        AnchorPane ap = null;
        try {
            ap = (AnchorPane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
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


