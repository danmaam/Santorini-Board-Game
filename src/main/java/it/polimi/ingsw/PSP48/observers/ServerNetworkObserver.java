package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

public interface ServerNetworkObserver {
    public void move(MoveCoordinates p);

    public void build(MoveCoordinates p);

    public void dome(MoveCoordinates p);

    public void putWorkerOnTable(Position p);

    public void registerPlayerDivinity(String divinity);

    public void selectAvailableDivinities(ArrayList<String> divinities);

    public void firstPlayerRegistration(String player);

    public void processNickname(String nickname);

    public void processGameMode(String gameMode);
}
