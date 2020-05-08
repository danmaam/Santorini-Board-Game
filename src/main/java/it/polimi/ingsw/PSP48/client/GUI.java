package it.polimi.ingsw.PSP48.client;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GUI extends Application implements Runnable, ViewInterface, ClientNetworkObserver {
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
    public void nicknameResult(String result) {

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
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 500, Color.WHITE);
        stage.setTitle("JavaFX Demo");
        stage.setScene(scene);
        stage.show();
    }
}
