package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientNetworkIncoming implements Runnable {
    private final ViewInterface playerView;
    private ObjectInputStream in;
    private final Socket server;
    private ClientNetworkOutcoming o;

    private boolean closed;

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
            try {
                server.setSoTimeout(20 * 1000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            retrieveMessage();

        } catch (IOException e) {
            if (!closed) playerView.endgame("Connection to the server lost");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void retrieveMessage() throws IOException, ClassNotFoundException {
        while (true) {
            Object newMessage = in.readObject();
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
        closed = true;
    }
}
