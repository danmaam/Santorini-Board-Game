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
     * @param gd the state of the game
     * @return true if the actual player considered has won, false if the game must go on
     */
    @Override
    public boolean winCondition(Model gd) {
        int numbersOfCompleteTowers = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (gd.getCell(i, j).getLevel() == 3 && gd.getCell(i, j).isDomed()) numbersOfCompleteTowers++;
            }

        }
        return super.winCondition(gd) || numbersOfCompleteTowers >= 5;
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
