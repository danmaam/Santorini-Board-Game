package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.DivinityPowerException;
import it.polimi.ingsw.PSP48.server.model.exceptions.OccupiedCellException;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Eros extends Divinity {

    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    private int previousRow = -1;
    private int previousColumn = -1;


    /**
     * @param gameCells the actual game board state
     * @return an array list of cells valid fro the positioning
     */
    @Override
    public ArrayList<Position> validCellsForInitialPositioning(Cell[][] gameCells) {
        ArrayList<Position> validCells = super.validCellsForInitialPositioning(gameCells);
        validCells = validCells.stream()
                .filter(cell -> cell.getColumn() == 0 || cell.getColumn() == 4 || cell.getRow() == 0 || cell.getRow() == 4)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Position> tbr = new ArrayList<>();
        for (Position c : validCells) {
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

        for (Position c : tbr) validCells.remove(c);
        return validCells;

    }


    @Override
    public String getName() {
        return "Eros";
    }

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     */
    @Override
    public boolean winCondition(Model gd) {
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

    @Override
    public Consumer<GameController> putWorkerOnBoard(Position p, Model gd) throws OccupiedCellException, DivinityPowerException {
        if (!(p.getRow() == 0 || p.getRow() == 4 || p.getColumn() == 0 || p.getColumn() == 4)) {
            throw new DivinityPowerException("Can't put the worker on this cell due to divinity power");
        }

        if (previousRow != -1 || previousColumn != -1) {
            if (previousRow == -1) {
                if (p.getColumn() != 4 - previousColumn) throw new DivinityPowerException("");
            } else {
                if (previousColumn == -1) {
                    if (p.getRow() != 4 - previousRow) throw new DivinityPowerException("");
                } else {
                    if (p.getRow() != 4 - previousRow && p.getColumn() != 4 - previousColumn)
                        throw new DivinityPowerException("");
                }
            }
        }
        Consumer<GameController> nextAction = super.putWorkerOnBoard(p, gd);
        if (p.getRow() == 0 || p.getRow() == 4) previousRow = p.getRow();
        if (p.getColumn() == 0 || p.getColumn() == 4) previousColumn = p.getColumn();
        return nextAction;
    }

    @Override
    public String getDescription() {
        return "Place your Workers anywhere along opposite edges of the board. You also win if one of your Workers moves to a space neighboring your other Worker and both are on the first level (or the same level in a 3-player game).";
    }
}
