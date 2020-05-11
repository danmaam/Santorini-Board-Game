package it.polimi.ingsw.PSP48.client.GUI;

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

public class GUI extends Application implements Runnable, ViewInterface, ClientNetworkObserver
{
    private ArrayList<GUIPlayer> playerList = new ArrayList<>();
    private final GUICell [][] gameBoard= new GUICell[5][5];

    /**
     * class constructor initialising the game board
     */
    GUI()
    {
        for(int i=0; i<5; i++)
        {
            for (int j=0; j<5; j++)
            {
                gameBoard[i][j]=new GUICell(i, j, false); //at the very beginning cells are not highlighted
            }
        }
    }

    public ArrayList<GUIPlayer> getPlayerList()
    {
        return playerList;
    }

    public GUICell[][] getGameBoard()
    {
        return gameBoard;
    }

    /**
     * method used to directly retrieve a cell from the board
     * @param row is the row of the needed cell
     * @param column is the column of the needed cell
     * @return the cell we need
     */
    public GUICell getCellOnBoard(int row, int column)
    {
        return gameBoard[row][column];
    }

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

    /**
     * method updating the board after every change
     * @param newCells is the list of cells that need to be updated
     */
    @Override
    public void changedBoard(ArrayList<Cell> newCells)
    {
        GUICell cellToUpdate;

        for (Cell c : newCells)
        {
            cellToUpdate=this.getCellOnBoard(c.getRow(), c.getColumn());

            cellToUpdate.setCellLevel(c.getLevel());
            cellToUpdate.setDome(c.isDomed());
            String updatedName=c.getPlayer();
            cellToUpdate.setPlayer(updatedName);
        }

        //we'll then need to show the board to the player
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
