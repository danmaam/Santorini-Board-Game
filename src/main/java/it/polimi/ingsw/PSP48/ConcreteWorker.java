package it.polimi.ingsw.PSP48;
import java.util.ArrayList;

/**
 * Concrete class used to represent the workers in game
 */
public class ConcreteWorker extends Worker {
    int id;
    private final Colour colour;
    private Divinity divinity;
    private Divinity tempDivinity = null;

    /**
     * Class constructor
     * @param colour colour chosen by the player
     * @param id     worker
     */
    public ConcreteWorker(Colour colour, int id) {
        super(colour, id);
        this.colour = colour;
        this.id = id;
    }

    /**
     * Getter of colour
     *
     * @return player colour
     */
    public Colour getColour() {
        return colour;
    }

    //ANCORA DA IMPLEMENTARE
    /**
     * Getter of position
     *
     * @return
     */
    public Cell getPosition() {
        return null;
    }

    /**
     * Getter of divinity
     *
     * @return divinity associated to the worker
     */
    public Divinity getDivinity() {
        return divinity;            //da clonare
    }

    /**
     * Getter of id
     *
     * @return worker id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter of divinty
     *
     * @param newDivinity the divinity to set
     */
    public void setDivinity(Divinity newDivinity) {
        this.divinity = newDivinity;
    }

    /**
     * Setter of tempDivinity
     *
     * @param newDivinity the tempDivinity to set
     */
    public void setTempDivinity(Divinity newDivinity) {
        this.tempDivinity = newDivinity;
    }

    /**
     * Method used to fix the original divinity
     */
    public void fixTempDivinity() {
        this.tempDivinity = null;
    }

    /**
     * Method used to move the worker on the board game
     *
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the cell where the player wants to move
     * @param moveRow      the row of the cell where the player wants to move
     * @param gameCells    the actual state of the board
     * @return the updated cells state
     */
    public ArrayList<Cell> moveWorker(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells) {

        return this.divinity.move(WorkerColumn, WorkerRow, moveColumn, moveRow, gameCells);
    }

    /**
     * Method used to build
     *
     * @param row
     * @param column
     */
    public void buildWorker(int row, int column) {
        this.divinity.build(row, column);
    }

    /**
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells) {
        return this.divinity.getValidCellsToPutDome(workerColumn, workerRow, gameCells);
    }

    /**
     * * @param WorkerColumn   the column where the worker is
     *
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return this.divinity.getValidCellForBuilding(WorkerColumn, WorkerRow, gameCells, divinitiesInGame);
    }

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return this.divinity.getValidCellForMove(WorkerColumn, WorkerRow, gameCells, divinitiesInGame);
    }
}

