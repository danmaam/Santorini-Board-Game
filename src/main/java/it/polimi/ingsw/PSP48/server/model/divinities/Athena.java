package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestBuildDome;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

public class Athena extends Divinity {

    private Boolean lastTurnLevelUp = false;

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
     * Resets the flag if the player went level up on the last turn
     *
     * @return
     */
    @Override
    public GameControllerState turnBegin(Model gd) {
        lastTurnLevelUp = false;
        return super.turnBegin(gd);
    }

    /**
     * @param workerRow    the row of the cell where the worker is
     * @param workerColumn the column of the cell where the worker is
     * @param moveRow      the row of the board where the worker wants to move
     * @param moveColumn   the column of the board where the worker wants to move
     * @param gd           the actual game state
     * @return the next controller FSM state
     * @throws NotAdjacentCellException if the destination cell is not adjacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @throws DivinityPowerException   if the move isn't allowed by another divinity
     * @throws NoTurnEndException       if the move doesn't allow the player to end the turn
     */
    public GameControllerState move(int workerRow, int workerColumn, int moveRow, int moveColumn, Model gd) throws DivinityPowerException, IncorrectLevelException, OccupiedCellException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        super.move(workerRow, workerColumn, moveRow, moveColumn, gd);
        if (gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(workerRow, workerColumn).getLevel())
            lastTurnLevelUp = true;
        return new RequestBuildDome();
    }


    /**
     * Checks if another player move is allowed, since Athena blocks other players from getting higher
     * when an Athena's worker went up in the last turn.
     *
     * @param move      the coordinates of the move
     * @param gameBoard the model of the game
     * @return false if the move isn't allowed, true otherwise
     */
    @Override
    public Boolean othersMove(ActionCoordinates move, Cell[][] gameBoard) {
        int workerLevel = gameBoard[move.getWorkerRow()][move.getWorkerColumn()].getLevel();
        int moveLevel = gameBoard[move.getMoveRow()][move.getMoveColumn()].getLevel();
        if (moveLevel - workerLevel >= 1) return !lastTurnLevelUp;
        return true;
    }


    @Override
    public String getName() {
        return "Athena";
    }

    @Override
    public String getDescription() {
        return "If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.";
    }
}
