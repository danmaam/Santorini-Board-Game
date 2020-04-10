package it.polimi.ingsw.PSP48.model;

import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.Birthday;
import it.polimi.ingsw.PSP48.model.Colour;

/**
 * Class used to contain a player's data, such as name and birthday.
 */
public class Player {
    private final Birthday birthday;
    private final String name;
    private Colour colour;
    private Divinity divinity;
    private Divinity tempDivinity;
    private int oldLevel;
    private int newLevel;

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public void setOldLevel(int oldLevel) {
        this.oldLevel = oldLevel;
    }

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    /**
     * Class constructor
     *
     * @param name     String that contains the player's name.
     * @param birthday The player's birthday.
     * @throws IllegalArgumentException if name or birthday are null.
     */
    public Player(String name, Birthday birthday) {
        if (name == null || birthday == null) {
            throw new IllegalArgumentException("Name and birthday must not be null.");
        } else {
            this.name = name;
            this.birthday = birthday;
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
     *
     * @return the player's birthday
     */
    public Birthday getBirthday() {
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
     *
     * @param newDivinity the tempDivinity to set
     */
    public void setTempDivinity(Divinity newDivinity) {
        this.tempDivinity = newDivinity;
    }

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
}



