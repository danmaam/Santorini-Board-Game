package it.polimi.ingsw.PSP48.client;

import java.util.ArrayList;

/**
 * these methods will be called by the ones used to interact with the player, in order to update the server about the changes
 */
public interface ViewObserver
{
    //void move(MoveCoordinates p);
    //void build (MoveCoordinates p);
    //void dome (MoveCoordinates p);
    void selectDivinity(String divinity);
    void addPlayer(String playerDetails);
    void selectColour(String colour);
    void selectAvailableDivinities(ArrayList<String> divinities);
    void selectFirstPlayer(String PlayerName);
}
