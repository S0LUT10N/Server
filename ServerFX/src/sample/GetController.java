package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GetController {

    @FXML
    public TextField idText;
    @FXML
    public TextField nameText;
    @FXML
    public Button getBtn;
    @FXML
    public RadioButton idRB;
    @FXML
    public RadioButton nameRB;

    public ToggleGroup btns = new ToggleGroup();
    public TextField savedNameText;

    @FXML
    public void initialize(){
        idRB.setToggleGroup(btns);
        nameRB.setToggleGroup(btns);
        idRB.setSelected(true);
        idRBAction();
    }

    public void idRBAction(){
        idText.setDisable(false);
        nameText.setDisable(true);
        nameText.setText("");
    }
    public void nameRBAction(){
        idText.setDisable(true);
        idText.setText("");
        nameText.setDisable(false);
    }

    public void getAction() throws IOException {
        Client client = new Client();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ResponseController.class.getResource("Response.fxml"));
        AnchorPane page = loader.load();


        String fileName = savedNameText.getText();
        String response;
        if (nameRB.isSelected()) {
            String fileServerName = nameText.getText();
            response = client.get(fileServerName, fileName);
        }
        else {
            int fileServerId = Integer.parseInt(idText.getText());
            response = client.get(fileServerId, fileName);
        }


        Stage dialogStage = new Stage();
        dialogStage.setTitle("Response");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(null);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        ResponseController controller = loader.getController();

        controller.printResponse(response);

        ((Stage) idRB.getScene().getWindow()).close();

        dialogStage.show();

    }

}
