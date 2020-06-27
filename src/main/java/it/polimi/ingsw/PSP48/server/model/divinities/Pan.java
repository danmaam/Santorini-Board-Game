package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Model;

/**
 * Class that represent simple god Pan
 */
public class Pan extends Divinity {

    /**
     * Method that checks if the divinity can be used in a game with a certain number of players
     * @param pNum the number of players
     * @return true if the game is played by two or three players
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
     * Calculates the player's winning condition according to Pan's power: he can also won if he jump down of two or more levels.
     *
     * @param gd the state of the game
     * @return true if the actual player considered has won, false if the game must go on
     * @author Daniele Mammone
     */
    @Override
    public boolean postMoveWinCondition(Model gd) {
        return (super.postMoveWinCondition(gd) || gd.getCurrentPlayer().getNewLevel() - gd.getCurrentPlayer().getOldLevel() <= -2);
    }


    /**
     * Getter of name
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Pan";
    }

    /**
     * Getter of the divinity's description
     * @return the description of how the divinity's power affects the game
     */
    @Override
    public String getDescription() {
        return "You also win if your Worker moves down two or more levels.";
    }
}