package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface of an observer of client's network managers
 */
public interface ClientNetworkObserver {
    /**
     * The result from server of the processing of the nickname
     *
     * @param result the result
     */
    public void nicknameResult(String result);

    /**
     * The result from server of the processing of the gamemode
     *
     * @param result the result
     */
    public void gameModeResult(String result);

    /**
     * Used by the server to request the send of the player's nickname
     *
     * @param message the player's nickname
     */
    public void requestNicknameSend(String message);

    /**
     * Used by the server to request the send of the player's nickname
     *
     * @param message the game mode chosen by the player
     */
    public void requestGameModeSend(String message);

    /**
     * Notifies the client that the setup phase is completed
     *
     * @param message the message of completed setup
     */
    public void completedSetup(String message);

}
