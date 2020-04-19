package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.GameData;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Artemis extends Divinity {
    private final String name = "Artemis";
    private final Boolean threePlayerSupported = true;

    private int oldRowMove = -1;
    private int oldColumnMove = -1;


    /**
     * reset the last move coordinate
     */
    @Override
    public void turnBegin(GameData gd) {
        oldColumnMove = -1;
        oldRowMove = -1;
    }

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
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
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NotEmptyCellException {
        if (oldRowMove != -1 && oldColumnMove != -1 && oldRowMove == moveRow && oldColumnMove == moveColumn)
            throw new DivinityPowerException("Fail to move on the previous cell");
        int workerLevel = gd.getCell(WorkerRow, WorkerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        super.move(WorkerColumn, WorkerRow, moveColumn, moveRow, gd);
        if (oldRowMove == -1 && oldColumnMove == -1) {
            oldRowMove = WorkerRow;
            oldColumnMove = WorkerColumn;
        }

    }

    @Override
    public String getName() {
        return name;
    }
}
