package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;

import java.util.ArrayList;

public class UpdatedPlayerList extends NetworkMessagesToClient {
    private ArrayList<String> newPlayerList;

    @Override
    public void doAction(AbstractView v) {
        v.changedPlayerList(newPlayerList);
    }

    public UpdatedPlayerList(ArrayList<String> newPlayerList) {
        this.newPlayerList = (ArrayList<String>) newPlayerList.clone();
    }
}
