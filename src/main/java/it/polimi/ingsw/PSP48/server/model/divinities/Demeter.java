package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestOptionalBuild;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.TurnEnd;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementation of Demeter divinity
 * @author Daniele Mammone
 */
public class Demeter extends Divinity {

    /**
     * Checks if Divinity is allowed for a certain number of players
     *
     * @param pNum the number of players
     * @return if the divinity is allowed for the specified number of players
     * @author Daniele Mammone
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

    private int oldRowBuild = -1;
    private int oldColumnBuild = -1;
    private boolean prevBuild = false;


    /**
     * Reset the coordinate of first building and then checks if the player can end the turn
     *
     * @return the next controller FSM state
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState turnBegin(Model gd) {
        oldRowBuild = -1;
        oldColumnBuild = -1;
        prevBuild = false;
        return super.turnBegin(gd);
    }

    /**
     * Generates a list of cells where a certain worker can build. At the second build, Demeter can't build on the
     * first cell where she built.
     *
     * @param workerRow             the row where the worker is
     * @param workerColumn          the column where the worker is
     * @param otherDivinitiesInGame the divinities in the game
     * @param gameCells             the actual board state
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int workerRow, int workerColumn, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCells) {
        return super.getValidCellForBuilding(workerRow, workerColumn, otherDivinitiesInGame, gameCells)
                .stream()
                //deletes the cells where ethe player did the first build
                .filter(cell -> !(cell.getColumn() == oldColumnBuild && cell.getRow() == oldRowBuild))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Generates a list of cells where a certain worker can put a dome excluding the previous cell when it's invoked for the second build.
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
                //deletes the cell where the player completed the first build
                .filter(cell -> !(cell.getColumn() == oldColumnBuild && cell.getRow() == oldRowBuild))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Process the build checking that the player is not trying to build on the same cell
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
     * @return the next fsm controller state
     */
    @Override
    public GameControllerState build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws DivinityPowerException, MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException {

        //checks if it's the first or the second build
        if (!prevBuild) {
            //process the build
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            //saves the first build coordinates
            oldRowBuild = buildRow;
            oldColumnBuild = buildColumn;
            prevBuild = true;
            return new RequestOptionalBuild();
        } else {
            //if the player skipped the optional build
            if (workerRow == -1 && workerColumn == -1) return new TurnEnd();
            //checks if the optional build isn't on the previous cell
            else if (buildRow == oldRowBuild && buildColumn == oldColumnBuild)
                throw new DivinityPowerException("NO!");
            else {
                //process the build and ends the turn
                super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
                return new TurnEnd();
            }
        }
    }

    /**
     * Getter of name
     *
     * @return the divinity's name
     * @author Daniele Mammone
     */
    @Override
    public String getName() {
        return "Demeter";
    }

    /**
     * Process the dome checking that the player isn't adding a dome on the build where he previously built.
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
     * @return the next controller fsm state
     */
    @Override
    public GameControllerState dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        //checks if it's the first build
        if (!prevBuild) {
            //process the dome
            super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
            //saves the dome coordinates
            oldRowBuild = domeRow;
            oldColumnBuild = domeColumn;
            prevBuild = true;
            return new RequestOptionalBuild();
        } else {
            //if the player skipped the optional dome
            if (workerRow == -1 && workerColumn == -1) return new TurnEnd();
            else if (domeRow == oldRowBuild && domeColumn == oldColumnBuild)
                //checks if the player is trying to dome on the first build cell
                throw new DivinityPowerException("NO!");
            else {
                //processes the dome end ends the turn
                super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
                return new TurnEnd();
            }
        }
    }

    /**
     * Getter of divinity's description
     *
     * @return the description of the divinity power
     * @author Annalaura Massa
     */
    @Override
    public String getDescription() {
        return "Your Worker may build one additional time, but not on the same space.";
    }
}
