package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.Cell;
import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Demeter extends Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;

    private int oldRowBuild = -1;
    private int oldColumnBuild = -1;



    /**
     * Reset the coordinate of first building
     */
    @Override
    public void turnBegin(GameData gd) {
        oldRowBuild = -1;
        oldColumnBuild = -1;
    }

    /**
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCells) {
        return super.getValidCellForBuilding(WorkerColumn, WorkerRow, otherDivinitiesInGame, gameCells)
                .stream()
                .filter(cell -> !(cell.getColumn() == oldColumnBuild && cell.getRow() == oldRowBuild))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param workerColumn     the column where the worker is
     * @param workerRow        the row where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the divinities in game
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellsToPutDome(workerColumn, workerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> !(cell.getColumn() == oldColumnBuild && cell.getRow() == oldRowBuild))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Redefined since we have to check that the player is not trying to build on the same cell
     *
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the game status
     * @throws NotAdiacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public void build(int workerRow, int workerColumn, int buildRow, int buildColumn, GameData gd) throws DivinityPowerException, MaximumLevelReachedException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        if (oldRowBuild != -1 && oldColumnBuild != -1 && oldRowBuild == buildRow && oldColumnBuild == buildColumn)
            throw new DivinityPowerException("feffe");
        super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
        oldColumnBuild = buildColumn;
        oldRowBuild = buildRow;
    }

    @Override
    public String getName() {
        return name;
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
        if (oldRowBuild != -1 && oldColumnBuild != -1 && oldRowBuild == domeRow && oldColumnBuild == domeColumn)
            throw new DivinityPowerException("feffe");
        super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
        oldRowBuild = domeRow;
        oldColumnBuild = domeColumn;
    }

}
