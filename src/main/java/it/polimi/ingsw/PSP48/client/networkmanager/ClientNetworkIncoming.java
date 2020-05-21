package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientNetworkIncoming implements Runnable {
    private final ViewInterface playerView;
    private ObjectInputStream in;
    private final Socket server;
    private ClientNetworkOutcoming o;


    ScheduledExecutorService pingScheduler = Executors.newScheduledThreadPool(1);

    private final ArrayList<ClientNetworkObserver> observers = new ArrayList<>();

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void retrieveMessage() throws IOException, ClassNotFoundException {
        while (true) {
            Object newMessage = in.readObject();
            System.out.println("Received: " + newMessage);
            if (newMessage instanceof NetworkMessagesToClient) {
                ((NetworkMessagesToClient) newMessage).doAction(playerView);
            } else if (newMessage instanceof ClientSetupMessages) {
                for (ClientNetworkObserver o : observers) ((ClientSetupMessages) newMessage).doAction(o);
            } else if (newMessage instanceof PingMessage) {
                pingScheduler.schedule(() -> o.replyPing(), 5, TimeUnit.SECONDS);
            }
        }

    }

    public ClientNetworkIncoming(ViewInterface playerView, Socket server) {
        this.playerView = playerView;
        this.server = server;
    }



    public void setOutHandler(ClientNetworkOutcoming out) {
        this.o = out;
    }

    public void shutdown() {
        pingScheduler.shutdown();
    }
}
