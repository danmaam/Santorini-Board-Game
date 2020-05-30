package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Class that represents advanced god Hestia
 */
public class Hestia extends Divinity {

    /**
     * Method that checks if the divinity can be used in a game with a certain number of players
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
     * don't do anything since without a divinity there isn't a modifier
     *
     * @param gd
     * @return the next action of the controller
     */
    @Override
    public Consumer<GameController> turnBegin(Model gd) {
        alreadyBuilt = false;
        return GameController::requestMove;
    }

    /**
     * Gets the cells where a worker can build; redefined since Hestia allows an additional build, but not on a perimetric cell
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param otherDivinitiesInGame the other divinities in game
     * @param gameCell              the game board
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCell) {
        return super.getValidCellForBuilding(WorkerColumn, WorkerRow, otherDivinitiesInGame, gameCell).stream()
                .filter(cell -> !alreadyBuilt || cell.getColumn() != 0 && cell.getColumn() != 4 && cell.getRow() != 0 && cell.getRow() != 4)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets the cells where a worker can put a dome; redefined since Hestia allows an additional build or dome, but not on a perimetric cell
     * @param workerColumn     the column where the worker is
     * @param workerRow        the row where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellsToPutDome(workerColumn, workerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> !alreadyBuilt || cell.getColumn() != 0 && cell.getColumn() != 4 && cell.getRow() != 0 && cell.getRow() != 4)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Redefined since Hestia allows an additional build or dome, but not on a perimetric cell
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        if (!alreadyBuilt) {
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            alreadyBuilt = true;
            return GameController::requestOptionalBuilding;
        } else {
            if (buildRow == 4 || buildColumn == 0 || buildColumn == 4 || buildRow == 0)
                throw new DivinityPowerException("Trying to make the second construction on a perimetral cell");
            else if (buildRow == -1 && buildColumn == -1) return GameController::turnChange;
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            return GameController::turnChange;
        }
    }

    /**
     * Redefined since Hestia allows an additional build or dome, but not on a perimetric cell
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
    public Consumer<GameController> dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        if (!alreadyBuilt) {
            super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
            alreadyBuilt = true;
            return GameController::requestOptionalBuilding;
        } else {
            if (domeRow == 4 || domeColumn == 0 || domeColumn == 4 || domeRow == 0)
                throw new DivinityPowerException("Trying to make the second construction on a perimetral cell");
            else if (domeRow == -1 && domeColumn == -1) return GameController::turnChange;
            super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
            return GameController::turnChange;
        }
    }

    /**
     * Getter of name
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Hestia";
    }

    /**
     * Getter of the divinity's description
     * @return the description of how the divinity's power affects the game
     */
    @Override
    public String getDescription() {
        return "Your Worker may build one additional time, but this cannot be on a perimeter space.";
    }
}
