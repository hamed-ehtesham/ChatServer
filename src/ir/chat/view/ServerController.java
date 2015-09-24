package ir.chat.view;

import ir.chat.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mohammad Amin on 10/09/2015.
 */
public class ServerController {
    @FXML
    private Button closeButton;
    @FXML
    private Pane movePane;
    @FXML
    private TextArea log;
    @FXML
    private TextField loginPort;
    @FXML
    private TextField registeryPort;
    @FXML
    private TextField messagePort;
    @FXML
    private TextField usernameDB;
    @FXML
    private TextField passwordDB;
    @FXML
    private TextField nameDB;
    @FXML
    private Label onlineUser;
    @FXML
    private Label registeredUser;
    @FXML
    private Button runServer;


    @FXML
    private void initialize() {
        log.setEditable(false);
    }

    public Pane getMovePane() {
        return movePane;
    }

    @FXML
    private void CloseButton_Clicked() {
        Platform.exit();
    }

}


