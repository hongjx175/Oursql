package views;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
        //scene = new Scene();
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


    public void showCreateView(VBox vb, int num) {
        TextField name = new TextField();
        TextField length = new TextField();
        HBox hb = new HBox();
        vb.getChildren().add(new Label("第" + num + "列"));

        hb.getChildren().addAll(new Label("列名: "), name);
        vb.getChildren().add(hb);//列名

        hb = new HBox();
        hb.getChildren().addAll(new Label("长度: "), length);
        vb.getChildren().add(hb);//长度

        //下面是类型选择
        vb.getChildren().add(new Label("type:"));

        ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("int");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("String");
        rb2.setToggleGroup(group);
        rb2.setSelected(true);

        RadioButton rb3 = new RadioButton("identity number");
        rb3.setToggleGroup(group);
        rb3.setSelected(true);

        RadioButton rb4 = new RadioButton("date");
        rb4.setToggleGroup(group);
        rb4.setSelected(true);

        RadioButton rb5 = new RadioButton("time");
        rb5.setToggleGroup(group);
        rb5.setSelected(true);

        RadioButton rb6 = new RadioButton("numbers");
        rb6.setToggleGroup(group);
        rb6.setSelected(true);

        hb = new HBox();
        hb.getChildren().addAll(rb1, rb2, rb3, rb4, rb5, rb6);
        vb.getChildren().add(hb);
        vb.getChildren().add(new Label());
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


