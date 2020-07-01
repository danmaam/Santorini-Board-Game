package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * Interface of game controller fsm state
 */
public interface GameControllerState {
    /**
     * Getter of the next method that the controller must execute
     *
     * @return the next method to be invoked on the controller
     */
    public Consumer<GameController> nextState();

    /**
     * Getter of state's name
     *
     * @return the name of the controller state
     */
    public String toString();
}
