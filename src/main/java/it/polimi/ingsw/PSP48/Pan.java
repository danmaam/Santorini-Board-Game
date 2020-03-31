package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

public class Pan extends Divinity {
    private final String name = "Pan";
    private final Boolean threePlayerSupported = true;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * @param playerWorkers the workers of the actual player
     * @return true if the actual player considererd has won, false if the game must go on
     */
    @Override
    public boolean winCondition(ArrayList<Worker> playerWorkers) {
        return (super.winCondition(playerWorkers) || newLevel - oldLevel <= -2);
    }
}