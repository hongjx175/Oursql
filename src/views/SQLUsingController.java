package views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SQLUsingController {

    private MainApp mainapp;

    public void setMainApp(MainApp mainapp) {
        this.mainapp = mainapp;
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
    private TextField columnNum;
    @FXML
    private VBox vbox;
    @FXML
    private Button confirm;
    @FXML
    private ScrollPane sc;

    @FXML
    public void getColNum(MouseEvent event) {
        int num = Integer.parseInt(columnNum.getText());

        mainapp.showCreateView(num, vbox);
    }

}
