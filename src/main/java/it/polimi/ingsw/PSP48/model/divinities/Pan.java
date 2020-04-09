package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.exceptions.*;

public class Pan extends Divinity {
    private final String name = "Pan";
    private final Boolean threePlayerSupported = true;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     * @author Daniele Mammone
     */
    @Override
    public boolean winCondition(GameData gd) {
        return (super.winCondition(gd) || newLevel - oldLevel <= -2);
    }

    /**
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the actual game state
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NotEmptyCellException {
        super.move(WorkerColumn, WorkerRow, moveColumn, moveRow, gd);
        oldLevel = gd.getCell(WorkerRow, WorkerColumn).getLevel();
        newLevel = gd.getCell(moveRow, moveColumn).getLevel();
    }
}