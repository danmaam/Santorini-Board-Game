package it.polimi.ingsw.PSP48.client.GUI;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.GameBoardController;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.LoginScreenController;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkIncoming;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkOutcoming;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GUI extends Application implements ClientNetworkObserver, Runnable, ViewInterface {


    private ClientNetworkOutcoming cA;
    private Socket server;
    private ClientNetworkIncoming cI;
    private Scene scene;
    private Parent root;
    private static Stage primaryStage;
    private Thread cIThread;
    private Thread cAThread;
    private final ArrayList<ViewObserver> observers = new ArrayList<>();
    private int playersInGame;
    private LoginScreenController loginController;
    private GameBoardController boardController;


    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {

    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {

    }

    @Override
    public void endgame(String messageOfEndGame) {
        Platform.runLater(() -> {
            cA.shutDown();
            primaryStage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("End of game");
            alert.setHeaderText("Oh! The game finished!");
            alert.setContentText(messageOfEndGame);
            alert.showAndWait();
        });

    }

    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        boardController.requestDivinitySelection(availableDivinities);
    }

    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        boardController.requestInitialPlayerSelection(players);
    }

    /**
     * method handling the choice of the positions of the workers on the board
     *
     * @param validCells is the list of valid positions to put the worker on the board
     */
    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        /*
        //received valid cells, they must be activated
        //i load the image containing the background for the valid cells
        Image illuminatedCell = new Image("santorini_risorse-grafiche-2/Texture2D/Toggle_Checkmark.png");
        //i must enable all the cells
        for (Position p : validCells) {
            ImageView colouredCell = new ImageView(illuminatedCell);

        }

         */
    }

    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        boardController.requestChallengerDivinitiesSelection(div, playerNumber);
    }

    @Override
    public void printMessage(String s) {
        boardController.printMessage(s);
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
    public void changedBoard(ArrayList<Cell> newCells) {

    }

    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        boardController.changedPlayerList(newPlayerList);
    }


    @Override
    public void run() {

    }

    @Override
    public synchronized void start(Stage stage) throws Exception {
        System.out.println(this);
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/SantoriniGUI.fxml"));
        loginController = new LoginScreenController(this);
        loginLoader.setController(loginController);
        root = loginLoader.load();
        scene = new Scene(root, 467, 653);
        primaryStage = stage;
        stage.setTitle("Santorini Log-In");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        System.out.println("Initialized stage");
    }

    @Override
    public synchronized void completedSetup(String message) {
        cI.completedSetup();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
        boardController = new GameBoardController(this);
        loader.setController(boardController);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setOnCloseRequest((e) -> manageWindowClose());
        Platform.runLater(() -> {
            scene = new Scene(root, 1155, 825);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Santorini");
            primaryStage.show();
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


    public void setNumberOfPlayers(int number) {
        playersInGame = number;
    }

    public ClientNetworkOutcoming getUploader() {
        return cA;
    }

    public void stopNetwork() {
        if (cIThread != null) cIThread.stop();
        if (cAThread != null) cAThread.stop();

        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startNetwork(String IP) {

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

    public void setSocket(Socket s) {
        server = s;
    }

    public synchronized void requestGameModeSend(String message) {
        loginController.requestGameModeSend(message);
    }

    public synchronized void nicknameResult(String result) {
        Platform.runLater(() -> loginController.nicknameResult(result));
    }

    @Override
    public void gameModeResult(String result) {

    }

    public synchronized void requestNicknameSend(String message) {
        Platform.runLater(() -> loginController.requestNicknameSend(message));
    }

    public int getPlayersInGame() {
        return playersInGame;
    }
}