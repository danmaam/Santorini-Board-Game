package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.DivinityPowerException;
import it.polimi.ingsw.PSP48.server.model.exceptions.OccupiedCellException;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Implementation of Eros divinity
 */
public class Eros extends Divinity {

    /**
     * Checks if Eros is allowed for a certain number of players
     *
     * @param pNum the number of players
     * @return if the divinity is allowed for the specified number of players
     */
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
     * Generates a list of cells where a player can put his initial worker according to Eros' power
     *
     * @param gameCells the actual game board state
     * @return an array list of cells valid for the positioning
     */
    @Override
    public ArrayList<Position> validCellsForInitialPositioning(Cell[][] gameCells) {
        ArrayList<Position> validCells = super.validCellsForInitialPositioning(gameCells);
        //deletes the cells that aren't on the board's perimeter
        validCells = validCells.stream()
                .filter(cell -> cell.getColumn() == 0 || cell.getColumn() == 4 || cell.getRow() == 0 || cell.getRow() == 4)
                .collect(Collectors.toCollection(ArrayList::new));
        ArrayList<Position> tbr = new ArrayList<>();
        for (Position c : validCells) {
            //if the first positioning happened, deletes cells that aren't on the board's opposide side
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

    /**
     * Getter of name
     *
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Eros";
    }

    /**
     * Calculate Eros' win condition according to the game rules
     *
     * @param gd the state of the game
     * @return true if the actual player has won, false if the game must go on
     */
    @Override
    public boolean postMoveWinCondition(Model gd) {
        boolean divinityWinCondition = false;
        //first, we have to check if the player has two workers in game
        String playerName = gd.getCurrentPlayer().getName();
        ArrayList<Position> positions = gd.getPlayerPositionsInMap(playerName);
        //check if the player has at least two workers in game, and if they are adjacent
        if (!(positions.size() <= 1 || !adjacentCellVerifier(positions.get(0).getRow(), positions.get(0).getColumn(), positions.get(1).getRow(), positions.get(1).getColumn()))) {
            //the player ha at least two workers in game; now i have to check how many players there are in game
            if (gd.getNumberOfPlayers() == 2) {
                //case with two players in game: eros wins with workers adjacent at the first level
                if (gd.getCell(positions.get(0).getRow(), positions.get(0).getColumn()).getLevel() == 1 && gd.getCell(positions.get(1).getRow(), positions.get(1).getColumn()).getLevel() == 1)
                    divinityWinCondition = true;
            } else {
                //case with three players: eros wins with workers adjacent at any level
                if (gd.getCell(positions.get(0).getRow(), positions.get(0).getColumn()).getLevel() == gd.getCell(positions.get(1).getRow(), positions.get(1).getColumn()).getLevel())
                    divinityWinCondition = true;
            }
        }


        return super.postMoveWinCondition(gd) || divinityWinCondition;
    }

    /**
     * Puts player's workers on the board, according to Eros' power, so only on the board margin, and on board
     * opposite sides.
     *
     * @param p  the position where the player would put the worker on the board
     * @param gd the model
     * @return the next controller FSM state
     * @throws OccupiedCellException  if the desidered cell is occupied
     * @throws DivinityPowerException if the positioning isn't complain to eros' power
     */
    @Override
    public GameControllerState putWorkerOnBoard(Position p, Model gd) throws OccupiedCellException, DivinityPowerException {
        //checks if the positioning is on perimerer cell
        if (!(p.getRow() == 0 || p.getRow() == 4 || p.getColumn() == 0 || p.getColumn() == 4)) {
            throw new DivinityPowerException("Can't put the worker on this cell due to divinity power");
        }

        //checks if the second positioning is on the opposite side of the board
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
        GameControllerState nextAction = super.putWorkerOnBoard(p, gd);
        if (p.getRow() == 0 || p.getRow() == 4) previousRow = p.getRow();
        if (p.getColumn() == 0 || p.getColumn() == 4) previousColumn = p.getColumn();
        return nextAction;
    }

    /**
     * Getter of divinity's description
     *
     * @return the description of the divinity power
     */
    @Override
    public String getDescription() {
        return "Place your Workers anywhere along opposite edges of the board. You also win if one of your Workers moves to a space neighboring your other Worker and both are on the first level (or the same level in a 3-player game).";
    }
}
