package login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sql.Mysql;

public class signinController {
    private Mainapp mainapp;
    private Mysql mysql;
    @FXML
    private TextField account;
    @FXML
    private TextField password;
    @FXML
    private Button button1;
    @FXML
    public Button button2;

    private String accountStr;
    private String passwordStr;
    public void setMainapp(Mainapp mainapp){

    }

    public void Init() {
    }
    @FXML
    public void clickSignin(MouseEvent event) throws Exception {
        if(!mysql.login(accountStr,passwordStr)){
            mainapp.showForgetView();
        }
        else{
            mainapp.showSqlView();
        }
    }
    @FXML
    public void clickForget(MouseEvent event) throws Exception {
        mainapp.showForgetView();
    }

    @FXML
    public void getAccount(){
        accountStr=account.getText();
    }
    @FXML
    public void getPassword(){
        passwordStr=password.getText();
    }


}
