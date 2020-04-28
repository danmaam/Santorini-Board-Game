package it.polimi.ingsw.PSP48.client.view;

public class Player {
    private String name;
    private String divinity;
    private final String playerSymbol= "\u263a";
    private ColoursForPrinting playerColour;

    public Player(String name, ColoursForPrinting playerColour, String divinity){
        this.name=name;
        this.divinity=divinity;
        this.playerColour=playerColour;
    }
    public Player (String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDivinity() {
        return divinity;
    }

    public String getPlayerSymbol() {
        return playerSymbol;
    }

    public ColoursForPrinting getPlayerColour() {
        return playerColour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayerColour(ColoursForPrinting playerColour) {
        this.playerColour = playerColour;
    }

    public void setDivinity(String divinity) {
        this.divinity = divinity;
    }

    public String toString(){
        return this.playerColour+this.playerSymbol+ColoursForPrinting.reset;
    }
}

