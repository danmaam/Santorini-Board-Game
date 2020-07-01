package it.polimi.ingsw.PSP48.server.controller.ControllerState;


import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * State in which the controller request a player with prometheus to do a move after he completed the optional build
 */
public class PrometheusMovePostOptionalBuild implements GameControllerState {
    /**
     * Getter of the next method that the controller must execute
     *
     * @return the next method to be invoked on the controller
     */
    @Override
    public Consumer<GameController> nextState() {
        return GameController::PrometheusMovePostOptionalBuild;
    }

    /**
     * Getter of state's name
     *
     * @return the name of the controller state
     */
    @Override
    public String toString() {
        return "PrometheusMovePostOptionalBuild{}";
    }
}
