package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.GameData;
import it.polimi.ingsw.PSP48.Worker;
import it.polimi.ingsw.PSP48.Cell;

import java.util.ArrayList;


public class Chronus extends Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = false;

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     */
    @Override
    public boolean winCondition(GameData gd) {
        int numbersOfCompleteTowers = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (gd.getCell(i, j).getLevel() == 3 && gd.getCell(i, j).isDomed()) numbersOfCompleteTowers++;
            }

        }
        return super.winCondition(gd) || numbersOfCompleteTowers >= 5;
    }
}
