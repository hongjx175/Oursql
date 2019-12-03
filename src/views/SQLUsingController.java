package views;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class SQLUsingController {
    private MainApp mainapp;

    public void setMainApp(MainApp mainapp){
        this.mainapp=mainapp;
    }
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab createTab;
    @FXML
    private Tab tab2;
    @FXML
    private Tab tab3;
    @FXML
    private TextField tableName;
    @FXML
    private TextField tf;
}
