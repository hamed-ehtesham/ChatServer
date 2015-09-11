package ir.chat.view.signup;

import ir.chat.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mohammad Amin on 10/09/2015.
 */
public class SignUpController {
    @FXML
    private Button closeButton;
    @FXML
    private Pane movePane;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField emailAddress;

    @FXML
    private void initialize() {
    }

    public Pane getMovePane() {
        return movePane;
    }

    @FXML
    private void CloseButton_Clicked() {
        Platform.exit();
    }

    @FXML
    private void validateEmail() {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        matcher = pattern.matcher(emailAddress.getText());
        if(!matcher.matches()){
           // emailAddress.setText("ridi");
            emailAddress.setStyle("-fx-background-color: #F75D59");
        }else{
            emailAddress.setStyle("-fx-background-color: #98FF98");
        }
    }

    @FXML
    private void validatePhoneNumber(){
        if (!phoneNumber.getText().matches("^[0-9]{10}$")){
            phoneNumber.setStyle("-fx-background-color: #F75D59");
        }else{
            phoneNumber.setStyle("-fx-background-color: #98FF98");
        }

    }
}


