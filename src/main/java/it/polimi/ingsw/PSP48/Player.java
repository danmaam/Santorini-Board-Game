package it.polimi.ingsw.PSP48;

import java.util.Calendar;

/**
 * Class used to contain a player's data, such as name and birthday.
 */
public class Player {
    private final Calendar birthday;
    private final String name;


    /**
     * Class constructor
     * @param name String that contains the player's name.
     * @param birthday The player's birthday.
     * @throws IllegalArgumentException if name or birthday are null.
     */
    public Player(String name, Calendar birthday){
        if (name==null || birthday==null){
            throw new IllegalArgumentException("Name and birthday must not be null.");
        }
        else {
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
     * @return the player's birthday
     */
    public Calendar getBirthday() {
        return (Calendar)birthday.clone();
    }

}

