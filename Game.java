/*
	Skeleton class: Game

	ROLES:
		1. Allow players to be added until there enough to
			play a game or until the host decides to start
			the game.
		2. Implement the logic of the game: This class will
			decide the order in which players will move. Once
			a player makes a move this class will validate the move.
		3. Coordinate players: Once a player move has been validated
			this class will inform all other players of the move. If
			a player move results in a player winning the game this class
			will inform all other players that the game is over.
	
	Objects of this class are created when no open game is found for a player.
	Rather than deleting the object when the game is over we will reuse them for
	players trying to join a new game.

*/
import java.util.*;
class Game {
	private Board board;
	private ArrayList<Player> players;

	public Game() {
		/*
			Pre-conditions: None

			Post-conditions:
				board and players will be instantiated. 

		*/
	}

	boolean isOpen() {
		/*
			Pre-conditions:
				board and players have been instantiated.

			Post-conditions:
				Will return true or false depending on the state of the game.

			Semantics:
				Will return true if the maximum number of players hasn't been
				reached and the game hasn't started, otherwise false.

		*/
	}

	void addPlayer(Player p) {
		/*
			Pre-conditions:
				isOpen() returns true and p is not null.

			Post-conditions: 
				p will be added to the players list.

		*/
	}

	void removePlayer(Player p) {
		/*
			Pre-conditions: 
				p is not null.

			Post-conditions:
				p will be removed from the players list.

			Semantics:
				p will be removed from the players list and all connections
				to the client will be closed. The game will then
				send a message to all players informing them of the player leaving.

		*/
	}

	void tellAll(String message) {
		/*
			Pre-conditions:
				message is not null.

			Post-conditions:
				message will have been sent to all players.
			
			Semantics:
				We will send the message to every player in players.
				The message may be a message from the server or a 
				player move.

		*/
	}

	void start() {
		/*
			Pre-conditions:
				players has a length >= 2

			Post-conditions:
				The game board will be set-up and the order
				of player moves will have been decided. The
				game will be ready to play.

			Semantics:
				This method will set up the game board.
				Snakes & ladders will be randomly placed on the board.
				The players pieces will be placed on the first square on the board
				and the order in which players play will be chosen at random.

		*/
	}
}