/*
	Skeleton class: Player

	ROLES:
		1. Store information about a particular client.

*/
import java.net.*;
import java.io.*;
import java.util.*;

class Player {
	private Socket clientSocket;
	private BufferedReader in;
	private PrintWriter out;
	private boolean host;
	private String name, pieceColour;
	private Piece p;

	public Player(Socket s, BufferedReader i, PrintWriter o, boolean h, String n, String p) {
		/*
			Pre-conditions: None

			Post-conditions:
				All instance variables will have been set up.

		*/
		clientSocket = s;
		in = i; 
		out = o;
		host = h;
		name = n;
		pieceColour = p;
	}

	String getName() {
		/*
			Pre-conditions: None

			Post-conditions:
				Player name will be returned.

		*/
		return name;
	}

	String getColour() {
		/*
			Pre-conditions: None

			Post-conditions:
				pieceColour will be returned.

		*/
		return pieceColour;
	}

	BufferedReader getInput() {
		/*
			Pre-conditions: None

			Post-conditions:
				in will be returned.
		*/
		return in;
	}

	PrintWriter getOutput() {
		/*
			Pre-conditions: None

			Post-conditions:
				out will be returned.

		*/
		return out;
	}

	boolean isHost() {
		/*
			Pre-conditions: None

			Post-conditions:
				isHost will be returned.

			Semantics: 
				This method allows the game to determine if a given
				player is the host. The game needs to know this because
				the host player has the option to start the game.

		*/
		return host;
	}

	void disconnect() {
		try {
			clientSocket.close();
			in.close();
			out.close();
		}
		catch(Exception e) {} 
	}

	void setHost(boolean h) {
		host = h;
	}

	void setColour(String c) {
		pieceColour = c;
	}

	void setPiece(Piece piece) {
		p = piece;
	}

	Piece getPiece() {
		return p;
	}
}