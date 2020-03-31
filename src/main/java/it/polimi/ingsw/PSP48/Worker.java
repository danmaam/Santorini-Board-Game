package it.polimi.ingsw.PSP48;
import java.util.ArrayList;

public abstract class Worker {
    int id;
    private final Colour colour;
    private Divinity divinity = null;
    private Divinity tempDivinity = null;

    /**
     * Class constructor
     *
     * @param colour colour chosen by the player
     * @param id     worker
     */
    public Worker(Colour colour, int id) {
        this.colour = colour;
        this.id = id;
    }

    /**
     * Getter of colour
     *
     * @return player colour
     */
    public abstract Colour getColour();

    /**
     * Getter of position
     *
     * @return
     */
    public abstract Cell getPosition();

    /**
     * Getter of id
     *
     * @return worker id
     */
    public abstract int getId();

    /**
     * Getter of divinity
     *
     * @return divinity associated to the worker
     */
    public abstract Divinity getDivinity();

    /**
     * Setter of Divinity
     *
     * @param newDivinity the tempDivinity to set
     */
    public abstract void setDivinity(Divinity newDivinity);

    /**
     * Setter of tempDivinity
     *
     * @param newDivinity the tempDivinity to set
     */
    public abstract void setTempDivinity(Divinity newDivinity);

    /**
     * Method used to fix the original divinity
     */
    public abstract void fixTempDivinity();

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
    public abstract ArrayList<Cell> moveWorker(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells);

    /**
     * Method used to build
     *
     * @param row
     * @param column
     */
    public abstract void buildWorker(int row, int column);

    /**
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    public abstract ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells);

    /**
     * * @param WorkerColumn     the column where the worker is
     *
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    public abstract ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame);

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    public abstract ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame);
}

