package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.util.*;
public class Controller {
    Scanner scan=new Scanner(System.in);
    @FXML
    public TextField account;
    @FXML
    public TextField password;
    @FXML
    public Button button1;
    @FXML
    public Button button2;
    @FXML
    public Button button3;
    @FXML
    public Button button4;
    @FXML
    public Button button5;

    public void Init() {
    }
    @FXML
    public void onButtonClick(ActionEvent event){

    }
    public void Input(){
        String accountStr=scan.nextLine();
    }

}
