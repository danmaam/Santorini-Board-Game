package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Eros extends Divinity {
    private final String name = "Eros";
    private final Boolean threePlayerSupported = true;

    private int oldLevel;
    private int newLevel;

    private int positioningRow = -1;
    private int positioningColumn = -1;


    /**
     * @param gameCells the actual game board state
     * @return an array list of cells valid fro the positioning
     */
    @Override
    public ArrayList<Cell> validCellsForInitialPositioning(Cell[][] gameCells) {
        ArrayList<Cell> validCells = super.validCellsForInitialPositioning(gameCells);
        return validCells.stream()
                .filter(cell -> cell.getColumn() == 0 || cell.getColumn() == 4 || cell.getRow() == 0 || cell.getRow() == 4)
                .filter(cell -> positioningRow == -1 ? true : cell.getRow() == 4 - positioningRow || positioningColumn == -1 ? true : cell.getColumn() == 4 - positioningColumn)
                .collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * @param row        the row where the player wants to put his worker
     * @param column     the column where the player wants to put his worker
     * @param gameCells  the actual state of the board
     * @param playerName the name of the player that is positioning his workers
     * @return true if the positioning is valid
     * @throws NotEmptyCellException  is on the cell there is already another worker
     * @throws DivinityPowerException if the player is trying to put his workers not on the board's border
     * @author Daniele Mammone
     */
    @Override
    public void gameSetUp(int row, int column, Cell[][] gameCells, String playerName) throws NotEmptyCellException, DivinityPowerException {
        if (gameCells[row][column].getPlayer() != null) throw new NotEmptyCellException("Cella già occupata");
        if (!(row == 4 || row == 0 || column == 4 || column == 0))
            throw new DivinityPowerException("Non su lati opposti");
        if (!(positioningRow == -1 && positioningColumn == -1) && !(positioningRow != -1 && row == 4 - positioningRow) && !(positioningColumn != -1 && column == 4 - positioningColumn))
            throw new DivinityPowerException("Not opposite side");
        else if (positioningRow == -1 && positioningColumn == -1) {
            if (row == 4 || row == 0) positioningRow = row;
            if (column == 4 || column == 0) positioningColumn = column;
        }
        gameCells[row][column].setPlayer(playerName);
    }

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     */
    @Override
    public boolean winCondition(GameData gd) {
        Boolean divinityWinCondition = false;
        //first, we have to check if the player has two workers in game
        String playerName = gd.getCurrentPlayer().getName();
        ArrayList<Position> positions = gd.getPlayerPositionsInMap(playerName);
        //check if the player has at least two workers in game, and if they are adiacent
        if (positions.size() <= 1 || !adiacentCellVerifier(positions.get(0).getRow(), positions.get(0).getColumn(), positions.get(1).getRow(), positions.get(1).getColumn()))
            divinityWinCondition = false;
            //now, i have to verify the win condition depending on the number of player in game, cause they are adiacent
        else if (gd.getNumberOfPlayers() == 2) {
            if (gd.getCell(positions.get(0).getRow(), positions.get(0).getColumn()).getLevel() == 1 && gd.getCell(positions.get(1).getRow(), positions.get(1).getColumn()).getLevel() == 1)
                divinityWinCondition = true;
        } else {
            if (gd.getCell(positions.get(0).getRow(), positions.get(0).getColumn()).getLevel() == gd.getCell(positions.get(1).getRow(), positions.get(1).getColumn()).getLevel())
                divinityWinCondition = true;
        }
        return super.winCondition(gd) || divinityWinCondition;
    }
}
