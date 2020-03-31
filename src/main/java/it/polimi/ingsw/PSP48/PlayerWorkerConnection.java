package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * class that contains the association between the player and its workers.
 */
public class PlayerWorkerConnection {
    private Player player;
    private ArrayList<Worker> worker;

    /**
     * Class constructor
     *
     * @param player a player in the game
     * @param worker list of the worker that have been chosen by the player.
     */
    public PlayerWorkerConnection(Player player, ArrayList<Worker> worker) {
        this.player = player;
        this.worker = worker;
    }

    /**
     * Getter of player
     *
     * @return reference to the player
     */
    public Player getPlayer() {
        return player;      //da clonare
    }

    /**
     * Getter of worker
     *
     * @return the list of the workers associated to the player.
     */
    public ArrayList<Worker> getWorkerList() {
        return worker;      //da clonare
    }

    /**
     * Setter of player
     *
     * @param newPlayer
     */
    public void setPlayer(Player newPlayer) {
        this.player = newPlayer;
    }

    /**
     * Setter of worker
     *
     * @param w list of workers that has to be associated with the player.
     */
    public void setWorker(ArrayList<Worker> w) {
        this.worker = w;
    }
}
