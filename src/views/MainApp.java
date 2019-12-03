package views;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage, wstage;
    private Scene scene;
    private SQLUsingController sqlUsingController;

    // boolean entered=false;
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
        SigninController signinController = (SigninController) replaceSceneContent("signin.fxml");
        //将主控制器的引用传给登录控制器对象
        signinController.setMainApp(this);
    }

    public void showForgetView() throws Exception {
        stage.setTitle("Forget");
        stage.getIcons().clear();
        //stage.getIcons().add(new Image("file:images/regist.png"));
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("forget.fxml"));
        AnchorPane ap = (AnchorPane) loader.load();
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
        //scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.setResizable(false);
        sqlUsingController = (SQLUsingController) loader.getController();
        System.out.println(sqlUsingController);
        sqlUsingController.setMainApp(this);
    }

    public void showCreateView(int num, VBox vb) {
        for (int i = 0; i < num; i++) {
            vb.getChildren().add(new TextField("" + "name:"));
            //rootrootvb.getChildren().add(new )
            vb.getChildren().add(new TextField("" + "length:"));
        }
    }

    public void showWrong() {
        wstage = new Stage();
        wstage.setTitle("Wrong");
        wstage.setWidth(400);
        wstage.setHeight(400);
        BorderPane bp = new BorderPane();
        Scene wscene = new Scene(bp);
        Text inform = new Text("账户或密码错误");
        bp.setCenter(inform);
        wstage.setScene(wscene);
        wstage.show();
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
        //scene.setFill(Color.BLUE);
        stage.setScene(scene);
        stage.setResizable(false);
        return loader.getController();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


