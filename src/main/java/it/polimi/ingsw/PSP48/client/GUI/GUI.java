package it.polimi.ingsw.PSP48.client.GUI;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.DivinityChoiceController;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.FirstPlayerSelectionController;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.PositioningController;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
    private Pane multifunctionalPane;
    @FXML
    private ImageView thirdPlayerCard;
    @FXML
    private ImageView thirdPlayerBg;
    private static ClientNetworkOutcoming cA;
    private static Socket server;
    private ClientNetworkIncoming cI;
    private Scene scene;
    private Parent root;
    private static Stage primaryStage;
    private Thread cIThread;
    private Thread cAThread;
    private static ArrayList<ViewObserver> observers = new ArrayList<>();
    private static int playersInGame;


    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {

    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {

    }

    @Override
    public void endgame(String messageOfEndGame) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cA.shutDown();
                primaryStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("End of game");
                alert.setHeaderText("Oh! The game finished!");
                alert.setContentText(messageOfEndGame);
                alert.showAndWait();
            }
        });

    }

    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
        final GUI thisController = this;
        if (availableDivinities.size() == 1)
            notifyObserver((x) -> x.registerPlayerDivinity(availableDivinities.get(0).getName()));
        else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Pane selectionPane = null;
                    divinitiesSelectorLoader.setController(new DivinityChoiceController(availableDivinities, 1, thisController));
                    try {
                        selectionPane = divinitiesSelectorLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gameMessage.setText("Select your divinity in the selector at your right.");
                    multifunctionalPane.getChildren().add(selectionPane);
                }
            });
        }
    }

    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/firstPlayerSelection.fxml"));
        final GUI thisController = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Pane selectionPane = null;
                divinitiesSelectorLoader.setController(new FirstPlayerSelectionController(players.size(), thisController, players));
                try {
                    selectionPane = divinitiesSelectorLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gameMessage.setText("Select the first player");
                multifunctionalPane.getChildren().add(selectionPane);
            }
        });
    }

    /**
     * method handling the choice of the positions of the workers on the board
     * @param validCells is the list of valid positions to put the worker on the board
     */
    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells)
    {
        final FXMLLoader positioningLoader=new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
        final GUI controller = this; //outer controller to communicate with the scene controller
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Pane positioningPane=null;
                positioningLoader.setController(new PositioningController(validCells, controller));
                try {
                    positioningPane = positioningLoader.load();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                multifunctionalPane.getChildren().add(positioningPane);
            }
        });
    }

    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
        final GUI thisController = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Pane selectionPane = null;
                divinitiesSelectorLoader.setController(new DivinityChoiceController(div, playersInGame, thisController));
                try {
                    selectionPane = divinitiesSelectorLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gameMessage.setText("Select divinities in game in the selector at your right.");
                multifunctionalPane.getChildren().add(selectionPane);
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
                if (playersInGame == 2) {
                    thirdPlayerBg.setVisible(false);
                    thirdPlayerCard.setVisible(false);
                    ((Text) getNodeFromGridPane(playersPane, 2, 4)).setVisible(false);
                }
                //first of all, I need to reset all text fields and images
                for (int i = 0; i < 5; i = i + 2) {
                    ((Text) getNodeFromGridPane(playersPane, 2, i)).setText("Waiting for Players");
                    ((ImageView) getNodeFromGridPane(playersPane, 0, 4)).setImage(null);
                }
                int i = 0;
                for (String s : newPlayerList) {
                    //first I need to parse the arrived strings
                    String name = s.split("\\.")[0];
                    String colour = s.split("\\.")[1];
                    String divinity = s.split("\\.")[2];
                    //now i need to set divinity image
                    if (!divinity.equals("Divinity Not Chosen") && !divinity.equals("Base Divinity")) {
                        ((ImageView) getNodeFromGridPane(playersPane, 0, i)).setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinity + ".png"));
                    }
                    ((Text) getNodeFromGridPane(playersPane, 2, i)).setText(name + "\n" + divinity);
                    switch (colour) {
                        case "GREY":
                            ((Text) getNodeFromGridPane(playersPane, 2, i)).setFill(Color.GREY);
                            break;
                        case "BLUE":
                            ((Text) getNodeFromGridPane(playersPane, 2, i)).setFill(Color.BLUE);
                            break;
                        case "WHITE":
                            ((Text) getNodeFromGridPane(playersPane, 2, i)).setFill(Color.WHITE);
                            break;
                    }
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
        playersInGame = numberOfPlayers;
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
        ((ViewInterface) loader.getController()).registerObserver(cA);
        primaryStage.setOnCloseRequest((e) -> manageWindowClose());
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

    public void manageWindowClose() {
        cA.shutDown();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void sendChallengerDivinities(ArrayList<String> divinities) {
        notifyObserver((x) -> x.selectAvailableDivinities(divinities));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                multifunctionalPane.getChildren().clear();
            }
        });
    }

    public void sendPlayerDivinity(String divinity) {
        notifyObserver((x) -> x.registerPlayerDivinity(divinity));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                multifunctionalPane.getChildren().clear();
            }
        });
    }

    public void sendFirstPlayerChoice(String playerName) {
        notifyObserver((x) -> x.selectFirstPlayer(playerName));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                multifunctionalPane.getChildren().clear();
            }
        });
    }

    /**
     * method called by the initial positioning scene controller, to notify the server about the player choice
     * @param initialPosition is the position chosen by the player
     */
    public void sendInitialPositioningChoice(Position initialPosition)
    {
        notifyObserver(x->x.putWorkerOnTable(initialPosition));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                multifunctionalPane.getChildren().clear();
            }
        });
    }
}