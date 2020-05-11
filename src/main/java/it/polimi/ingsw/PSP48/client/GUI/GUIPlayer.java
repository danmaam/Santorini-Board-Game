package it.polimi.ingsw.PSP48.client.GUI;

/**
 * class representing all the data about a player in the gui
 */
public class GUIPlayer
{
    String nickname;
    String divinityName;
    GUIColours assignedColour;

    /**
     * class constructor used at the beginning of the game when we only know the nicknames of the players
     * @param playerName is the nickname we assign to a player
     */
    GUIPlayer(String playerName)
    {
        this.nickname=playerName;
    }

    /**
     * class constructor initialising all the fields of the player, used when we know everything that has been assigned to him
     * @param playerName is the nickname of the player
     * @param divinity is the divinity he has chosen
     * @param colour is the colour he has chosen
     */
    GUIPlayer(String playerName, String divinity, GUIColours colour)
    {
        this.nickname=playerName;
        this.divinityName=divinity;
        this.assignedColour=colour;
    }

    public String getNickname()
    {
        return nickname;
    }

    public String getDivinityName()
    {
        return divinityName;
    }

    public GUIColours getAssignedColour()
    {
        return assignedColour;
    }

    public void setAssignedColour(GUIColours newColour)
    {
        this.assignedColour = newColour;
    }

    public void setDivinityName(String name)
    {
        this.divinityName = name;
    }

    public void setNickname(String player)
    {
        this.nickname = player;
    }
}
