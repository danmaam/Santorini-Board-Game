package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Artemis extends Divinity {

    private int oldRowMove = -1;
    private int oldColumnMove = -1;

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
     * reset the last move coordinate
     */
    @Override
    public Consumer<GameController> turnBegin(Model gd) {
        oldColumnMove = -1;
        oldRowMove = -1;
        return (super.turnBegin(gd));
    }

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellForMove(WorkerColumn, WorkerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> !(cell.getColumn() == oldColumnMove && cell.getRow() == oldRowMove))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Overriden since Artemis allows a second move, but not on the previous cell
     *
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the game status
     * @return the next method to be invoked by the controller
     * @throws NotAdjacentCellException if the destination cell is not adjacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Model gd) throws NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NoTurnEndException {
        Consumer<GameController> nextAction;
        if (oldRowMove == -1 && oldColumnMove == -1) nextAction = GameController::requestOptionalMove;
        else {
            nextAction = GameController::requestBuildDome;
            //the player doesn't want to do the optional move, or the controller requests the next action since the optional move isn't possible
            if (moveColumn == -1 && moveRow == -1) return nextAction;
        }
        if (oldRowMove != -1 && oldColumnMove != -1 && oldRowMove == moveRow && oldColumnMove == moveColumn)
            throw new DivinityPowerException("Fail to move on the previous cell");
        super.move(WorkerColumn, WorkerRow, moveColumn, moveRow, gd);
        if (oldRowMove == -1 && oldColumnMove == -1) {
            oldRowMove = WorkerRow;
            oldColumnMove = WorkerColumn;
        }

        return nextAction;
    }

    @Override
    public String getName() {
        return "Artemis";
    }
}
