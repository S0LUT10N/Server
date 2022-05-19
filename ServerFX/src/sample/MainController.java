package sample;

import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainController {

    public Button getBtn;
    public Button saveBtn;
    public Button deleteBtn;

    public void getBtnAction() throws IOException {
        Stage getStage = new Stage();
        getStage.setTitle("Get");
        Parent root = FXMLLoader.load(getClass().getResource("Get.fxml"));
        Scene getScene = new Scene(root);
        getStage.setScene(getScene);

        ((Stage) getBtn.getScene().getWindow()).close();

        getStage.show();
    }

    public void saveBtnAction() throws IOException {
        Stage saveStage = new Stage();
        saveStage.setTitle("Save");
        Parent root = FXMLLoader.load(getClass().getResource("Save.fxml"));
        Scene saveScene = new Scene(root);
        saveStage.setScene(saveScene);

        ((Stage) saveBtn.getScene().getWindow()).close();

        saveStage.show();
    }

    public void deleteBtnAction() throws IOException {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("Delete");
        Parent root = FXMLLoader.load(getClass().getResource("Delete.fxml"));
        Scene deleteScene = new Scene(root);
        deleteStage.setScene(deleteScene);

        ((Stage) deleteBtn.getScene().getWindow()).close();

        deleteStage.show();
    }

}
