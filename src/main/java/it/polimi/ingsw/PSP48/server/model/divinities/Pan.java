package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Model;

public class Pan extends Divinity {
    private final String name = "Pan";
    private final Boolean threePlayerSupported = true;


    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     * @author Daniele Mammone
     */
    @Override
    public boolean winCondition(Model gd) {
        return (super.winCondition(gd) || gd.getCurrentPlayer().getNewLevel() - gd.getCurrentPlayer().getOldLevel() <= -2);
    }

    @Override
    public String getName() {
        return name;
    }

}