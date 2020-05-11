package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientNetworkIncoming implements Runnable {
    private ViewInterface playerView;
    private ObjectInputStream in;
    private Socket server;
    private NetworkMessagesToClient newMessage;

    private boolean completeSetup = false;

    private ArrayList<ClientNetworkObserver> observers = new ArrayList<>();

    public void addObserver(ClientNetworkObserver n) {
        observers.add(n);
    }

    public void removeObserver(ClientNetworkObserver n) {
        observers.remove(n);
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(server.getInputStream());
            retrieveMessage();
        } catch (IOException e) {
            System.out.println("Disconnected from server");
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void retrieveMessage() throws IOException, ClassNotFoundException {
        while (true) {
            if (!completeSetup) {
                ClientSetupMessages result = (ClientSetupMessages) in.readObject();
                for (ClientNetworkObserver o : observers) result.doAction(o);
            } else {
                System.out.println("waiting for message");
                newMessage = (NetworkMessagesToClient) in.readObject();
                System.out.println("received object" + newMessage.toString());
                newMessage.doAction(playerView);
            }
        }
    }

    public ClientNetworkIncoming(ViewInterface playerView, Socket server) {
        this.playerView = playerView;
        this.server = server;
    }

    public void completedSetup() {
        completeSetup = true;
    }
}
