package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.networkMessagesToServer.*;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class ClientNetworkOutcoming implements Runnable, ViewObserver {
    private final Socket server;
    private ObjectOutputStream outputStm;


    private NetworkMessagesToServer o;

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
        send_nickname, send_gamemode, send_gameAction, close_inputstream, replyPing
    }


    private action nextAction;
    private String nextMessage;

    @Override
    public void run() {
        try {
            outputStm = new ObjectOutputStream(server.getOutputStream());
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
                e.printStackTrace();
            }

            if (nextAction == null)
                continue;
            else if (nextAction == action.close_inputstream) {
                server.close();
                return;
            }

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
                case replyPing:
                    sendPingMessage();
                    break;

            }
        }
    }

    private synchronized void sendPlayerNickname() throws IOException {
        outputStm.writeObject(nextMessage);
    }

    private synchronized void sendGameMode() throws IOException {
        outputStm.writeObject(nextMessage);
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

    public synchronized void sendGameAction() throws IOException {
        outputStm.writeObject(o);
    }

    public synchronized void shutDown() {
        nextAction = action.close_inputstream;
        notifyAll();
    }

    public synchronized void replyPing() {
        nextAction = action.replyPing;
        notifyAll();
    }

    public synchronized void sendPingMessage() throws IOException {
        outputStm.writeObject(new PingMessage());
    }


}
