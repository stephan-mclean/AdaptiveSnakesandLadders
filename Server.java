/*
	Class: Server.

	ROLES:
		1. Wait for incoming connections from players.
			The server will constantly loop waiting from an incoming
			connection. Once a connection is made, the server will retrieve
			the players name and place them in a game.
		2. Place players into a game.
			Once a player has connected to the server and given their name
			the server will look for an open game to place the player in.
			If no open game is found the server will create a new game
			and make the player the host of that game.

	Author: Stephan McLean, Kevin Sweeney, Ryan Moody.
*/
import java.net.*;
import java.io.*;
import java.util.*;
class Server extends Thread {
	private ServerSocket server;
	private ArrayList<Game> games;

	public Server() {
		/* 
			Pre-conditions: None
			
			Post-conditions: 
				server is set up on a port. 
				games has been instantiated.
							
		*/
		try {
			server = new ServerSocket(9999);
		}
		catch(IOException e) {}
		games = new ArrayList<Game>();
	}

	public void run() {
		/* 
			Pre-conditions:
				server is set up on a port.
				games has been instantiated
		
			Post-conditions:
				server will be closed and all clients disconnected.
		
			Semantics:
				This method will constantly loop waiting for players to conenct.
				Once a connection is made and the players name is retrieved
				they will be placed into a game.
		*/
		try {
			while(true) {
	
				Socket s = server.accept();
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				String name = in.readLine();

				// Create player & add to game
				// Game will set host & colour later.
				Player p = new Player(s, in, out, false, name, null);

				// synchronized??
				if(games.size() > 0) {
					// Find an open game.
					boolean found = false;
					for(Game g : games) {
						if(g.isOpen()) {
							found = true;
							g.addPlayer(p);
							break;
						}
					}


					if(!found) {
						createNewGame(p);
					}
				}
				else {
					createNewGame(p);
				}
			}
		}
		catch(Exception e) {
			exit();
		}
		exit();
	}

	void exit() {
		try {
			server.close();
		}
		catch(Exception e) {}
	}

	void createNewGame(Player p) {
		// Add a new game lobby & add p to the lobby
		Game g = new Game();
		g.addPlayer(p);
		games.add(g);
	}

	public static void main(String [] args) {
		new Server().start();
	}

}