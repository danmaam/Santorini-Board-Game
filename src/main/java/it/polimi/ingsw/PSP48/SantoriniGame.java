package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.client.CLI.CLI;
import it.polimi.ingsw.PSP48.client.CLI.ColoursForPrinting;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.server.Server;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main game class. This is the entry point for the JVM.
 * Arguments of launching:
 * --startserver: starts the application in server mode, to host a game server
 * --cli: starts the game client in cli mode
 * --gui: starts the game client in gui mode
 */
public class SantoriniGame {
    /**
     * Main method of the game
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            Application.launch(GUI.class, args);
        } else if (args[0].equals("--cli")) {
            CLI cli = new CLI(ColoursForPrinting.white);
            cli.run();
        } else if (args[0].equals("--startserver")) Server.run();
        else System.out.println("Invalid parameter.\nPlease run the application with one of the following parameter:\n--cli: starts the CLI\n--startserver: starts the server\nno parameter: starts the GUI");
    }
}
