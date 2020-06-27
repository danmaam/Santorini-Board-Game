package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Model;


public class Chronus extends Divinity {
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
     */
    @Override
    public boolean postBuildWinCondition(Model gd) {
        int numbersOfCompleteTowers = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (gd.getCell(i, j).getLevel() == 3 && gd.getCell(i, j).isDomed()) numbersOfCompleteTowers++;
            }

        }
        return numbersOfCompleteTowers >= 5;
    }

    @Override
    public String getName() {
        return "Chronus";
    }

    @Override
    public String getDescription(){
        return "You also win when there are at least five Complete Towers on the board.";
    }
}
