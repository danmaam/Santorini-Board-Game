package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestMove;
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
 * Class that represents advanced god Hestia
 */
public class Hestia extends Divinity {

    /**
     * Method that checks if the divinity can be used in a game with a certain number of players
     *
     * @param pNum the number of players
     * @return true if the game is played by two or three players
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

    private Boolean alreadyBuilt = false;

    /**
     * Resets if the second action occurred
     *
     * @param gd the game model
     * @return the next action of the controller
     */
    @Override
    public GameControllerState turnBegin(Model gd) {
        alreadyBuilt = false;
        return super.turnBegin(gd);
    }

    /**
     * Gets the cells where a worker can build; Hestia allows an additional build, but not on a perimeter cell
     *
     * @param workerRow             the row where the worker is
     * @param workerColumn          the column where the worker is
     * @param otherDivinitiesInGame the other divinities in game
     * @param gameCell              the game board
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int workerRow, int workerColumn, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCell) {
        return super.getValidCellForBuilding(workerRow, workerColumn, otherDivinitiesInGame, gameCell).stream()
                .filter(cell -> !alreadyBuilt || cell.getColumn() != 0 && cell.getColumn() != 4 && cell.getRow() != 0 && cell.getRow() != 4)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets the cells where a worker can put a dome; Hestia allows an additional build or dome, but not on a perimeter cell
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the other divinities in the game
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellsToPutDome(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellsToPutDome(workerRow, workerColumn, gameCells, divinitiesInGame).stream()
                .filter(cell -> !alreadyBuilt || cell.getColumn() != 0 && cell.getColumn() != 4 && cell.getRow() != 0 && cell.getRow() != 4)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Redefined since Hestia allows an additional build or dome, but not on a perimeter cell
     *
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adjacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        if (!alreadyBuilt) {
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            alreadyBuilt = true;
            return new RequestOptionalBuild();
        } else {
            if (buildRow == 4 || buildColumn == 0 || buildColumn == 4 || buildRow == 0)
                throw new DivinityPowerException("Trying to make the second construction on a perimetral cell");
            else if (buildRow == -1 && buildColumn == -1) return new TurnEnd();
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            return new TurnEnd();
        }
    }

    /**
     * Redefined since Hestia allows an additional build or dome, but not on a perimeter cell
     *
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        if (!alreadyBuilt) {
            super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
            alreadyBuilt = true;
            return new RequestOptionalBuild();
        } else {
            if (domeRow == 4 || domeColumn == 0 || domeColumn == 4 || domeRow == 0)
                throw new DivinityPowerException("Trying to make the second construction on a perimetral cell");
            else if (domeRow == -1 && domeColumn == -1) return new TurnEnd();
            super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
            return new TurnEnd();
        }
    }

    /**
     * Getter of name
     *
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Hestia";
    }

    /**
     * Getter of the divinity's description
     *
     * @return the description of how the divinity's power affects the game
     */
    @Override
    public String getDescription() {
        return "Your Worker may build one additional time, but this cannot be on a perimeter space.";
    }
}
