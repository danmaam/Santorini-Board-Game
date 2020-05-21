package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.networkMessagesToClient.UpdatedPlayerList;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientNetworkIncoming implements Runnable {
    private ViewInterface playerView;
    private ObjectInputStream in;
    private Socket server;
    private Object newMessage;
    private ClientNetworkOutcoming o;
    final Object syncOfDeath = new Object();
    boolean completedTreadInvocation = true;


    private boolean completeSetup = false;

    ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
    ScheduledExecutorService pingScheduler = Executors.newScheduledThreadPool(1);

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
            synchronized (ClientNetworkIncoming.class) {
                while (!completedTreadInvocation) {
                    try {
                        ClientNetworkIncoming.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            newMessage = in.readObject();
            if (newMessage instanceof NetworkMessagesToClient) {
                completedTreadInvocation = false;
                threadExecutor.submit(() -> {
                    final NetworkMessagesToClient functionParameter = (NetworkMessagesToClient) newMessage;
                    ;
                    synchronized (ClientNetworkIncoming.class) {
                        completedTreadInvocation = true;
                        ClientNetworkIncoming.class.notifyAll();
                    }
                    functionParameter.doAction(playerView);
                });
            } else if (newMessage instanceof ClientSetupMessages) {
                completedTreadInvocation = false;
                for (ClientNetworkObserver o : observers) {
                    threadExecutor.submit(() -> {
                        final ClientSetupMessages functionParameter = (ClientSetupMessages) newMessage;
                        synchronized (ClientNetworkIncoming.class) {
                            completedTreadInvocation = true;
                            ClientNetworkIncoming.class.notifyAll();
                        }
                        functionParameter.doAction(o);
                    });
                }
            } else if (newMessage instanceof PingMessage) {
                pingScheduler.schedule(() -> o.replyPing(), 5, TimeUnit.SECONDS);
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

    public void setPlayerView(ViewInterface playerView) {
        System.out.println("setting: " + playerView);
        this.playerView = playerView;
    }

    public void setOutHandler(ClientNetworkOutcoming out) {
        this.o = out;
    }

    public void shutdown() {
        threadExecutor.shutdown();
        pingScheduler.shutdown();
    }
}
