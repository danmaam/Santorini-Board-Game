package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

import java.util.ArrayList;

public class UpdatedPlayerList extends NetworkMessagesToClient {
    private ArrayList<String> newPlayerList;

    @Override
    public void doAction(ViewInterface v) {
        v.changedPlayerList(newPlayerList);
    }

    public UpdatedPlayerList(ArrayList<String> newPlayerList) {
        this.newPlayerList = (ArrayList<String>) newPlayerList.clone();
    }
}
