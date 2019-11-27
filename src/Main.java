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
            URL location = getClass().getResource("oursql.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load();

            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 300, 275));
            Controller controller=fxmlLoader.getController();
            controller.Init();
            primaryStage.show();
        }


        public static void main(String[] args) {
            launch(args);
        }
}


