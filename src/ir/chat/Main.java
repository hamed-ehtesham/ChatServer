package ir.chat;

import ir.chat.util.RSAEncrypt;
import ir.chat.util.Undecorator;
import ir.chat.view.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
    Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {


        this.primaryStage = primaryStage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/server.fxml"));
        Parent root = loader.load();
        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:src/ir/chat/view/images/communication_bubble_message_chat_messenger_internet-512.png"));
        primaryStage.setTitle("Telepaty");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        ServerController controller = loader.getController();
        primaryStage.sizeToScene();
        Undecorator undecorator = Undecorator.UndecoratorFactory(primaryStage, 4, controller.getMovePane(), false);
        primaryStage.show();


    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
