Prova Finale Ingegneria Del Software AA 2019-2020
==========================

## Abstract
Java implementation of the board game "Santorini", by Cranio Creations (C), playable in network, using the MVC pattern.

## Group composition
- Daniele Mammone
- Rebecca Marelli
- Annalaura Massa

## Game-Specific requirements
The game implements the rule set "**Full rules**" All the base divinities have been implemented, expect Hermes, and it's possible to complete games with two, or three players.
Players can choose to play with, or without divinities (following the rules for the selected mode).

## Advanced features
The game implements the following advanced features:
- **Multiple Games**: the *server* the server is able to handle more than one game at once. At game start, players can choose if they wants to play a two or a three player game. The server adds them in the first available game (or create a game if there aren't available games). When a game number player is reached, the server starts the game.
- **Advanced Divinities**: the advanced divinities implemented are:
	+ "Circe", n. 17, available only in a two players game, according to the rules;
	+ "Zeus", n. 30;
	+ "Chronus", n. 16, available only in a two players game, according to the rules;
	+ "Eros", n. 19;
	+ "Hestia", n. 21;

## Game modes
The game is playable with both command line interface and graphical interface, the last developed using JavaFX.

## How to start the application
The game is deployed in a JAR containing both client and server
### Prerequisites
Java SE 14 must be installed on your system, and Java binary path must be configured.
On Linux distributions, it's recommended to install Java 14 via OpenJDK 14, installable with the following terminal command:

```
 sudo apt install openjdk-14-jdk-headless
 ```
If apt doen't find the package, you must add at the file /etc/apt/sources.list the followings lines
```
deb http://it.archive.ubuntu.com/ubuntu/ focal main universe restricted multiverse
#deb-src http://it.archive.ubuntu.com/ubuntu/ focal main universe restricted multiverse

deb http://security.ubuntu.com/ubuntu focal-security main universe restricted multiverse
#deb-src http://security.ubuntu.com/ubuntu focal-security main universe restricted multiverse

deb http://it.archive.ubuntu.com/ubuntu/ focal-updates main universe restricted multiverse
#deb-src http://it.archive.ubuntu.com/ubuntu/ focal-updates main universe restricted multiverse
```
and then execute the commands
```
sudo apt update
```
```
sudo apt upgrade
```
For Windows and Mac systems, downloads and installs Oracle's JDK from the following link
https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html

### Starting Server
Open the terminal, go with cd command in the folder where there is the JAR file, and execute the following command
```
java -jar SantoriniGame.jar --startserver
```
The server puts itself in listening mode, on the port *7777*

### Starting GUI
The JAR file contains JavaFX dependendencies of all the OS, to ensure that the game is crossplatform. To launch the GUI, just double click on the JAR itself or, if this doesn't work for some reason, open the terminal, go in the folder where there is the JAR, and execute the command:
```
java -jar SantoriniGame.jar
```
> :warning: **Trying to open a second instance of the gui via double click may fail, depending on your OS. If you can't open a second instance via double-click, open the GUI using the terminal**



### Starting CLI

Open the terminal, go in the directory where there is the JAR file and execute the command
```
java -jar SantoriniGame.jar --cli
```

Unfortunally, the CLI is compatible only with UNIX systems, due to ANSI characters printed on screen. To use it on Windows systems, it's recommended to install the Windows Subsystem for Linux following the guide at https://www.windowscentral.com/install-windows-subsystem-linux-windows-10, and launch CLI from WSL. Using CLI on Windows' native terminal, may occur in printing of apparently random characters. Tip: to reach your disk root from WSL, go in /mnt folder, where there are the roots of the disks connected at your system.



