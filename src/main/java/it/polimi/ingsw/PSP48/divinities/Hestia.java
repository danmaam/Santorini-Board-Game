package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Hestia extends Divinity {
    private final String name = "Hestia";
    private final Boolean threePlayerSupported = true;

    private int oldLevel;
    private int newLevel;

    private Boolean alreadyBuilt = false;

    /**
     * don't do anything since without a divinity there isn't a modifier
     *
     * @param gd
     */
    @Override
    public void turnBegin(GameData gd) {
        alreadyBuilt = false;
    }

    /**
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param otherDivinitiesInGame the other divinites in game
     * @param gameCell              the game board
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCell) {
        return super.getValidCellForBuilding(WorkerColumn, WorkerRow, otherDivinitiesInGame, gameCell).stream()
                .filter(cell -> alreadyBuilt ? cell.getColumn() != 0 && cell.getColumn() != 4 && cell.getRow() != 0 && cell.getRow() != 4 : true)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param workerColumn     the column where the worker is
     * @param workerRow        the row where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellsToPutDome(workerColumn, workerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> alreadyBuilt ? cell.getColumn() != 0 && cell.getColumn() != 4 && cell.getRow() != 0 && cell.getRow() != 4 : true)
                .collect(Collectors.toCollection(ArrayList::new));
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
        if (alreadyBuilt && (buildRow == 4 || buildColumn == 0 || buildColumn == 4 || buildRow == 0))
            throw new DivinityPowerException("Trying to make the second construction on a perimetral cell");
        super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
        if (!alreadyBuilt) alreadyBuilt = true;
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
        if (alreadyBuilt && (domeColumn == 4 || domeRow == 4 || domeColumn == 0 || domeRow == 0))
            throw new DivinityPowerException("trying to perform the second build on a perimetral cell");
        super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
        if (!alreadyBuilt) alreadyBuilt = true;
    }
}
