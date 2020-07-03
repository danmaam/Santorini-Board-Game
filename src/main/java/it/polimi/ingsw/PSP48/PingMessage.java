package it.polimi.ingsw.PSP48;

import java.io.Serializable;

/**
 * The ping messages exchanged between client and server
 */
public class PingMessage implements Serializable {
    private final String message = "I'm alive";
}
