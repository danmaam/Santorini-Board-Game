package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.networkMessagesToServer.*;
import it.polimi.ingsw.PSP48.networkMessagesToServer.NetworkMessagesToServer;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientNetworkOutcoming implements Runnable, ViewObserver {
    private Socket server;
    private ObjectOutputStream outputStm;
    private ObjectInputStream inputStm;
    private ArrayList<ClientNetworkObserver> observers = new ArrayList<>();

    public void addObserver(ClientNetworkObserver n) {
        observers.add(n);
    }

    public void removeObserver(ClientNetworkObserver n) {
        observers.remove(n);
    }

    public NetworkMessagesToServer o;

    public ClientNetworkOutcoming(Socket s) {
        this.server = s;
    }

    @Override
    public synchronized void move(MoveCoordinates p) {
        nextAction = action.send_gameAction;
        o = new MoveMessage(p);
        notifyAll();
    }

    @Override
    public void build(MoveCoordinates p) {
        nextAction = action.send_gameAction;
        o = new BuildMessage(p);
        notifyAll();
    }

    @Override
    public void dome(MoveCoordinates p) {
        nextAction = action.send_gameAction;
        o = new DomeMessage(p);
        notifyAll();
    }

    @Override
    public void putWorkerOnTable(Position p) {
        nextAction = action.send_gameAction;
        o = new WorkerPositionMessage(p);
        notifyAll();
    }

    @Override
    public void registerPlayerDivinity(String divinity) {
        nextAction = action.send_gameAction;
        o = new PlayerDivinityMessage(divinity);
        notifyAll();
    }

    @Override
    public void addPlayer(String playerDetails, Calendar birthday) {

    }

    @Override
    public void selectAvailableDivinities(ArrayList<String> divinities) {
        nextAction = action.send_gameAction;
        o = new ChallengerDivinitiesMessage(divinities);
        notifyAll();
    }

    @Override
    public void selectFirstPlayer(String playerName) {
        nextAction = action.send_gameAction;
        o = new FirstPlayerSelectionMessage(playerName);
        notifyAll();
    }

    private enum action {
        send_nickname, send_gamemode, send_gameAction
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
                case send_gameAction:
                    sendGameAction();
                    break;
            }
        }
    }

    private synchronized void sendPlayerNickname() throws IOException, ClassNotFoundException {
        outputStm.writeObject(nextMessage);
        String newStr = (String) inputStm.readObject();

        List<ClientNetworkObserver> observersCpy;

        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ClientNetworkObserver observer : observersCpy) {
            observer.nicknameResult(newStr);
        }
    }

    private synchronized void sendGameMode() throws IOException, ClassNotFoundException {
        outputStm.writeObject(nextMessage);
        String newStr = (String) inputStm.readObject();

        List<ClientNetworkObserver> observersCpy;

        synchronized (observers) {
            observersCpy = new ArrayList<>(observers);
        }

        /* notify the observers that we got the string */
        for (ClientNetworkObserver observer : observersCpy) {
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

    public synchronized void sendGameAction() throws IOException, ClassNotFoundException {
        outputStm.writeObject(o);
    }


}
