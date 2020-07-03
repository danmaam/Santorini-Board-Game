package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implementation of Circe Divinity
 * @author Daniele Mammone
 */
public class Circe extends Divinity {

    /**
     * Checks if Circe is allowed for a certain number of players
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
     * Getter of name
     *
     * @return the divinity's name
     * @author Daniele Mammone
     */
    @Override
    public String getName() {
        return "Circe";
    }


    /**
     * At the beginning of each Circe turn, restores the original players' divinities situation.
     * Then, stole other player divinity if the the other player's workers are not adjacent.
     * Then, checks if the player can end the turn
     *
     * @param gd the model
     * @return the next controller FSN state
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState turnBegin(Model gd) {
        //first, i must rollback the divinities of the players
        // i remove the other player from the list of players
        String otherPlayer = gd.getPlayersInGame().stream().filter(p -> !(p.getName().equals(gd.getCurrentPlayer().getName()))).collect(Collectors.toCollection(ArrayList::new)).get(0).getName();
        // there must be present almost two workers to apply the power, and there are gods that deletes workers from the board
        //now i must find the player's workers on the board and must check if them are adjacent or not
        ArrayList<Position> workersPosition = gd.getPlayerPositionsInMap(otherPlayer);
        if (workersPosition.size() > 1 && !adjacentCellVerifier(workersPosition.get(0).getRow(), workersPosition.get(0).getColumn(), workersPosition.get(1).getRow(), workersPosition.get(1).getColumn())) {
            //there are almost two workers and they are not not adjacent, so the player stoles the divinity to the other player
            gd.getCurrentPlayer().setTempDivinity(gd.getCurrentPlayer().getDivinity());
            gd.getCurrentPlayer().setDivinity(gd.getPlayer(otherPlayer).getDivinity());
            gd.getPlayer(otherPlayer).setTempDivinity(gd.getPlayer(otherPlayer).getDivinity());
            gd.getPlayer(otherPlayer).setDivinity(new Divinity());
            gd.sendPlayerList();
            return gd.getCurrentPlayer().getDivinity().turnBegin(gd);

        }
        return super.turnBegin(gd);
    }

    /**
     * Getter of divinity's description
     * @author Annalaura Massa
     * @return the description of the divinity power
     */
    @Override
    public String getDescription() {
        return "If an opponent's Workers do not neighbor each other, you alone have use of their power until your next turn.";
    }

    /**
     * Before each Circe turn, restores all players' original divinity
     * @author Daniele Mammone
     * @param model the model of the game
     */
    @Override
    public void preTurnSecondaryDivinityChecks(Model model) {
        for (Player p : model.getPlayersInGame()) {
            if (p.getTempDivinity() != null) p.restoreTempDivinity();
        }
        model.sendPlayerList();
    }
}
