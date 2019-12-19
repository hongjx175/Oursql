package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Register {

    private MainApp mainapp;
    private String nameStr, passwordStr, password2Str;
    @FXML
    private TextField name;
    @FXML
    private TextField password;
    @FXML
    private TextField password2;
    @FXML
    private Button confirm;
    @FXML
    private Label out;

    public void getInfo() {
        nameStr = name.getText();
        passwordStr = password.getText();
        password2Str = password2.getText();
    }

    public void setMainApp(MainApp mainapp) {
        this.mainapp = mainapp;
    }

    public boolean test1(String str) {

        return str.matches("^.*[a-zA-Z]+.*$")
            && str.matches("^.*[0-9]+.*$")
            && str.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$");
        //包含数字、字母、特殊字符（包括^$./,;:’!@#%&*|?+()[]{}） 三种
    }

    public boolean test2(String str) {
        return str.matches("^.{8,}$");//不少于8位
    }

    public boolean test3(String str) {
        return !str.matches("^.*[\\s]+.*$");//不能包含空格、制表符、换页符等空白字符
    }

    @FXML
    public void Confirm(MouseEvent event) throws Exception {
        this.getInfo();
        if (!passwordStr.equals(password2Str)) {
        }
        boolean can1 = test1(passwordStr);
        boolean can2 = test2(passwordStr);
        boolean can3 = test3(passwordStr);
        if (!(can1 && can2 && can3)) {
            if (!can1) {

            }
            if (!can2) {

            }
            if (!can3) {

            }
        }

    }
}
