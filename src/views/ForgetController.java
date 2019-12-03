package views;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class ForgetController {
    @FXML
    private ImageView icy_blue;
    @FXML
    private ImageView hjx;
    private MainApp mainapp;

    public void setMainApp(MainApp mainapp) {
        this.mainapp=mainapp;
    }



    public void clickReturn(javafx.scene.input.MouseEvent mouseEvent) {
        mainapp.showSigninView();
    }

    // @FXML
    /*private void initialize() {
        icy_blue.setImage(new Image("file:C:\\Users\\86185\\Desktop\\icy_blue.jpg"));
        hjx.setImage(new Image("file:C:\\Users\\86185\\Desktop\\hjx.jpg"));
    }*/
}
