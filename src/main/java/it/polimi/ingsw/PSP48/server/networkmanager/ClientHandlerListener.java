package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.networkMessagesToServer.NetworkMessagesToServer;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;
import javafx.concurrent.ScheduledService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHandlerListener implements Runnable {

    private Object nextMessage;

    private boolean setNickname = false;
    private boolean setGameMode = false;
    private boolean setClosed = false;

    private final Socket clientSocket;
    private ClientHandler out;

    private final ArrayList<ServerNetworkObserver> observers = new ArrayList<>();

    public void registerObserver(ServerNetworkObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ServerNetworkObserver obv) {
        observers.remove(obv);
    }

    private ExecutorService executors = Executors.newSingleThreadExecutor();
    private ScheduledExecutorService pingExecutor = Executors.newScheduledThreadPool(1);

    public void notifyObservers() {
        for (ServerNetworkObserver nO : observers) {
            ((NetworkMessagesToServer) nextMessage).doThings(nO);
        }
    }

    public ClientHandlerListener(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try {
            clientSocket.setSoTimeout(9999 * 1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            waitForMessages();
        } catch (SocketTimeoutException e) {
            if (clientSocket.isConnected()) {

                try {
                    clientSocket.close();
                    System.out.println("Disconnected for inactivity");
                    for (ServerNetworkObserver o : observers) {
                        o.destroyGame();
                    }

                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }
            }
        } catch (IOException e) {
            //means that the connection dropped. i must close the game.
            if (!setClosed) {
                for (ServerNetworkObserver o : observers) {
                    o.destroyGame();
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void waitForMessages() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        while (true) {
            nextMessage = input.readObject();
            if (nextMessage instanceof String) {
                if (!setNickname) {
                    for (ServerNetworkObserver o : observers)
                        executors.submit(() -> o.processNickname((String) nextMessage));
                } else if (!setGameMode) {
                    for (ServerNetworkObserver o : observers)
                        executors.submit(() -> o.processGameMode((String) nextMessage));
                }
            } else if (nextMessage instanceof NetworkMessagesToServer) {
                executors.submit(this::notifyObservers);
            } else if (nextMessage instanceof PingMessage)
                pingExecutor.schedule(() -> out.replyPing(), 5, TimeUnit.SECONDS);
        }
    }

    public void nicknameSet(boolean value) {
        setNickname = value;
    }

    public void setGameMode(boolean value) {
        setGameMode = value;
    }

    public void setClosed() {
        executors.shutdown();
        pingExecutor.shutdown();
        setClosed = true;
    }

    public void setUploader(ClientHandler h) {
        out = h;
    }


}
