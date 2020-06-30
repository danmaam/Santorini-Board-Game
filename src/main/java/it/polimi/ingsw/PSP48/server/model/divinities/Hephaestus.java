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

public class Hephaestus extends Divinity {

    private int prevBuildRow = -1;
    private int prevBuildColumn = -1;

    private boolean prevBuild = false;

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
     * Generates a list of cell where a certain worker can build, according to Hephaestus' power, since he can
     * do an optional build on the same cell.
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param divinitiesInGame the divinities in the game
     * @param gameCells        the actual board state
     * @return a list of cell valid for the building of the worker
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int workerRow, int workerColumn, ArrayList<Divinity> divinitiesInGame, Cell[][] gameCells) {
        ArrayList<Position> validCells = super.getValidCellForBuilding(workerRow, workerColumn, divinitiesInGame, gameCells);
        if (prevBuildRow != -1 && prevBuildColumn != -1) validCells = validCells.stream()
                .filter(cell -> cell.getRow() == prevBuildRow && cell.getColumn() == prevBuildColumn)
                .collect(Collectors.toCollection(ArrayList::new));
        return validCells;
    }


    /**
     * Redefined since it has to check if we are trying to perform the second build on cells different from the first.
     * If the player build a third level, return a turn change since he can't complete a second build.
     *
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the game status
     * @return the next action of the controller
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws DivinityPowerException, MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        if (!prevBuild) {
            super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
            prevBuildRow = buildRow;
            prevBuildColumn = buildColumn;
            prevBuild = true;
            if (gd.getCell(buildRow, buildColumn).getLevel() < 3) return new RequestOptionalBuild();
            else return new TurnEnd();
        } else {
            if (workerRow == -1 && workerColumn == -1) return new TurnEnd();
            else if (buildRow != prevBuildRow && buildColumn != prevBuildColumn)
                throw new DivinityPowerException("NO!");
            else {
                super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
                return new TurnEnd();
            }
        }
    }

    /**
     * Generates a list of cells where a certain worker can put a dome, according to Eros' power: he can put a dome only at the first construction
     * operation.
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the divinities in game
     * @return a list of cell where the player can put a dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellsToPutDome(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        if (prevBuildColumn == -1 && prevBuildRow == -1)
            return super.getValidCellsToPutDome(workerRow, workerColumn, gameCells, divinitiesInGame);
        else return new ArrayList<>();
    }

    /**
     * Requires the model to register a dome operation performed by the player
     *
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @return the next controller FSM state
     * @throws NotAdjacentCellException        if player is trying to put a dome on a cell not adjacent to the worker
     * @throws OccupiedCellException           if player is trying to put a dome on a occupied cell
     * @throws DomedCellException              if player is trying to put a dome on an already domed cell
     * @throws MaximumLevelNotReachedException if the player is trying to put a dome on a cell with level <= 2
     * @throws DivinityPowerException          if another divinity blocks the dome, or if the player is trying to put a dome as second operation.
     */
    @Override
    public GameControllerState dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        if (prevBuild) throw new DivinityPowerException("Trying to add a dome as the second build!");
        return super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
    }

    @Override
    public String getName() {
        return "Hephaestus";
    }

    @Override
    public String getDescription() {
        return "Your Worker may build one additional block (not dome) on top of your first block.";
    }

    @Override
    public GameControllerState turnBegin(Model gd) {
        prevBuildRow = -1;
        prevBuildColumn = -1;
        prevBuild = false;
        return super.turnBegin(gd);
    }
}
