package it.polimi.ingsw.PSP48.server.model;

import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;

import java.util.Calendar;

/**
 * Class used to contain a player's data, such as name, birthday, colour, divinity and workers.
 * When the Player has Circe as Divinity and her power is activated, the opponent's divinity is set as tempDivinity.
 * At the end of the player's turn, tempDivinity value is restored to null.
 */
public class Player {
    private final Calendar birthday;
    private final String name;
    private Colour colour;
    private Divinity divinity;
    private Divinity tempDivinity;
    private int oldLevel;
    private int workersOnTable;
    private int newLevel;
    private Position lastWorkerMoved = new Position(-1, -1);

    /**
     * Method used to increment the number of workers on the board.
     */
    public void addWorker() {
        this.workersOnTable++;
    }

    /**
     * Method used to decrease the number of workers on the board.
     * @throws IllegalStateException if there are no workers left.
     */
    public void removeWorker() throws IllegalStateException {
        if (workersOnTable == 0) throw new IllegalStateException("trying to remove a worker that doesn't exists");
        else this.workersOnTable--;
    }

    /**
     * Getter of the number of the worker on the board
     * @return number of workers in game
     */
    public int getWorkersOnTable() {
        return workersOnTable;
    }

    /**
     * Getter of oldLevel
     * @return the old level
     */
    public int getOldLevel() {
        return oldLevel;
    }

    /**
     * Getter of new level
     * @return the new level
     */
    public int getNewLevel() {
        return newLevel;
    }

    /**
     * Setter of oldLevel
     * @param oldLevel the level to set
     */
    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    /**
     * Setter of newLevel
     * @param newLevel the level to set
     */
    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    /**
     * Class constructor
     * @param name     String that contains the player's name.
     * @param birthday The player's birthday.
     * @throws IllegalArgumentException if name or birthday are null.
     */
    public Player(String name, Calendar birthday, boolean divinities, Colour c) {
        if (name == null || (birthday == null && !divinities)) {
            throw new IllegalArgumentException("Name and birthday must not be null.");
        } else {
            this.name = name;
            this.birthday = birthday;
            this.colour = c;
            workersOnTable = 0;
        }
    }

    /**
     * Getter of name
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of birthday
     * @return the player's birthday
     */
    public Calendar getBirthday() {
        return birthday;
    }

    /**
     * Getter of colour
     * @return player colour
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * setter of the colour
     * @param newColour is the new colour of the player
     */
    public void setColour(Colour newColour)
    {
        this.colour=newColour;
    }

    /**
     * Getter of divinity
     * @return divinity associated to the worker
     */
    public Divinity getDivinity() {
        return divinity;
    }

    /**
     * Setter of divinity
     * @param newDivinity the divinity to set
     */
    public void setDivinity(Divinity newDivinity) {
        this.divinity = newDivinity;
    }

    /**
     * Setter of tempDivinity
     * @param newDivinity the tempDivinity to set
     */
    public void setTempDivinity(Divinity newDivinity) {
        this.tempDivinity = newDivinity;
    }

    /**
     * Getter of last moved worker
     * @return the position of the last moved worker
     */
    public Position getLastWorkerMoved() {
        return lastWorkerMoved;
    }

    /**
     * Getter of tempDivinity
     * @return the divinity temporary asssociated to the player
     */
    public Divinity getTempDivinity() {
        return tempDivinity;
    }

    /**
     * Method used to restore the original divinity
     */
    public void restoreTempDivinity() {
        divinity = tempDivinity;
        tempDivinity = null;
    }

    /**
     * Method used to reset the position of the last used worker
     */
    public void resetLastWorkerUsed() {
        lastWorkerMoved = new Position(-1, 1);
    }

    /**
     * Method used to set the position of the last used worker
     * @param row    the worker row
     * @param column the worker column
     */
    public void setLastWorkerUsed(int row, int column) {
        lastWorkerMoved = new Position(row, column);
    }
}



