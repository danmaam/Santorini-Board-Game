package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.networkMessagesToServer.NetworkMessagesToServer;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandlerListener<NetworkMessageToServer> implements Runnable {

    private final Socket clientListener;
    private NetworkMessagesToServer nextMessage;

    private ArrayList<ServerNetworkObserver> observers = new ArrayList<>();

    public void registerObserver(ServerNetworkObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ServerNetworkObserver obv) {
        observers.remove(obv);
    }

    public void notifyObservers() {
        for (ServerNetworkObserver nO : observers) {
            nextMessage.doThings(nO);
        }
    }

    public ClientHandlerListener(Socket client) {
        clientListener = client;
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
        ObjectInputStream in = new ObjectInputStream(clientListener.getInputStream());
        while (true) {
            nextMessage = (NetworkMessagesToServer) in.readObject();
            notifyObservers();
        }
    }
}
