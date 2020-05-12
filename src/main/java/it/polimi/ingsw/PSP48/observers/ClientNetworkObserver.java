package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.io.IOException;
import java.util.ArrayList;

public interface ClientNetworkObserver {
    public void nicknameResult(String result);

    public void gameModeResult(String result);

    public void requestNicknameSend(String message);

    public void requestGameModeSend(String message);

    public void completedSetup(String message);

}
