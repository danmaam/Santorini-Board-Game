package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * State of the controller where a player that can't end his turn is removed from the game
 */
public class CurrentPlayerCantEndTurn implements GameControllerState {

    /**
     * Getter of the next method that the controller must execute
     *
     * @return the next method to be invoked on the controller
     */
    @Override
    public Consumer<GameController> nextState() {
        return GameController::currentPlayerCantEndTurn;
    }

    /**
     * Getter of state's name
     *
     * @return the name of the controller state
     */
    @Override
    public String toString() {
        return "CurrentPlayerCantEndTurn{}";
    }
}
