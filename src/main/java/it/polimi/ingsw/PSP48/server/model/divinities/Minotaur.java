package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestBuildDome;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementation of Minotaur divinity
 */
public class Minotaur extends Divinity {
    /**
     * Checks if Minotaur is allowed for a certain number of players
     *
     * @param pNum the number of players
     * @return if the divinity is allowed for the specified number of players
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
     * Getter of name
     *
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Minotaur";
    }

    /**
     * Generates a list of cell where a certain worker can move, according to Minotaur's power: he can push away other players' workers
     * on the next cell in the direction of the move, if this cell isn't occupied and if it's not out of the game board
     *
     * @param workerRow             the row where the worker is
     * @param workerColumn          the column where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
        Cell actualWorkerCell = gameCells[workerRow][workerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= workerRow + i && workerRow + i <= 4 && 0 <= workerColumn + j && workerColumn + j <= 4) {
                    validCells.add(gameCells[workerRow + i][workerColumn + j]);
                }
            }
        }


        validCells = validCells.stream()
                //deletes from the valid the cell which are too high or too low to be reached
                .filter(cell -> cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> !cell.isDomed())
                .filter(cell -> cell.getPlayer() == null || !(cell.getPlayer().equals(gameCells[workerRow][workerColumn].getPlayer())))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Cell> nV = new ArrayList<>();
        //noe we have to remove occupied cells that can't push away the other worker

        for (Cell c : validCells) {
            direction nextDir = getDirection(c.getRow(), c.getColumn(), workerRow, workerColumn);
            Position nextPos = getNextPosition(workerRow, workerColumn, nextDir);
            Position pushingPosition = getNextPosition(c.getRow(), c.getColumn(), nextDir);
            if ((!(0 <= pushingPosition.getRow() && pushingPosition.getRow() <= 4) ||
                    !(0 <= pushingPosition.getColumn() && pushingPosition.getColumn() <= 4) ||
                    gameCells[pushingPosition.getRow()][pushingPosition.getColumn()].getPlayer() != null ||
                    gameCells[pushingPosition.getRow()][pushingPosition.getColumn()].isDomed())
                    && gameCells[nextPos.getRow()][nextPos.getColumn()].getPlayer() != null) nV.add(c);
        }

        for (Cell c : nV) validCells.remove(c);


        //now we have to remove cells where the move is impossible due to other divinity powers

        nV = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : otherDivinitiesInGame) {
                if (!d.getName().equals(this.getName()) && !d.othersMove(new ActionCoordinates(workerRow, workerColumn, c.getRow(), c.getColumn()), gameCells)) {
                    nV.add(c);
                    break;
                }
            }
        }

        for (Cell c : nV) validCells.remove(c);

        //now in valid cells there is the list with compatible moves cells

        ArrayList<Position> validPositions = new ArrayList<>();
        validCells.forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));

        return validPositions;
    }

    private direction getDirection(int moveRow, int moveCol, int workRow, int workCol) {
        //case 1: line moving
        if (moveRow == workRow) {
            if (moveCol > workCol) return direction.right;
            else return direction.left;
        }

        //case 2: column moving
        else if (moveCol == workCol) {
            if (moveRow > workRow) return direction.down;
            else return direction.up;
        }

        //case 3: diagonal moving
        else {
            if (moveRow > workRow) {
                if (moveCol > workCol) return direction.downright;
                else return direction.downleft;
            } else {
                if (moveCol < workCol) return direction.upleft;
                else return direction.upright;

            }
        }
    }

    /**
     * @param wR  the row where the worker is
     * @param wC  the column where the worker is
     * @param dir the direction where the worker is moving
     * @return the position of the move, according to the direction of the move
     */
    private Position getNextPosition(int wR, int wC, direction dir) {
        switch (dir) {
            case right:
                return new Position(wR, wC + 1);
            case left:
                return new Position(wR, wC - 1);
            case up:
                return new Position(wR - 1, wC);
            case down:
                return new Position(wR + 1, wC);
            case downleft:
                return new Position(wR + 1, wC - 1);
            case downright:
                return new Position(wR + 1, wC + 1);
            case upright:
                return new Position(wR - 1, wC + 1);
            case upleft:
                return new Position(wR - 1, wC - 1);
        }
        return null;
    }

    /**
     * @param workerRow    the row of the cell where the worker is
     * @param workerColumn the column of the cell where the worker is
     * @param moveRow      the row of the board where the worker wants to move
     * @param moveColumn   the column of the board where the worker wants to move
     * @param gd           the actual game state
     * @return the next controller FSM state
     * @throws DivinityPowerException   if another divinity is blocking the move
     * @throws NotAdjacentCellException if the destination cell is not adjacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    public GameControllerState move(int workerRow, int workerColumn, int moveRow, int moveColumn, Model gd) throws
            NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adjacentCellVerifier(workerRow, workerColumn, moveRow, moveColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gd.getCell(workerRow, workerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        if (!(moveLevel - workerLevel <= 1))
            throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be occupied
        direction nextDir = getDirection(moveRow, moveColumn, workerRow, workerColumn);
        Position pushingPosition = null;
        if (!(gd.getCell(moveRow, moveColumn).getPlayer() == null)) {
            if (gd.getCell(moveRow, moveColumn).getPlayer().equals(gd.getCurrentPlayer().getName()))
                throw new OccupiedCellException("Cella occupata da un tuo stesso worker");
            pushingPosition = getNextPosition(moveRow, moveColumn, nextDir);
            //checks if the cell where the other player should be pushed isn't out of the board, or occupied
            if (!(0 <= pushingPosition.getRow() && pushingPosition.getRow() <= 4) ||
                    !(0 <= pushingPosition.getColumn() && pushingPosition.getColumn() <= 4))
                throw new DivinityPowerException("Cella di push fuori tabellone");
            if (gd.getCell(pushingPosition.getRow(), pushingPosition.getColumn()).getPlayer() != null ||
                    gd.getCell(pushingPosition.getRow(), pushingPosition.getColumn()).isDomed())
                throw new OccupiedCellException("Cella non vuota oppure fuori tabellone. Impossibile usare potere del minotauro");

        }
        //fourth check: the cell must not be domed
        if (gd.getCell(moveRow, moveColumn).isDomed())
            throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fifth check: if another different divinity doesn't invalid this move

        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersMove(new ActionCoordinates(workerRow, workerColumn, moveRow, moveColumn), gd.getGameBoard()))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);
        gd.getCurrentPlayer().setLastWorkerUsed(moveRow, moveColumn);
        String tempPlayer = gd.getCell(moveRow, moveColumn).getPlayer();
        gd.getCell(workerRow, workerColumn).setPlayer(null);
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCurrentPlayer().getName());


        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(moveRow, moveColumn).clone());
        changedCell.add((Cell) gd.getCell(workerRow, workerColumn).clone());


        if (pushingPosition != null) {
            gd.getCell(pushingPosition.getRow(), pushingPosition.getColumn()).setPlayer(tempPlayer);
            changedCell.add(gd.getCell(pushingPosition.getRow(), pushingPosition.getColumn()));
        }

        gd.notifyObservers(x -> x.changedBoard(changedCell));
        //now, the game board has been modified

        return new RequestBuildDome();
    }

    /**
     * Getter of divinity's description
     *
     * @return the description of the divinity power
     */
    @Override
    public String getDescription() {
        return "Your Worker may move into an opponent Worker's space, if their Worker can be forced one space straight backwards to an unoccupied space at any level.";
    }
}
