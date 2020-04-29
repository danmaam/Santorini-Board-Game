package it.polimi.ingsw.PSP48.client.view;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientNetworkAdapter implements Runnable {
    private Socket server;
    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;
    private ArrayList<NetworkObserver> observers = new ArrayList<>();

    public void addObserver(NetworkObserver n) {
        observers.add(n);
    }

    public void removeObserver(NetworkObserver n) {
        observers.remove(n);
    }

    public Object o;

    public ClientNetworkAdapter(Socket s) {
        this.server = s;
    }

    private enum action {
        send_nickname, send_gamemode
    }


    private action nextAction;
    private String nextMessage;

    @Override
    public void run() {
        try {
            outputStm = new ObjectOutputStream(server.getOutputStream());
            inputStm = new ObjectInputStream(server.getInputStream());
            handleServerConnection();
        } catch (IOException e) {
            System.out.println("server has died");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("protocol violation");
        }
    }

    public synchronized void handleServerConnection() throws IOException, ClassNotFoundException {
        while (true) {
            nextAction = null;
            try {
                wait();
            } catch (InterruptedException e) {
            }

            if (nextAction == null)
                continue;

            switch (nextAction) {
                case send_nickname:
                    sendPlayerNickname();
                    break;
                case send_gamemode:
                    sendGameMode();
                    break;
            }
        }
    }

    public synchronized void sendPlayerNickname() throws IOException, ClassNotFoundException {
        outputStm.writeObject(nextMessage);
        String newStr = (String) inputStm.readObject();

        List<NetworkObserver> observersCpy;

        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (NetworkObserver observer : observersCpy) {
            observer.nicknameResult(newStr);
        }
    }

    public synchronized void sendGameMode() throws IOException, ClassNotFoundException {
        outputStm.writeObject(nextMessage);
        String newStr = (String) inputStm.readObject();

        List<NetworkObserver> observersCpy;

        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (NetworkObserver observer : observersCpy) {
            observer.gameModeResult(newStr);
        }
    }

    public synchronized void requestNicknameSend(String nickname) {
        nextAction = action.send_nickname;
        this.nextMessage = nickname;
        notifyAll();
    }

    public synchronized void setGameMode(String n) {
        nextAction = action.send_gamemode;
        nextMessage = n;
        notifyAll();
    }


}
