package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.client.CLI.CLI;
import it.polimi.ingsw.PSP48.client.CLI.ColoursForPrinting;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.server.Server;
import javafx.application.Application;
import javafx.stage.Stage;

public class SantoriniGame {
    public static void main(String[] args) {
        if (args.length == 0) {
            Application.launch(GUI.class, args);
        } else if (args[0].equals("--cli")) {
            CLI cli = new CLI(ColoursForPrinting.white);
            cli.run();
        } else if (args[0].equals("--startserver")) Server.run();
        else System.out.println("Invalid parameter. Aborting.");
    }
}
