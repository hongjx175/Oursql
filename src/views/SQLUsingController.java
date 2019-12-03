package views;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    private TextField columnNum;
    @FXML
    private VBox vbox;
    @FXML
    private Button confirm;
    @FXML
    private ScrollPane sc;
    @FXML
    public void addColumn(MouseEvent event) {
        mainapp.showCreateView(vbox);
    }

}
