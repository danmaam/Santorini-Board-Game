package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Prometheus extends Divinity {
    private final String name = "Prometheus";
    private final Boolean threePlayerSupported = true;

    private int oldLevel;
    private int newLevel;

    boolean previousBuild = false;

    /**
     * don't do anything since without a divinity there isn't a modifier
     *
     * @param gd
     */
    @Override
    public void turnBegin(GameData gd) {
        previousBuild = false;
    }

    /**
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
        return super.getValidCellForMove(WorkerColumn, WorkerRow, gameCells, otherDivinitiesInGame).stream()
                .filter(cell -> !previousBuild || !(cell.getLevel() > gameCells[WorkerRow][WorkerColumn].getLevel()))
                .collect(Collectors.toCollection(ArrayList::new));
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
        //i must check if i'm not growing up level if i built before
        if (previousBuild && gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(WorkerRow, WorkerColumn).getLevel())
            throw new DivinityPowerException("trying to level up after previous building");
        super.move(WorkerColumn, WorkerRow, moveColumn, moveRow, gd);
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @throws NotAdiacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public void build(int workerRow, int workerColumn, int buildRow, int buildColumn, GameData gd) throws NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
        previousBuild = true;
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @throws NotAdiacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public void dome(int workerRow, int workerColumn, int domeRow, int domeColumn, GameData gd) throws NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
        previousBuild = true;
    }
}
