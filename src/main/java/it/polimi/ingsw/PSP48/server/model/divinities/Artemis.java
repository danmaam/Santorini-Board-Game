package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestBuildDome;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestOptionalMove;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Implementation of Artemis divinity
 */
public class Artemis extends Divinity {

    private int oldRowMove = -1;
    private int oldColumnMove = -1;

    /**
     * Checks if Artemis is allowed for a certain number of players
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

    /**
     * Resets the last move coordinate and the super method to check if the player can end his turn.
     *
     * @return the next controller FSM state
     */
    @Override
    public GameControllerState turnBegin(Model gd) {
        oldColumnMove = -1;
        oldRowMove = -1;
        return (super.turnBegin(gd));
    }

    /**
     * Generates a list of valid cells for move. At the second move, Artemis can't turn on the previous cell
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellForMove(workerRow, workerColumn, gameCells, divinitiesInGame).stream()
                .filter(cell -> !(cell.getColumn() == oldColumnMove && cell.getRow() == oldRowMove))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Process the move. Artemis allows a second move, but not on the previous cell
     *
     * @param workerRow    the row of the cell where the worker is
     * @param workerColumn the column of the cell where the worker is
     * @param moveRow      the row of the board where the worker wants to move
     * @param moveColumn   the column of the board where the worker wants to move
     * @param gd           the game status
     * @return the next method to be invoked by the controller
     * @throws NotAdjacentCellException if the destination cell is not adjacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState move(int workerRow, int workerColumn, int moveRow, int moveColumn, Model gd) throws NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NoTurnEndException {
        GameControllerState nextAction;
        if (oldRowMove == -1 && oldColumnMove == -1) nextAction = new RequestOptionalMove();
        else {
            nextAction = new RequestBuildDome();
            //the player doesn't want to do the optional move, or the controller requests the next action since the optional move isn't possible
            if (moveColumn == -1 && moveRow == -1) return nextAction;
        }
        if (oldRowMove != -1 && oldColumnMove != -1 && oldRowMove == moveRow && oldColumnMove == moveColumn)
            throw new DivinityPowerException("Fail to move on the previous cell");
        super.move(workerRow, workerColumn, moveRow, moveColumn, gd);
        if (oldRowMove == -1 && oldColumnMove == -1) {
            oldRowMove = workerRow;
            oldColumnMove = workerColumn;
        }

        return nextAction;
    }

    /**
     * Getter of name
     *
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Artemis";
    }

    /**
     * Getter of divinity's description
     *
     * @return the description of the divinity power
     */
    @Override
    public String getDescription() {
        return "Your Worker may move one additional time, but not back to its initial space.";
    }
}
