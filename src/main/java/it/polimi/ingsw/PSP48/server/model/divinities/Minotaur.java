package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Minotaur extends Divinity {
    private final String name = "Minotaur";
    private final Boolean threePlayerSupported = true;


    @Override
    public String getName() {
        return name;
    }

    /**
     * we need also to return cells occupied
     *
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
        Cell actualWorkerCell = gameCells[WorkerRow][WorkerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[WorkerRow + i][WorkerColumn + j]);
                }
            }
        }


        validCells = validCells.stream()
                //deletes from the valid the cell which are too high or too low to be reached
                .filter(cell -> cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> !cell.isDomed())
                .filter(cell -> cell.getPlayer() == null || !(cell.getPlayer().equals(gameCells[WorkerRow][WorkerColumn].getPlayer())))
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Cell> nV = new ArrayList<>();
        //noe we have to remove occupied cells that can't push away the other worker

        for (Cell c : validCells) {
            direction nextDir = getDirection(c.getRow(), c.getColumn(), WorkerRow, WorkerColumn);
            Position pushingPosition = getNextPosition(c.getRow(), c.getColumn(), nextDir);
            if (!(0 <= pushingPosition.getRow() && pushingPosition.getRow() <= 4) ||
                    !(0 <= pushingPosition.getColumn() && pushingPosition.getColumn() <= 4) ||
                    gameCells[pushingPosition.getRow()][pushingPosition.getColumn()].getPlayer() != null ||
                    gameCells[pushingPosition.getRow()][pushingPosition.getColumn()].isDomed()) nV.add(c);
        }

        for (Cell c : nV) validCells.remove(c);


        //now we have to remove cells where the move is impossible due to other divinity powers

        nV = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : otherDivinitiesInGame) {
                if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), 0))) {
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
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the actual game state
     * @throws NotAdjacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    public Consumer<GameController> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Model gd) throws
            NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gd.getCell(WorkerRow, WorkerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        if (!(moveLevel - workerLevel <= 1))
            throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be occupied
        direction nextDir = getDirection(moveRow, moveColumn, WorkerRow, WorkerColumn);
        Position pushingPosition = null;
        if (!(gd.getCell(moveRow, moveColumn).getPlayer() == null)) {
            if (gd.getCell(moveRow, moveColumn).getPlayer().equals(gd.getCurrentPlayer().getName()))
                throw new OccupiedCellException("Cella occupata da un tuo stesso worker");
            pushingPosition = getNextPosition(moveRow, moveColumn, nextDir);
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
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersMove(new MovePosition(WorkerRow, WorkerColumn, moveRow, moveColumn, moveLevel - moveColumn)))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);
        String tempPlayer = gd.getCell(moveRow, moveColumn).getPlayer();
        gd.getCell(WorkerRow, WorkerColumn).setPlayer(null);
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCurrentPlayer().getName());

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(moveRow, moveColumn).clone());
        changedCell.add((Cell) gd.getCell(WorkerRow, WorkerColumn).clone());


        if (pushingPosition != null) {
            gd.getCell(pushingPosition.getRow(), pushingPosition.getColumn()).setPlayer(tempPlayer);
            changedCell.add((Cell) gd.getCell(pushingPosition.getRow(), pushingPosition.getColumn()));
        }

        gd.notifyObservers(x -> x.changedBoard(changedCell));
        //now, the game board has been modified

        return GameController::requestBuildDome;
    }


}
