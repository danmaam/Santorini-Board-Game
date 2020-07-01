package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * State where the controller change the turn for initial positioning
 */
public class InitialPositioningTurnChange implements GameControllerState {
    /**
     * Getter of the next method that the controller must execute
     *
     * @return the next method to be invoked on the controller
     */
    @Override
    public Consumer<GameController> nextState() {
        return GameController::initialPositioningTurnChange;
    }

    /**
     * Getter of state's name
     *
     * @return the name of the controller state
     */
    @Override
    public String toString() {
        return "InitialPositioningTurnChange{}";
    }
}
