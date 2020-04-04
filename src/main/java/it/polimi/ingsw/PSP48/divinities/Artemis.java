package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Artemis extends Divinity {
    private final String name = "Artemis";
    private final Boolean threePlayerSupported = true;

    private int oldRowMove = -1;
    private int oldColumnMove = -1;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * reset the last move coordinate
     */
    @Override
    public void turnBegin(GameData gd) {
        oldColumnMove = -1;
        oldRowMove = -1;
    }

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellForMove(WorkerColumn, WorkerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> cell.getColumn() != oldColumnMove && cell.getRow() != oldRowMove)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Overriden since Artemis allows a second move, but not on the previous cell
     *
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the game status
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn)))
            throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gd.getCell(WorkerRow, WorkerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        if (!(moveLevel - workerLevel <= 1))
            throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be occupied
        if (!(gd.getCell(moveRow, moveColumn).getPlayer() == null)) throw new OccupiedCellException("Cella occupata");
        //fourth check: the cell must not be domed
        if (gd.getCell(moveRow, moveColumn).isDomed())
            throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fifth check: if another different divinity doesn't invalid this move

        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersMove(new MovePosition(WorkerRow, WorkerColumn, moveRow, moveColumn, moveLevel - moveColumn)))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, we must check if it's the first move or the second move
        if (oldRowMove == -1 && oldColumnMove == -1) {
            //if it's the first move, save the coordinates of original worker position
            oldRowMove = WorkerRow;
            oldColumnMove = WorkerColumn;
        } else {
            //if the worker is trying to turn back on original cell, throws an exception
            if (oldRowMove == moveRow && oldColumnMove == moveColumn)
                throw new DivinityPowerException("Trying to return to original cell");
        }

        //than the move is valid and the game bord can be modified
        oldLevel = workerLevel;
        newLevel = moveLevel;
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCell(WorkerRow, WorkerColumn).getPlayer());
        gd.getCell(WorkerRow, WorkerColumn).setPlayer(null);

        //now, the game board has been modified
    }

}
