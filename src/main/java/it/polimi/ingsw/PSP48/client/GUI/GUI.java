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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GUI extends Application implements ClientNetworkObserver, Runnable, ViewInterface {
    @FXML
    private TextField serverIP;
    @FXML
    private TextField playerNickname;
    @FXML
    private DatePicker birthday;
    @FXML
    private ToggleButton isGameWithThreePlayers;
    @FXML
    private ToggleButton isGameWithDivinities;
    @FXML
    private Text errorText;
    @FXML
    private Text gameMessage;
    @FXML
    private GridPane boardPane;
    @FXML
    private GridPane playersPane;
    @FXML
    private Pagination divinityList;
    private static ClientNetworkOutcoming cA;
    private static Socket server;
    private ClientNetworkIncoming cI;
    private Scene scene;
    private Parent root;
    private static Stage primaryStage;
    private Thread cIThread;
    private Thread cAThread;
    private static ArrayList<ViewObserver> observers = new ArrayList<>();


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
        ArrayList<String> selectedDivinities;
        int selected = 0;


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                GUI newController;
                Parent root = null;
                Stage divinityRequestStage = new Stage();
                FXMLLoader divinityRequestLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
                try {
                    root = divinityRequestLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newController = divinityRequestLoader.getController();
                System.out.println("new controller: " + newController);
                Scene divinitySelectionRequest = new Scene(root, 315, 640);
                divinityRequestStage.setScene(divinitySelectionRequest);
                divinityRequestStage.setResizable(false);
                divinityRequestStage.initModality(Modality.WINDOW_MODAL);
                divinityRequestStage.initOwner(primaryStage);
                //now i must parse the arrived divinities

                newController.divinityList.setMaxPageIndicatorCount(0);
                ArrayList<String> divinities = new ArrayList<>();

                for (DivinitiesWithDescription d : div) {
                    divinities.add(d.getName());
                }
                newController.divinityList.setPageFactory(n -> new ImageView("santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinities.get(n) + ".png"));

                divinityRequestStage.showAndWait();
            }
        });


    }

    @Override
    public void printMessage(String s) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameMessage.setText(s);
            }
        });
    }

    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {

    }

    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {

    }

    @Override
    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    @Override
    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    @Override
    public void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver o : observers) {
            lambda.accept(o);
        }
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
        System.out.println("Invoked on " + this);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameMessage.setText("Prova");
                int i = 0;
                for (String s : newPlayerList) {
                    ((Text) getNodeFromGridPane(playersPane, 2, i)).setText(s);
                    i = i + 2;
                }
            }
        });

    }

    @Override
    public void run() {

    }

    @Override
    public synchronized void start(Stage stage) throws Exception {
        System.out.println(this);
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


    public synchronized void loginButton() throws IOException {

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
        cI.removeObserver(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cI.addObserver(loader.getController());
        cI.setPlayerView(loader.getController());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                scene = new Scene(root, 1155, 825);
                primaryStage.setScene(scene);
                primaryStage.setTitle("Santorini");
                primaryStage.show();
            }
        });
    }

    public void initialize() {

    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

}
