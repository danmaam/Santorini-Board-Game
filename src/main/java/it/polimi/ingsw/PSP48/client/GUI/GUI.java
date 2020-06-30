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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * class used to call the methods of the various scene controllers, which implement the operations of the gui
 * @author Daniele Mammone, Rebecca Marelli
 */
public class GUI extends Application implements ClientNetworkObserver, ViewInterface {


    private ClientNetworkOutcoming cA;
    private Socket server;
    private ClientNetworkIncoming cI;
    private static Stage primaryStage;
    private final ArrayList<ViewObserver> observers = new ArrayList<>();
    private int playersInGame;
    private LoginScreenController loginController;
    private GameBoardController boardController;
    private Scene board = null;


    /**
     * Requests the board controller to request the player to complete a move action
     *
     * @param validCellsForMove an associations between workers that can move, and the cells where they can be moved
     */
    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        Platform.runLater(() -> boardController.requestMove(validCellsForMove));
    }

    /**
     * Requests the board controller to request the player to complete a construction action.
     *
     * @param validForBuild an associations between workers that can build, and the cells where they can build
     * @param validForDome  an associations between workers that can put a dome on the board, and the cells where they can put a dome
     */
    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {
        Platform.runLater(() -> boardController.requestDomeOrBuild(validForBuild, validForDome));
    }

    /**
     * Closes the game process and shows a alert message with the reason of the end of the game
     *
     * @param messageOfEndGame the reason of the end of the game
     */
    @Override
    public void endgame(String messageOfEndGame) {
        Platform.runLater(() -> {
            primaryStage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("End of game");
            alert.setHeaderText("Oh! The game finished!");
            alert.setContentText(messageOfEndGame);
            alert.showAndWait();
            System.exit(0);
        });

    }

    /**
     * Requests the board controller to request the player to select his divinity
     *
     * @param availableDivinities the divinities from which the player can choose
     */
    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {

        /*
        synchronized (lock) {
            while (!loadedBoard) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
*/

        Platform.runLater(() -> boardController.requestDivinitySelection(availableDivinities));
    }

    /**
     * Requests the board controller to request the challenger to choose the first player
     *
     * @param players the list of players
     */
    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        Platform.runLater(() -> boardController.requestInitialPlayerSelection(players));
    }

    /**
     * method handling the choice of the positions of the workers on the board
     *
     * @param validCells is the list of valid positions to put the worker on the board
     */
    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        Platform.runLater(() -> boardController.requestInitialPositioning(validCells)); //we call the right controller to handle the actions
    }

    /**
     * Requests the board controller to request the challenger to select the divinities availabe to be chosen for the game
     *
     * @param div          the available divinities
     * @param playerNumber the number of players in game
     */
    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        /*
        synchronized (lock) {
            while (!loadedBoard) {

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

         */
        Platform.runLater(() -> boardController.requestChallengerDivinitiesSelection(div));
    }

    /**
     * Prints a message in the left-down corner of the gui
     *
     * @param s the message to be printed
     */
    @Override
    public void printMessage(String s) {
        /*
        synchronized (lock) {
            while (!loadedBoard) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

         */
        Platform.runLater(() -> boardController.printMessage(s));
    }

    /**
     * Requests the board controller to request the player to complete an optional move action, or to skip it.
     *
     * @param validCellsForMove an association between workers that can perform the move and the cells where they can move
     */
    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        Platform.runLater(() -> boardController.requestOptionalMove(validCellsForMove));
    }

    /**
     * Requests the board controller to request the player to complete an optional construction action, or to skip it.
     *
     * @param build an association between workers that can build on the board, and the cells where they can build
     * @param dome  an association between workers that can put a dome on the board, and the cells where they can put a dome
     */
    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        Platform.runLater(() -> boardController.requestOptionalBuild(build, dome));
    }

    /**
     * Registers an observer of the View
     *
     * @param obv the observer to be unregistered
     */
    @Override
    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    /**
     * Unregister a view observer
     *
     * @param obv the observer to be unregistered
     */
    @Override
    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    /**
     * Notifies all observers to do something defined in the lambda expression parameter
     *
     * @param lambda the method to be invoked by the observer
     */
    @Override
    public void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver o : observers) {
            lambda.accept(o);
        }
    }


    /**
     * Updates the game cells which content has changed in model
     *
     * @param newCells the changed cells
     */
    @Override
    public void changedBoard(ArrayList<Cell> newCells) {
        Platform.runLater(() -> boardController.changedBoard(newCells));
    }

    /**
     * Updates the player list displayed in the window, with the new player-divinities association list
     *
     * @param newPlayerList the new player-divinity association list
     */
    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        Platform.runLater(() -> boardController.changedPlayerList(newPlayerList));
    }


    /**
     * Starts the gui thread, initialize the main stage with the login screen scene
     *
     * @param stage the primary stage of the gui
     */
    @Override
    public void start(Stage stage) {
        System.out.println(this);
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/SantoriniGUI.fxml"));
        loginController = new LoginScreenController(this);
        loginLoader.setController(loginController);
        Parent root = null;
        try {
            root = loginLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 467, 653);
        primaryStage = stage;

        FXMLLoader controllerLoader = new FXMLLoader(getClass().getResource("/gameLayout.fxml"));
        boardController = new GameBoardController(this);
        controllerLoader.setController(boardController);
        Pane boardRoot;
        try {
            boardRoot = controllerLoader.load();
            board = new Scene(boardRoot, 1280, 720);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Santorini Log-In");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        System.out.println("Initialized stage");
    }

    /**
     * Used by server to notify that the game setup has ended. The login screen is closed, and the board scene is loaded.
     *
     * @param message the message of completed setup
     */
    @Override
    public void completedSetup(String message) {

        Platform.runLater(() -> {
            primaryStage.setOnCloseRequest((e) -> manageWindowClose());
            primaryStage.setScene(board);
            primaryStage.setTitle("Santorini");
            primaryStage.show();
        });

    }

    /**
     * Kills all client processes when the game window is closed by the user
     */
    public void manageWindowClose() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Sets the number of players in game, after the game mode is chosen by the player
     *
     * @param number the number of players in game
     */
    public void setNumberOfPlayers(int number) {
        playersInGame = number;
    }

    /**
     * @return the network handler used to send messages over the network
     */
    public ClientNetworkOutcoming getUploader() {
        return cA;
    }

    /**
     * Closes network connection to server, stops all network handler threads, closes the connection socket, maintaining the process of the GUI running
     */
    public void stopNetwork() {
        if (cI != null) cI.shutdown();
        if (cA != null) cA.shutDown();

        try {
            if (server != null) server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the socket to the server, and network handler threads.
     *
     * @param IP the server IP
     */
    public void startNetwork(String IP) {
        try {
            server = new Socket(IP, 7777);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Can't connect to the server");
            alert.showAndWait();
            server = null;
            return;
        }
        //primaryStage.close();

        cA = new ClientNetworkOutcoming(server);
        Thread cAThread = new Thread(cA);

        this.registerObserver(cA);

        cI = new ClientNetworkIncoming(this, server);
        cI.setOutHandler(cA);
        cI.addObserver(this);
        Thread cIThread = new Thread(cI);

        cIThread.start();
        cAThread.start();

        System.out.println("Correctly connected to the server!");
    }

    /**
     * Request the login controller to send to the server the chosen gamemode, with player's birthday if decided to play without divinities
     *
     * @param message the game mode chosen by the player
     */
    @Override
    public void requestGameModeSend(String message) {
        loginController.requestGameModeSend();
    }


    /**
     * Request the login controller to send to the server the chosen nickname
     * If the server requests again the nickname due to nickname unavailability, the connection to the server is closed
     *
     * @param message the request made by the server
     */
    @Override
    public void requestNicknameSend(String message) {
        if (!message.equals("Invalid nickname. Retry")) {
            Platform.runLater(() -> loginController.requestNicknameSend());
        } else {
            Platform.runLater(() -> {
                stopNetwork();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText("Invalid nickname");
                alert.setContentText("Nickname already in use or invalid. \n Retry");
                alert.showAndWait();

            });
        }
    }

    /**
     *
     * @return the number of players in the game
     */
    public int getPlayersInGame() {
        return playersInGame;
    }
}