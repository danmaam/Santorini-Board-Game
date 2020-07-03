package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Model;

/**
 * Implementation of Chronus Divinity
 * @author Daniele Mammone
 */
public class Chronus extends Divinity {
    /**
     * Checks if Chronus is allowed for a certain number of players
     *
     * @param pNum the number of players
     * @return if the divinity is allowed for the specified number of players
     * @author Daniele Mammone
     */
    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return false;
        }
        return false;
    }


    /**
     * Calculates win condition after a build happened, according to Chronus' power.
     *
     * @param gd the model
     * @return true if the player has won
     * @author Daniele Mammone
     */
    @Override
    public boolean postBuildWinCondition(Model gd) {
        int numbersOfCompleteTowers = 0;
        //calculates the number of complete towers
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (gd.getCell(i, j).getLevel() == 3 && gd.getCell(i, j).isDomed()) numbersOfCompleteTowers++;
            }

        }
        return numbersOfCompleteTowers >= 5;
    }

    /**
     * Getter of name
     *
     * @return the divinity's name
     * @author Daniele Mammone
     */
    @Override
    public String getName() {
        return "Chronus";
    }

    /**
     * Getter of divinity's description
     *
     * @return the description of the divinity power
     * @author Annalaura Massa
     */
    @Override
    public String getDescription() {
        return "You also win when there are at least five Complete Towers on the board.";
    }
}
