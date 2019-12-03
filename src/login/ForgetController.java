package login;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class ForgetController {

    @FXML
    private ImageView icy_blue;
    @FXML
    private ImageView hjx;
    private MainApp mainapp;

    public void setMainapp(MainApp mainapp) {
        this.mainapp = mainapp;
    }

    // @FXML
    /*private void initialize() {
        icy_blue.setImage(new Image("file:C:\\Users\\86185\\Desktop\\icy_blue.jpg"));
        hjx.setImage(new Image("file:C:\\Users\\86185\\Desktop\\hjx.jpg"));
    }*/
}
