package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.client.GUI.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.io.IOException;
import java.time.LocalDate;

public class LoginScreenController {
    @FXML
    private ToggleButton isGameWithDivinities;
    @FXML
    private ToggleButton isGameWithThreePlayers;
    @FXML
    private TextField playerNickname;
    @FXML
    private TextField serverIP;
    @FXML
    private DatePicker birthday;

    private final GUI View;

    public LoginScreenController(GUI view) {
        View = view;
    }


    public synchronized void requestGameModeSend() {
        int numberOfPlayers;
        if (isGameWithThreePlayers.isSelected()) numberOfPlayers = 3;
        else numberOfPlayers = 2;
        String gameMode;
        if (isGameWithDivinities.isSelected()) gameMode = numberOfPlayers + "D";
        else {
            LocalDate ld = birthday.getValue();
            gameMode = numberOfPlayers + "ND" + " " + ld.getDayOfMonth() + "-" + ld.getMonthValue() + "-" + ld.getYear();
        }
        View.setNumberOfPlayers(numberOfPlayers);
        View.getUploader().setGameMode(gameMode);
    }


    public synchronized void nicknameResult(String result) {
        System.out.println(result);

    }


    public void requestNicknameSend() {
        String nickname = playerNickname.getText();
        View.getUploader().requestNicknameSend(nickname);
    }

    public synchronized void loginButton() {
        if (serverIP.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Missing Server IP. Insert a valid IP server.");
            alert.showAndWait();
            return;
        }


        if (playerNickname.getText().length() == 0 || playerNickname.getText().contains(".")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Insert a valid nickname");
            alert.showAndWait();
            return;
        }

        String IP = serverIP.getText();

        View.startNetwork(IP);
    }

    public void divinitiesButton() {
        birthday.setDisable(!birthday.isDisable());
        birthday.setValue(LocalDate.now());
    }


}
