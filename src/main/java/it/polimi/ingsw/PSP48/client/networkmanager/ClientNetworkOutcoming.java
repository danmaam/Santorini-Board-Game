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
    public synchronized void build(MoveCoordinates p) {
        nextAction = action.send_gameAction;
        o = new BuildMessage(p);
        notifyAll();
    }

    @Override
    public synchronized void dome(MoveCoordinates p) {
        nextAction = action.send_gameAction;
        o = new DomeMessage(p);
        notifyAll();
    }

    @Override
    public synchronized void putWorkerOnTable(Position p) {
        nextAction = action.send_gameAction;
        o = new WorkerPositionMessage(p);
        notifyAll();
    }

    @Override
    public synchronized void registerPlayerDivinity(String divinity) {
        nextAction = action.send_gameAction;
        o = new PlayerDivinityMessage(divinity);
        notifyAll();
    }

    @Override
    public synchronized void addPlayer(String playerDetails, Calendar birthday) {

    }

    @Override
    public synchronized void selectAvailableDivinities(ArrayList<String> divinities) {
        nextAction = action.send_gameAction;
        o = new ChallengerDivinitiesMessage(divinities);
        notifyAll();
    }

    @Override
    public synchronized void selectFirstPlayer(String playerName) {
        nextAction = action.send_gameAction;
        o = new FirstPlayerSelectionMessage(playerName);
        notifyAll();
    }

    private enum action {
        send_nickname, send_gamemode, send_gameAction, close_inputstream
    }


    private action nextAction;
    private String nextMessage;

    @Override
    public void run() {
        try {
            outputStm = new ObjectOutputStream(server.getOutputStream());
            System.out.println("starting message handler");
            handleServerConnection();
        } catch (IOException e) {
            System.out.println("server has died");
        } catch (ClassCastException | ClassNotFoundException e) {
            System.out.println("protocol violation");
        }
    }

    public synchronized void handleServerConnection() throws IOException, ClassNotFoundException {
        System.out.println("handling messages");
        while (true) {
            nextAction = null;
            try {
                wait();
            } catch (InterruptedException e) {
            }

            if (nextAction == null)
                continue;
            else if (nextAction == action.close_inputstream) return;

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
        System.out.println("sending nickname");
        outputStm.writeObject(nextMessage);
    }

    private synchronized void sendGameMode() throws IOException, ClassNotFoundException {
        System.out.println("sending game mode");
        outputStm.writeObject(nextMessage);
    }

    public synchronized void requestNicknameSend(String nickname) {
        System.out.println("sending nickname");
        nextAction = action.send_nickname;
        this.nextMessage = nickname;
        notifyAll();
    }

    public synchronized void setGameMode(String n) {
        System.out.println("sending game mode");
        nextAction = action.send_gamemode;
        nextMessage = n;
        notifyAll();
    }

    public synchronized void sendGameAction() throws IOException, ClassNotFoundException {
        System.out.println("sending game action");
        outputStm.writeObject(o);
    }

    public synchronized void shutDown() {
        nextAction = action.close_inputstream;
        notifyAll();
    }


}
