package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Atlas extends Divinity {

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
     * Generates a list of valid cells where a certain worker can build
     * Redefined method since Atlas can add a dome on each level     *
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the divinities that are in game
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellsToPutDome(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= workerRow + i && workerRow + i <= 4 && 0 <= workerColumn + j && workerColumn + j <= 4) {
                    validCells.add(gameCells[workerRow + i][workerColumn + j]);
                }
            }
        }

        validCells = validCells.stream()
                .filter(cell -> cell.getPlayer() == null)
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        for (Divinity d : divinitiesInGame) {
            validCells.removeIf(c -> !d.getName().equals(this.getName()) && !d.othersDome(new MoveCoordinates(workerRow, workerColumn, c.getRow(), c.getColumn())));

        }

        ArrayList<Position> validPositions = new ArrayList<>();
        validCells.forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));

        return validPositions;
    }

    /**
     * Overriden since Atlas can add a dome on each level
     *
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the game status
     * @return the next method that the Game Controller must call
     * @throws NotAdjacentCellException if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException    if the destination cell is occupied by another worker
     * @throws DomedCellException       is the cell is already domed
     * @throws DivinityPowerException   if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adjacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, domeRow, domeColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the cell must be empty of workers
        if (!(gd.getCell(domeRow, domeColumn).getPlayer() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be already domed
        if (gd.getCell(domeRow, domeColumn).isDomed())
            throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fifth check: if another different divinity doesn't invalid this move


        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersDome(new MoveCoordinates(workerRow, workerColumn, domeRow, domeColumn)))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gd.getCell(domeRow, domeColumn).addDome();
        //now, the game has been modified

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(domeRow, domeColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));

        return GameController::turnEnd;
    }


    @Override
    public String getName() {
        return "Atlas";
    }

    @Override
    public String getDescription() {
        return "Your Worker may build a dome at any level.";
    }
}
