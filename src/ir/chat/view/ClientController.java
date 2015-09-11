package ir.chat.view;

import ir.chat.Main;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class ClientController {
    @FXML
    private AnchorPane TitlePane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane ContactsPane;
    @FXML
    private BorderPane ChatPane;
    Main mainApp;
    @FXML
    private ButtonBar TitleButtonBar;
    @FXML
    private Button MaximizedButton;
    @FXML
    private TextArea MessageTextArea;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public AnchorPane getTitlePane() {
        return TitlePane;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        MessageTextArea.setWrapText(true);

        splitPane.setDividerPositions(0.4);
        splitPane.getDividers().get(0).positionProperty().unbind();
        splitPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ContactsPane.widthProperty().add(newValue.doubleValue() / 2);
                splitPane.setDividerPositions(0.4);
            }
        });
//        SplitPane.setResizableWithParent(ContactsPane, Boolean.TRUE);
//        ContactsPane.minWidthProperty().bind(splitPane.widthProperty().multiply(0.40));
//
//        ChatPane.minWidthProperty().bind(splitPane.widthProperty().multiply(0.40));
    }

    @FXML
    private void CloseButton_Clicked() {
        Platform.exit();
    }

    @FXML
    private void MaximizeButton_Clicked() {
        boolean maximized = mainApp.getPrimaryStage().isMaximized();
        mainApp.getPrimaryStage().setMaximized(!maximized);
            String image = getClass().getResource("images/restore.png").toExternalForm();
        if(maximized)
            image = getClass().getResource("images/maximize.png").toExternalForm();
        MaximizedButton.setStyle("-fx-background-image: url('" + image + "'); " +
                "    -fx-background-repeat: no-repeat;" +
                "    -fx-background-size: cover;");
    }

    @FXML
    private void MinimizeButton_Clicked() {
        mainApp.getPrimaryStage().setIconified(true);
    }
}
