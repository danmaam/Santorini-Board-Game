package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

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
