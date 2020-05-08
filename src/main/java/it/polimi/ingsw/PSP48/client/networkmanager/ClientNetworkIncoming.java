package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientNetworkIncoming implements Runnable {
    private ViewInterface playerView;
    private ObjectInputStream in;
    private Socket server;
    private NetworkMessagesToClient newMessage;

    private boolean nicknameSet = false;
    private boolean gameModeSet = false;

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
            if (!nicknameSet) {
                String result = (String) in.readObject();
                if (!result.equals("Invalid nickname. Retry")) nicknameSet = true;
                for (ClientNetworkObserver o : observers) o.nicknameResult(result);
            } else if (!gameModeSet) {
                String result = (String) in.readObject();
                if (!(result.equals("Not valid mode. Retry") || result.equals("Missing Birthday. Retry")))
                    gameModeSet = true;
                for (ClientNetworkObserver o : observers) o.gameModeResult(result);
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
}
