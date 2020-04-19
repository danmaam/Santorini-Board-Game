package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.BuildPosition;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.GameData;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;

public class Zeus extends Divinity {
    private final String name = "Eros";
    private final Boolean threePlayerSupported = true;

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> divinitiesInGame, Cell[][] gameCells) {
        ArrayList<Cell> validCells = super.getValidCellForBuilding(WorkerColumn, WorkerRow, divinitiesInGame, gameCells);
        if (gameCells[WorkerRow][WorkerColumn].getLevel() < 3) validCells.add(gameCells[WorkerRow][WorkerColumn]);
        return validCells;
    }

    /**
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
    public void build(int workerRow, int workerColumn, int buildRow, int buildColumn, GameData gd) throws
            NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, buildRow, buildColumn))) {
            if (workerRow == buildRow && workerColumn == buildColumn && gd.getCell(workerRow, workerColumn).getLevel() == 3)
                throw new MaximumLevelReachedException("Livello massimo già raggiunto");
            else throw new NotAdiacentCellException("Celle non adiacenti");
        }
        //second check: the cell must be empty of workers
        if (!(gd.getCell(buildRow, buildColumn).getPlayer() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be domed
        if (gd.getCell(buildRow, buildColumn).isDomed())
            throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fourth check: if we are not already at the maximum level
        if (3 == gd.getCell(buildRow, buildColumn).getLevel())
            throw new MaximumLevelReachedException("Trying to build on level 3");
        //fifth check: if another different divinity doesn't invalid this move

        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersBuilding(new BuildPosition(workerRow, workerColumn, buildRow, buildColumn, gd.getCell(buildRow, buildColumn).getLevel())))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gd.getCell(buildRow, buildColumn).addLevel();

        //now, the game board has been modified
    }

    @Override
    public String getName() {
        return name;
    }
}
