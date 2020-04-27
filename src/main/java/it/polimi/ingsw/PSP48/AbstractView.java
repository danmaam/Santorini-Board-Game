package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.server.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.observers.ModelObserver;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public abstract class AbstractView implements ViewInterface, ModelObserver {
    private ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();

    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    public void notifyObserver(BiConsumer<ViewObserver, Object> lambda, Object o) {
        for (ViewObserver obv : observers) lambda.accept(obv, o);
    }
}
