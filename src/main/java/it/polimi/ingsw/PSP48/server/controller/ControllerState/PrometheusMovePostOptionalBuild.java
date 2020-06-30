package it.polimi.ingsw.PSP48.server.controller.ControllerState;


import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

public class PrometheusMovePostOptionalBuild implements GameControllerState {
    @Override
    public Consumer<GameController> nextState() {
        return GameController::PrometheusMovePostOptionalBuild;
    }

    @Override
    public String toString() {
        return "PrometheusMovePostOptionalBuild{}";
    }
}
