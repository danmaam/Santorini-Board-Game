package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.client.Client;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientNetworkIncoming implements Runnable {

    private Socket server;
    private AbstractView playerView;
    private ObjectInputStream in;
    private NetworkMessagesToClient newMessage;

    @Override
    public void run() {
        try {
            retrieveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void retrieveMessage() throws IOException, ClassNotFoundException {
        in = new ObjectInputStream(server.getInputStream());
        while (true) {
            newMessage = (NetworkMessagesToClient) in.readObject();
            newMessage.doAction(playerView);
        }
    }

    public ClientNetworkIncoming(Socket server, AbstractView playerView) {
        this.server = server;
        this.playerView = playerView;
    }
}
