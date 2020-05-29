package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Class that represents advanced god Zeus
 */
public class Zeus extends Divinity {

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

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> divinitiesInGame, Cell[][] gameCells) {
        ArrayList<Position> validCells = super.getValidCellForBuilding(WorkerColumn, WorkerRow, divinitiesInGame, gameCells);
        if (gameCells[WorkerRow][WorkerColumn].getLevel() < 3) validCells.add(new Position(WorkerRow, WorkerColumn));
        return validCells;
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the game status
     *
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws
            NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, buildRow, buildColumn))) {
            if (workerRow == buildRow && workerColumn == buildColumn && gd.getCell(workerRow, workerColumn).getLevel() == 3)
                throw new MaximumLevelReachedException("Livello massimo già raggiunto");
            else throw new NotAdjacentCellException("Celle non adiacenti");
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

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(buildRow, buildColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));

        //now, the game board has been modified
        return GameController::turnChange;
    }

    /**
     * Getter of name
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Zeus";
    }

    /**
     * Getter of the divinity's description
     * @return the description of how the divinity's power affects the game
     */
    @Override
    public String getDescription() {
        return "Your Worker may build a block under itself.";
    }
}
