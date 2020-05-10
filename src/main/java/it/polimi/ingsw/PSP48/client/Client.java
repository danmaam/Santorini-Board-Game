package it.polimi.ingsw.PSP48.client;

import it.polimi.ingsw.PSP48.client.CLI.CLI;
import it.polimi.ingsw.PSP48.client.CLI.ColoursForPrinting;

public class Client {
    public static void main(String[] args) {
        if (args.length == 0) {
            //here the gui starts
        } else if (args[0].equals("--cli")) {
            CLI cli = new CLI(ColoursForPrinting.white);
            cli.run();
        } else System.out.println("Invalid parameter. Aborting.");
    }
}
