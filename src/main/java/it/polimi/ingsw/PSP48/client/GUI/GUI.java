package it.polimi.ingsw.PSP48.client.GUI;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkIncoming;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkOutcoming;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GUI extends Application implements Runnable, ViewInterface, ClientNetworkObserver {
    @FXML
    TextField serverIP;
    @FXML
    TextField playerNickname;
    @FXML
    DatePicker birthday;
    @FXML
    ToggleButton isGameWithThreePlayers;
    @FXML
    ToggleButton isGameWithDivinities;
    @FXML
    Text errorText;
    private ClientNetworkOutcoming cA;
    private Socket server;
    private ClientNetworkIncoming cI;
    private Scene scene;
    private Parent root;
    private static Stage primaryStage;
    private Thread cIThread;
    private Thread cAThread;

    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {

    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {

    }

    @Override
    public void endgame(String messageOfEndGame) {

    }

    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {

    }

    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {

    }

    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {

    }

    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {

    }

    @Override
    public void printMessage(String s) {

    }

    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {

    }

    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {

    }

    @Override
    public void registerObserver(ViewObserver obv) {

    }

    @Override
    public void unregisterObserver(ViewObserver obv) {

    }

    @Override
    public void notifyObserver(Consumer<ViewObserver> lambda) {

    }

    @Override
    public synchronized void nicknameResult(String result) {
        System.out.println(result);
        if (result.equals("Invalid nickname. Retry")) {
            cIThread.stop();
            cAThread.stop();
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Invalid nickname");
            alert.setContentText("Nickname already in use or invalid. \n Retry");
            alert.showAndWait();
            return;
        }
        notifyAll();
    }

    @Override
    public void gameModeResult(String result) {

    }


    @Override
    public void changedBoard(ArrayList<Cell> newCells) {

    }

    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {

    }

    @Override
    public void run() {

    }

    @Override
    public synchronized void start(Stage stage) throws Exception {
        URL url = new File("src/main/resources/SantoriniGUI.fxml").toURI().toURL();
        root = FXMLLoader.load(url);
        scene = new Scene(root, 467, 653);
        primaryStage = stage;
        stage.setTitle("Santorini Log-In");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        System.out.println("Initialized stage");
    }


    public synchronized void test() throws IOException {

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
        try {
            server = new Socket(IP, 7777);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Can't connect to the server");
            alert.showAndWait();
            return;
        }


        //primaryStage.close();


        cA = new ClientNetworkOutcoming(server);
        cAThread = new Thread(cA);

        this.registerObserver(cA);

        cI = new ClientNetworkIncoming(this, server);
        cI.addObserver(this);
        cIThread = new Thread(cI);

        cIThread.start();
        cAThread.start();

        System.out.println("Correctly connected to the server!");


    }

    public synchronized void showBirthdayInput() {
        birthday.setValue(LocalDate.now());
        System.out.println("doing");
        birthday.setDisable(!isGameWithDivinities.isSelected());
    }


    public synchronized void serverConnectionError() throws IOException {
        errorText.setText("Can't connect to the server.");
        URL url = new File("src/main/resources/SantoriniGUI.fxml").toURI().toURL();
        root = FXMLLoader.load(url);
        scene = new Scene(root, 467, 653);
        primaryStage.setScene(scene);
    }


    @Override
    public void requestNicknameSend(String message) {
        if (message.equals("Invalid nickname. Retry")) {
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("Invalid nickname");
                    alert.setContentText("Nickname already in use or invalid. \n Retry");
                    alert.showAndWait();
                }
            });
            return;
        } else {
            String nickname = playerNickname.getText();
            cA.requestNicknameSend(nickname);
        }

    }

    @Override
    public synchronized void requestGameModeSend(String message) {
        int numberOfPlayers;
        if (isGameWithThreePlayers.isSelected()) numberOfPlayers = 3;
        else numberOfPlayers = 2;
        String gameMode;
        if (!isGameWithDivinities.isSelected()) gameMode = numberOfPlayers + "D";
        else {
            LocalDate ld = birthday.getValue();
            gameMode = numberOfPlayers + "ND" + " " + ld.getDayOfMonth() + "-" + ld.getMonthValue() + "-" + ld.getYear();
        }
        cA.setGameMode(gameMode);
    }

    @Override
    public synchronized void completedSetup(String message) {
        cI.completedSetup();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                primaryStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Connection completed!");
                alert.setHeaderText("Completed connection");
                alert.setContentText(message);
                alert.showAndWait();
            }
        });

    }
}
