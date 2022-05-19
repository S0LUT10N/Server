package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SaveController {

    public TextField fileNameText;
    public TextField fileServerNameText;

    public void saveAction() throws IOException {
        Client client = new Client();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResponseController.class.getResource("Response.fxml"));
        AnchorPane page = loader.load();

        String fileName = fileNameText.getText();
        String fileServerName = fileServerNameText.getText();


        String response = client.save(fileName, fileServerName);


        Stage dialogStage = new Stage();
        dialogStage.setTitle("Response");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(null);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        ResponseController controller = loader.getController();

        controller.printResponse(response);

        ((Stage) fileNameText.getScene().getWindow()).close();

        dialogStage.show();

    }
}
