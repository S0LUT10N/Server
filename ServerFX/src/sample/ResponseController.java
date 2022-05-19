package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ResponseController {

    public Label response;

    public void printResponse(String Response) {
        response.setText(Response);
    }
}

