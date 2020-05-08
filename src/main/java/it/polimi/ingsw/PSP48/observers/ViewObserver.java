package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;
import java.util.Calendar;

public interface ViewObserver {
    public void move(MoveCoordinates p);

    public void build(MoveCoordinates p);

    public void dome(MoveCoordinates p);

    public void putWorkerOnTable(Position p);

    public void registerPlayerDivinity(String divinity);

    public void addPlayer(String playerDetails, Calendar birthday);

    public void selectAvailableDivinities(ArrayList<String> divinities);

    public void selectFirstPlayer(String playerName);
}