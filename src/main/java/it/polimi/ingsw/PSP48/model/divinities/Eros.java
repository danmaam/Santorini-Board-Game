package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.Cell;
import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.Position;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Eros extends Divinity {
    private final String name = "Eros";
    private final Boolean threePlayerSupported = true;

    private int previousRow = -1;
    private int previousColumn = -1;


    /**
     * @param gameCells the actual game board state
     * @return an array list of cells valid fro the positioning
     */
    @Override
    public ArrayList<Cell> validCellsForInitialPositioning(Cell[][] gameCells) {
        ArrayList<Cell> validCells = super.validCellsForInitialPositioning(gameCells);
        validCells = validCells.stream()
                .filter(cell -> cell.getColumn() == 0 || cell.getColumn() == 4 || cell.getRow() == 0 || cell.getRow() == 4)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Cell> tbr = new ArrayList<>();
        for (Cell c : validCells) {
            if (previousRow != -1 || previousColumn != -1) {
                if (previousRow == -1) {
                    if (c.getColumn() != 4 - previousColumn) tbr.add(c);
                } else {
                    if (previousColumn == -1) {
                        if (c.getRow() != 4 - previousRow) tbr.add(c);
                    } else {
                        if (c.getRow() != 4 - previousRow && c.getColumn() != 4 - previousColumn) tbr.add(c);
                    }
                }
            }
        }

        for (Cell c : tbr) validCells.remove(c);
        return validCells;

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
        if (!(previousRow == -1 && previousColumn == -1) && !(previousRow != -1 && row == 4 - previousRow) && !(previousColumn != -1 && column == 4 - previousColumn))
            throw new DivinityPowerException("Not opposite side");
        else if (previousRow == -1 && previousColumn == -1) {
            if (row == 4 || row == 0) previousRow = row;
            if (column == 4 || column == 0) previousColumn = column;
        }
        gameCells[row][column].setPlayer(playerName);
    }


    @Override
    public String getName() {
        return name;
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
