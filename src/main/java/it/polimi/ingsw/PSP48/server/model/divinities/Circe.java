package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.GameData;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Circe extends Divinity {
    private final String name = "Circe";
    private final Boolean threePlayerSupported = false;

    @Override
    public String getName() {
        return name;
    }

    /**
     * stole other player divinity if the the other palyer's workers are not adjacent
     *
     * @param gd the game state
     * @author Daniele Mammone
     */
    @Override
    public void turnBegin(GameData gd) {
        //first, i must rollback the divinities of the players
        for (Player p : gd.getPlayersInGame()) {
            if (p.getTempDivinity() != null) p.restoreTempDivinity();
        }

        // i remove the other player from the list of players
        String otherPlayer = gd.getPlayersInGame().stream().filter(p -> !(p.getName().equals(gd.getCurrentPlayer().getName()))).collect(Collectors.toCollection(ArrayList::new)).get(0).getName();
        //for debug reason
        System.out.println("Other player: " + otherPlayer);
        // there must be present the two workers to apply the power, and there are gods that deletes workers from the board
        //now i must find the player's workers on the board
        ArrayList<Position> workersPosition = gd.getPlayerPositionsInMap(otherPlayer);
        if (workersPosition.size() > 1) {
            //found the two workers, i must check if them are adjacent or not
            if (!adiacentCellVerifier(workersPosition.get(0).getRow(), workersPosition.get(0).getColumn(), workersPosition.get(1).getRow(), workersPosition.get(1).getColumn())) {
                //the workers are not adjacent, so the player stoles the divinity to the other player
                gd.getCurrentPlayer().setTempDivinity(gd.getCurrentPlayer().getDivinity());
                gd.getCurrentPlayer().setDivinity(gd.getPlayer(otherPlayer).getDivinity());
                gd.getPlayer(otherPlayer).setTempDivinity(gd.getPlayer(otherPlayer).getDivinity());
                gd.getPlayer(otherPlayer).setDivinity(new Divinity());
            }
        }
    }
}
