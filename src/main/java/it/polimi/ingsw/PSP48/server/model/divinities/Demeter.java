package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Demeter extends Divinity {

    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    private int oldRowBuild = -1;
    private int oldColumnBuild = -1;
    private boolean prevBuild = false;


    /**
     * Reset the coordinate of first building and then checks if the player can end the turn
     *
     * @return the next controller FSM state
     */
    @Override
    public Consumer<GameController> turnBegin(Model gd) {
        oldRowBuild = -1;
        oldColumnBuild = -1;
        prevBuild = false;
        return super.turnBegin(gd);
    }

    /**
     * Generates a list of cells where a certain worker can build
     *
     * @param workerRow             the row where the worker is
     * @param workerColumn          the column where the worker is
     * @param otherDivinitiesInGame the divinities in the game
     * @param gameCells             the actual board state
     * @return a list of cell valid for the building of the worker
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int workerRow, int workerColumn, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCells) {
        return super.getValidCellForBuilding(workerRow, workerColumn, otherDivinitiesInGame, gameCells)
                .stream()
                .filter(cell -> !(cell.getColumn() == oldColumnBuild && cell.getRow() == oldRowBuild))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Generates a list of cells where a certain worker can put a dome
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the divinities in game
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellsToPutDome(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellsToPutDome(workerRow, workerColumn, gameCells, divinitiesInGame).stream()
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
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws DivinityPowerException, MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException {

        if (!prevBuild) {
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            oldRowBuild = buildRow;
            oldColumnBuild = buildColumn;
            prevBuild = true;
            return GameController::requestOptionalBuilding;
        } else {
            if (workerRow == -1 && workerColumn == -1) return GameController::turnChange;
            else if (buildRow == oldRowBuild && buildColumn == oldColumnBuild)
                throw new DivinityPowerException("NO!");
            else {
                super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
                return GameController::turnChange;
            }
        }
    }

    @Override
    public String getName() {
        return "Demeter";
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @throws NotAdjacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        if (!prevBuild) {
            super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
            oldRowBuild = domeRow;
            oldColumnBuild = domeColumn;
            prevBuild = true;
            return GameController::requestOptionalBuilding;
        } else {
            if (workerRow == -1 && workerColumn == -1) return GameController::turnChange;
            else if (domeRow == oldRowBuild && domeColumn == oldColumnBuild)
                throw new DivinityPowerException("NO!");
            else {
                super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
                return GameController::turnChange;
            }
        }
    }

    @Override
    public String getDescription() {
        return "Your Worker may build one additional time, but not on the same space.";
    }
}
