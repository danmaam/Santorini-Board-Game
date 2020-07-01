package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

/**
 * State where the controller ends the game for some reason. Only used for debugging purposed to check if the controller
 * recognizes the end of a game. Don't use it for other things
 */
public class EndGame implements GameControllerState {
    @Override
    public String toString() {
        return "EndGame{}";
    }

    @Override
    public Consumer<GameController> nextState() {
        return null;
    }
}
