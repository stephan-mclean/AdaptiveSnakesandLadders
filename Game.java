/*
	Class: Game

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
	// Keep track of current player move.
	private int currentPlayer = 0; 
	private boolean started = false;
	private static final int MAX_PLAYERS = 2;
	private ArrayList<String> colours;

	public Game() {
		/*
			Pre-conditions: None

			Post-conditions:
				board and players will be instantiated. 

		*/
		board = new Board();
		players = new ArrayList<Player>();
		colours = new ArrayList<String>();
		colours.add("red"); colours.add("yellow");
		colours.add("green"); colours.add("cyan");
		colours.add("orange"); colours.add("blue");
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
		return (!started && (players.size() < MAX_PLAYERS));
	}

	void addPlayer(Player p) {
		/*
			Pre-conditions:
				isOpen() returns true and p is not null.

			Post-conditions: 
				p will be added to the players list.

		*/
		synchronized(players) {
			
			if(players.size() == 0) {
				p.setHost(true);
			}
			players.add(p);
			p.setColour(colours.get(0));
			p.setPiece(new Piece(colours.get(0)));
			colours.remove(0);
			tellAll(MessageFormatter.chatMessage("SERVER", 
						p.getName() + " has joined."));

			sendPresenceList();
			new PlayerListener(p, this).start();
		}
	}

	void sendPresence(Player p, String type) {
		/*
			Send presence message for p to all other players.
		*/
		tellAll(MessageFormatter.presenceMessage(p.getName(), p.getColour(),
												 "" + p.isHost(), type));
	}

	void sendPresenceList() {
		for(Player p : players) {
			sendPresence(p, "online");
		}
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
		synchronized(players) {
			players.remove(p);
			colours.add(p.getColour()); // Free up that colour.
			p.disconnect();
			tellAll(MessageFormatter.chatMessage("SERVER",
										p.getName() + " has left."));

			sendPresence(p, "offline");

			if(players.size() == 0) {
				started = false;
			}
			if(players.size() == 1) {
				players.get(0).setHost(true);
				tellAll(MessageFormatter.chatMessage("SERVER", 
						players.get(0).getName() + " is now host."));
				if(started) {
					started = false;
					tellAll(MessageFormatter.gameMessage("finish"));
				}
			}
		}
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
		for(Player p : players) {
			tell(p, message);
		}
	}

	void tell(Player p, String message) {
		p.getOutput().println(message);
	}

	void start(Player p) {
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

		if(p.isHost()) {
			if(players.size() >= 2) {
				if(!started) {
					started = true;
					setupBoard();
					tellAll(MessageFormatter.chatMessage("SERVER", "Game is starting."));
					tellAll(MessageFormatter.gameMessage("start"));
					// Choose player turn order.
					Collections.shuffle(players);
					playerTurn(players.get(currentPlayer));
				}
				else {
					tell(p, MessageFormatter.chatMessage("SERVER",
							"Game has already started."));
				}
			}
			else {
				tell(p, MessageFormatter.chatMessage("SERVER",
						"Not enough players."));
			}
		}
		else {
			tell(p, MessageFormatter.chatMessage("SERVER", 
										"Only the host can start the game"));
		}
		
	}

	boolean gameOver() {
		return board.hasPiece(0, 0);
	}

	void playerTurn(Player p) {
		// Inform p it is their turn.
		tellAll(MessageFormatter.chatMessage("SERVER", "It is " + p.getName() +
											 "'s turn"));
		tellAll(MessageFormatter.playerTurn(p.getName()));
	}

	void nextPlayerTurn() {
		currentPlayer = (currentPlayer + 1) % players.size();
		playerTurn(players.get(currentPlayer));
	}

	void setupBoard() {
		/*
			Set snakes, ladders & player pieces on the board.
		*/

		board = new Board();

		// Player pieces.
		for(Player p : players) {
			board.setPiece(9, 0, new Piece(p.getColour()));
			tellAll(MessageFormatter.setMessage("9", "0", p.getColour(), -1));
		}

		// Snakes & ladders.
		// Give snakes an id >= 0 & < 3
		// Give ladders an id >= 3 & < 6
		for(int i = 0; i < 6; i++) {
			placeRandom(i);
		}
	}

	void placeRandom(int i) {
		/*
			Place a piece with id i
			on a random square on the board.
		*/
		Piece p = new Piece(i);
		Square head = randomSquare();
		Square tail = randomSquare();

		head.setPiece(p);
		tail.setPiece(p);
		tellAll(MessageFormatter.setMessage("" + head.getRow(), 
				"" + head.getCol(), null, p.getId()));

		tellAll(MessageFormatter.setMessage("" + tail.getRow(), 
			"" + tail.getCol(), null, p.getId()));		
		
	}

	Square randomSquare() {
		/*
			Return any square on the board.
			Cannot return 0, 0 or 9, 0
			or a square that already has a snake or ladder.
		*/
		int row = rand(9, 0); // board.rows() - 1 instead of 9?
		int col = rand(9, 0);

		while((row == 0 && col == 0) || (row == 9 && col == 0) || 
				board.square(row, col).hasSnake() || 
				board.square(row, col).hasLadder()) {

			row = rand(9, 0);
			col = rand(9, 0);
		}

		return board.square(row, col);
	}

	private int rand(int max, int min) {
		return (int) (min + (Math.random() * max));
	}

	void normalMove(Player p) {
		/*
			Player p has sent a move request.
			Check if it is their turn & make the move.

		*/
		if(players.indexOf(p) == currentPlayer) {
			int roll = (int) (1 + (Math.random() * 6));
			tellAll(MessageFormatter.chatMessage("SERVER",
					p.getName() + " rolled " + roll));
			Piece playerPiece = p.getPiece();

			int x = 0;
			int y = 0;
			for(int i = 0; i < board.rows(); i++) {
				for(int j = 0; j < board.cols(); j++) {
					if(board.hasPiece(i, j, playerPiece)) {
						x = i; y = j;
						board.removePiece(i, j, playerPiece);
					}
				}
			}

			// Calculate new pos
			int newX = x;
			int newY = y;

			// Every even row we move left
			if(newX % 2 == 0)
				newY = newY - roll;
			else // Every odd row me move right
				newY = newY + roll;

			// Need to move to new row if column is greater than 9
			// or less than 0.
			if(newY > 9) {
				newX--;
				newY = 10 - (newY % 9);
			}
			else if(newY < 0) {
				newX--;
				if(newX < 0) {
					// Player has gone past square 100.
					// New column change is slightly different.
					newX++;
					newY = 9 - (9 - (newY * -1));
				}
				else
					newY = 9 - (10 - (newY * -1));
			}
			
			movePlayerPiece(x, y, newX, newY, playerPiece);
			if(!gameOver()) {
				nextPlayerTurn();
			}
			else {
				started = false;
				tellAll(MessageFormatter.gameMessage("finish"));
			}
		}
	}

	void movePlayerPiece(int fromX, int fromY, int x, int y, Piece p) {
			
			if(board.square(x, y).hasSnake()) {
				// If player has landed on head of snake move to tail
				Square tail = getPair(board.square(x, y).getSnake(), x, y, 1);
				if(tail != null) {
					x = tail.getRow();
					y = tail.getCol();
				}
			}
			else if(board.square(x, y).hasLadder()) {
				Square head = getPair(board.square(x, y).getLadder(), x, y, -1);
				if(head != null) {
					x = head.getRow();
					y = head.getCol();
				}
			}
			
			board.setPiece(x, y, p);
			tellAll(MessageFormatter.moveMessage("" + fromX, "" + fromY,
						"" + x, "" + y,
						p.getColour()));
		}

	Square getPair(Piece p, int x, int y, int direction) {
		/*
			Look for a pair for piece p moving through the rows of
			the board in direction.
			Will return null if not found.
		*/

		Square pair = null;
		for(int i = (x + direction); i >= 0 && i <= 9; i += direction) {
			for(int j = 0; j < board.cols(); j++) {
				if(board.hasPiece(i, j, p)) {
					pair = board.square(i, j);
					break;
				}
			}
		}
		return pair;
	}
}