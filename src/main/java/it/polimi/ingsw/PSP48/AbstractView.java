package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.client.view.NetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.observers.ModelObserver;
import it.polimi.ingsw.PSP48.observers.networkHandlerObserver;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class AbstractView implements ViewInterface, ModelObserver, networkHandlerObserver {
    private ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();

    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    public void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver obv : observers) lambda.accept(obv);
    }
}
