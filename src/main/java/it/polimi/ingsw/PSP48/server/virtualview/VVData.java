package it.polimi.ingsw.PSP48.server.virtualview;

import it.polimi.ingsw.PSP48.server.Observer;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.virtualview.exceptions.PlayerNotFoundException;

import java.net.Socket;
import java.util.ArrayList;

public class VVData {
    private ArrayList<PlayerSocketAssociation> playersInGame;
    private ArrayList<Observer> clientHObservers = new ArrayList<>();
    private ArrayList<Observer> controllerObservers = new ArrayList<>();
    private Cell[][] gameCells = new Cell[5][5];

    public Socket getPlayerHandler(String playerName) throws PlayerNotFoundException {
        for (PlayerSocketAssociation elem : playersInGame) {
            if (playerName.equals(elem.getPlayerName())) return elem.getPlayerSocket();
        }
        throw new PlayerNotFoundException("Giocatore " + playerName + " inesistente");
    }

    public void addClientHandlerObserver(Observer obv) {
        clientHObservers.add(obv);
    }

    public void unregisterClientHandlerObserver(Observer obv) {
        clientHObservers.remove(obv);
    }

    public void notifyClientHandlerObservers(Object o, String arg) {
        for (Observer ob : clientHObservers) ob.update(o, arg);
    }

    public void addControllerObservers(Observer obv) {
        controllerObservers.add(obv);
    }

    public void unregisterControllerObservers(Observer obv) {
        controllerObservers.remove(obv);
    }

    public void notifyControllerObservers(Object o, String arg) {
        for (Observer ob : controllerObservers) ob.update(o, arg);
    }

    public void stateOperation() {

    }

    public ArrayList<String> getPlayers() {
        ArrayList<String> players = new ArrayList<>();
        for (PlayerSocketAssociation p : playersInGame) players.add(p.getPlayerName());
        return players;
    }

    public void addPlayer(String playerName, Socket s) {
        playersInGame.add(new PlayerSocketAssociation(playerName, s));
        notifyClientHandlerObservers(playerName, "add_player");
        notifyControllerObservers(playerName, "add_player");
    }

    public void removePlayer(String playerName) throws PlayerNotFoundException {
        PlayerSocketAssociation kek = null;
        for (PlayerSocketAssociation p : playersInGame) {
            if (p.getPlayerName().equals(playerName)) {
                kek = p;
                break;
            }
        }
        if (kek != null) {
            playersInGame.remove(kek);
            notifyClientHandlerObservers(playerName, "remove_player");
        } else throw new PlayerNotFoundException("Giocatore " + playerName + " non trovato");
    }

    public void updateCell(int row, int column, String playerName, int level, Boolean dome) {
        gameCells[row][column] = new Cell(row, column, level, playerName, dome);
        notifyClientHandlerObservers(null, null);
    }

}
