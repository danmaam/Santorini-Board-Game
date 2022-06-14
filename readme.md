Software Engineering final test AA 2019-2020
==========================

## Abstract
Java implementation of the board game "Santorini", by Cranio Creations (C).

## Group composition
The group is the number 48 of Prof Pierluigi San Pietro's class, and is composed by:
- Daniele Mammone
- Rebecca Marelli
- Annalaura Massa

## Game-Specific requirements
The game implements the rule set "**Full rules**" All the base divinities have been implemented, expect Hermes, and it's possible to complete games with two, or three players.
Players can choose to play with, or without divinities (following the rules for the selected mode).

## Game-agnostic requirements
The game was developed using the MVC controller, and it's playable online via TCP socket. 
As described in the following paragraph, the server is able to handle more than one game at once.
Also, the client implements both Command Line and Graphical User interfaces.


## Advanced features
The game implements the following advanced features:
- **Multiple Games**: the *server* is able to handle more than one game at once. At game start, players can choose if they wants to play a two or a three player game. The server adds them in the first available game (or create a game if there aren't available games). When a game number player is reached, the server starts the game.
- **Advanced Divinities**: the advanced divinities implemented are:
	+ "Circe", n. 17, available only in a two players game, according to the rules;
	+ "Zeus", n. 30;
	+ "Chronus", n. 16, available only in a two players game, according to the rules;
	+ "Eros", n. 19;
	+ "Hestia", n. 21;


## Test coverage
The model have been successfully tested with the 92% of line coverage, using Junit 4.12 framework.
Also controller have been tested, but with a minor coverage, just to test the most critical situations (eg. when a player wins or loses).


## How to start the application
The game is deployed in a JAR containing both client and server
### Prerequisites
JDK 14 must be installed on your system to build the JAR and play the game, and Java binary path must be configured.
On Linux distributions, it's recommended to install Java 14 via OpenJDK 14, installable with the following terminal command:

```
 sudo apt install openjdk-14-jdk-headless
 ```
If apt shouldn't find the package, follows the instructions at https://openjdk.java.net/install/ and add OpenJDK/bin path in your system.
For Windows and Mac systems, it should be sufficient to download and install Oracle's JDK from the following link
https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html

### How to build the JAR
If you don't want to use the provided JAR file, you can build on your own the JAR file using Maven. In particular, the provided JAR have been built using the Maven Shade plugin, and it includes all the dependencies required to make cross-platform the GUI (e.g. the dependencies of JavaFX).
To build the JAR, install Maven on your system following the guide at https://maven.apache.org/install.html, clone this repo and, from the terminal, in the folder where you cloned the repo, run the command
```
mvn clean test package shade:shade
```
You can found the generated JAR file in the $PATH_TO_REPO/target folder

### Starting Server
Open the terminal, go folder where there is the JAR file, and execute the following command
```
java -jar SantoriniGame.jar --startserver
```
This will launch the server on the port *7777*

### Starting GUI
 To launch the GUI, just double click on the JAR itself or, if this doesn't work for some reason, open the terminal, go in the folder where there is the JAR, and execute the command:
```
java -jar SantoriniGame.jar
```
> :warning: **Trying to open a second instance of the gui via double-click may fail, depending on your OS. If you can't open a second instance via double-click, open the GUI using the terminal**


### Starting CLI

Open the terminal, go in the directory where there is the JAR file and execute the command
```
java -jar SantoriniGame.jar --cli
```

Unfortunately, the CLI is compatible only with UNIX systems, due to ANSI characters printed on screen. To use it on Windows systems, it's recommended to install the Windows Subsystem for Linux following the guide at https://www.windowscentral.com/install-windows-subsystem-linux-windows-10, and launch CLI from WSL, following the Linux instructions to setup your Linux installation. Using CLI on Windows' native terminal, may occur in printing of apparently random characters. Tip: to reach your disk root from WSL, go in /mnt folder, where there are the roots of the disks connected at your system.



