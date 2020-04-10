package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.Cell;
import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Hephaestus extends Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;
    private int prevBuildRow = -1;
    private int prevBuildColumn = -1;

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> divinitiesInGame, Cell[][] gameCells) {
        ArrayList<Cell> validCells = super.getValidCellForBuilding(WorkerColumn, WorkerRow, divinitiesInGame, gameCells);
        if (prevBuildRow != -1 && prevBuildColumn != -1) validCells = validCells.stream()
                .filter(cell -> cell.getRow() == prevBuildRow && cell.getColumn() == prevBuildColumn)
                .collect(Collectors.toCollection(ArrayList::new));
        return validCells;
    }


    /**
     * Redefined since it has to check if we are trying to perform the second build on cells different from the first
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
        if (prevBuildRow != -1 && prevBuildColumn != -1 && !(buildRow == prevBuildRow && buildColumn == prevBuildColumn))
            throw new DivinityPowerException("Trying to perform the second build on a different cell from the first");
        super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
        prevBuildColumn = buildColumn;
        prevBuildRow = buildRow;
    }

    /**
     * @param workerColumn     the column where the worker is
     * @param workerRow        the row where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the divinities in game
     * @return a list of cell where the player can put a dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        if (prevBuildColumn == -1 && prevBuildRow == -1)
            return super.getValidCellsToPutDome(workerColumn, workerRow, gameCells, divinitiesInGame);
        else return new ArrayList<>();
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the game status
     * @throws NotAdiacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public void dome(int workerRow, int workerColumn, int domeRow, int domeColumn, GameData gd) throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException {
        if (prevBuildRow != -1 && prevBuildColumn != -1)
            throw new DivinityPowerException("Impossibile aggiungere qua cupola");
        super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
        prevBuildRow = domeRow;
        prevBuildColumn = domeColumn;
    }

    @Override
    public String getName() {
        return name;
    }
}
