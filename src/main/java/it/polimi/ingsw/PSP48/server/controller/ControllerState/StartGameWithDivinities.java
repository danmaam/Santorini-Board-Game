package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

public class StartGameWithDivinities implements GameControllerState {
    @Override
    public Consumer<GameController> nextState() {
        return GameController::startGameWithDivinities;
    }

    @Override
    public String toString() {
        return "StartGameWithDivinities{}";
    }
}