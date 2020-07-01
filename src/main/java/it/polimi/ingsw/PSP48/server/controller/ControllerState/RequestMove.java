package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * The state where game controller requests a player to do a move
 */
public class RequestMove implements GameControllerState {
    /**
     * Getter of the next method that the controller must execute
     *
     * @return the next method to be invoked on the controller
     */
    @Override
    public Consumer<GameController> nextState() {
        return GameController::requestMove;
    }

    /**
     * Getter of state's name
     *
     * @return the name of the controller state
     */
    @Override
    public String toString() {
        return "RequestMove{}";
    }
}
