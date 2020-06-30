package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * Interface of game controller fsm state
 */
public interface GameControllerState {
    public Consumer<GameController> nextState();
}
