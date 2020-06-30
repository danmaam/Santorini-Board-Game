package it.polimi.ingsw.PSP48.server.controller.ControllerState;

import it.polimi.ingsw.PSP48.server.controller.GameController;

import java.util.function.Consumer;

public class PrometheusInitialOptionalBuild implements GameControllerState {
    @Override
    public Consumer<GameController> nextState() {
        return GameController::PrometheusInitialOptionalBuild;
    }
}
