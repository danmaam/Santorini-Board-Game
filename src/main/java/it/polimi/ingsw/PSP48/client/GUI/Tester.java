package it.polimi.ingsw.PSP48.client.GUI;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.GameBoardController;
import it.polimi.ingsw.PSP48.client.GUI.sceneControllers.LoginScreenController;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkIncoming;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkOutcoming;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Tester extends GUI {


    private ClientNetworkOutcoming cA;
    private Socket server;
    private ClientNetworkIncoming cI;
    private static Stage primaryStage;
    private final ArrayList<ViewObserver> observers = new ArrayList<>();
    private int playersInGame;
    private LoginScreenController loginController;
    private GameBoardController boardController;
    private Scene board = null;


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
            board = new Scene(boardRoot, 1075, 823);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setTitle("Santorini Log-In");


        primaryStage.setScene(board);


        primaryStage.setTitle("Santorini");
        primaryStage.show();


        ArrayList<String> playerList = new ArrayList<>();
        playerList.add("pippo.BLUE.Minotaur");
        playerList.add("paperino.WHITE.Apollo");
        playerList.add("pluto.GREY.Athena");
        boardController.changedPlayerList(playerList);

        ArrayList<Cell> newCells = new ArrayList<>();
        newCells.add(new Cell(3, 4, 0, "pippo", false));
        newCells.add(new Cell(4, 4, 1, "paperino", false));
        newCells.add(new Cell(2, 2, 2, "pluto", false));
        boardController.changedBoard(newCells);
        newCells = new ArrayList<>();
        newCells.add(new Cell(0, 0, 3, null, true));
        boardController.changedBoard(newCells);
        newCells = new ArrayList<>();
        newCells.add(new Cell(4, 4, 1, null, false));
        newCells.add(new Cell(3, 3, 0, "paperino", false));
        boardController.changedBoard(newCells);

        ArrayList<Position> pos = new ArrayList<>();
        pos.add(new Position(4, 4));
        pos.add(new Position(0, 3));

        //boardController.requestInitialPositioning(pos);

        ArrayList<WorkerValidCells> move = new ArrayList<>();
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(new Position(2, 2));
        move.add(new WorkerValidCells(positions, 1, 1));

        ArrayList<Position> pos2 = new ArrayList<>();
        pos2.add(new Position(4, 4));

        move.add(new WorkerValidCells(pos2, 3, 3));

        ArrayList<String> p = new ArrayList<>();
        p.add("pippo");
        p.add("paperino");
        p.add("pluto");

        ArrayList<DivinitiesWithDescription> d = new ArrayList<>();
        d.add(new DivinitiesWithDescription("Apollo", "A"));
        d.add(new DivinitiesWithDescription("Artemis", "A"));
        d.add(new DivinitiesWithDescription("Zeus", "A"));
        d.add(new DivinitiesWithDescription("Circe", "A"));
        d.add(new DivinitiesWithDescription("Prometheus", "A"));
        d.add(new DivinitiesWithDescription("Atlas", "A"));
        d.add(new DivinitiesWithDescription("Eros", "A"));

        ArrayList<String> players = new ArrayList<>();
        players.add("pippo");
        players.add("pluto");
        players.add("paperino");

        boardController.requestChallengerDivinitiesSelection(d);


    }
}
