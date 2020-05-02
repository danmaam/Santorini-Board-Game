package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.networkMessagesToServer.NetworkMessagesToServer;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandlerListener implements Runnable {

    private NetworkMessagesToServer nextMessage;

    private boolean setNickname = false;
    private boolean setGameMode = false;
    private String setUp;

    private Socket clientSocket;

    private ArrayList<ServerNetworkObserver> observers = new ArrayList<>();

    public void registerObserver(ServerNetworkObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ServerNetworkObserver obv) {
        observers.remove(obv);
    }

    public void notifyObservers() {
        System.out.println("received message " + nextMessage.toString());
        for (ServerNetworkObserver nO : observers) {
            nextMessage.doThings(nO);
        }
    }

    public ClientHandlerListener(Socket client) {
        this.clientSocket = client;
    }

    @Override
    public void run() {
        try {
            waitForMessages();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public synchronized void waitForMessages() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        while (true) {
            if (!setNickname) {
                setUp = (String) input.readObject();
                for (ServerNetworkObserver o : observers) o.processNickname(setUp);
            } else if (!setGameMode) {
                setUp = (String) input.readObject();
                for (ServerNetworkObserver o : observers) o.processGameMode(setUp);
            } else {
                System.out.println("waiting for messages");
                nextMessage = (NetworkMessagesToServer) input.readObject();
                System.out.println("received message");
                notifyObservers();
            }
        }
    }

    public void nicknameSet(boolean value) {
        setNickname = value;
    }

    public void setGameMode(boolean value) {
        setGameMode = value;
    }
}
