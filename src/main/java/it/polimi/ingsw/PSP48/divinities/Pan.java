package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.GameData;
import it.polimi.ingsw.PSP48.Worker;

import java.util.ArrayList;

public class Pan extends Divinity {
    private final String name = "Pan";
    private final Boolean threePlayerSupported = true;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     * @author Daniele Mammone
     */
    @Override
    public boolean winCondition(GameData gd) {
        return (super.winCondition(gd) || newLevel - oldLevel <= -2);
    }
}