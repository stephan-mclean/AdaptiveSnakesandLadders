/*
	Skeleton class: Client

	ROLES:
		1. This class is responsible for getting the players name.
		2. It is responsible for making contact with the server.
		3. This class will also handle setting up the interface
			and modifying it when moves come in from the server.


*/
import java.net.*;
import javax.swing.*;
class Client extends JFrame {
	private String name;
	private Socket server;

	public Client() {
		/*
			Pre-conditions: None

			Post-conditions:
				name will be instantiated. 
				server will have been set up and a connection to the server
				established.
				The GUI will have been set up.

		*/
	}

	void showMessage(String message) {
		/*
			Pre-conditions: None

			Post-conditions: message will be shown on the interface.

		*/
	}

	void movePiece(int x, int y, int fromX, int fromY) {
		/*
			Pre-conditions: None

			Post-conditions: The piece at position [fromX][fromY] will be removed
								and set at position [x][y] on the board.

		*/
	}

	void setPiece(int x, int y, String colour) {
		/*
			Pre-conditions: None

			Post-conditions: A player piece with the specified colour
							 will be set at [x][y]
		*/
	}

	void setPiece(int x, int y, int id) {
		/*
			Pre-conditions: None

			Post-conditions: A snake or ladder with the specified id
							 will be set at [x][y]

		*/
	}

	void gameOver(String winner) {
		/*
			Pre-conditions: None

			Post-conditions: The player will be informed that someone
							 has won the game and the game will stop playing.
		*/
	}

	void tellServer(String message) {
		/*
			Pre-conditions: None

			Post-conditions: message will be sent to the server.

		*/
	}
}