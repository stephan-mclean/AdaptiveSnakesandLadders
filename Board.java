/*
	Skeleton class: Board

	ROLES: 
		1. Keep track of all the pieces in the game. (Snakes, Ladders and player pieces.)

	Objects of this class are created when a new game is started and are
	deleted once the game is over.
	
*/

class Board {
	private Square [] [] squares;
	private int rows, cols;

	public Board() {
		/*
			Pre-conditions: None

			Post-conditions:
				rows and cols will be set to a default value of 10
				and squares will be instantiated.

		*/
	}

	public Board(int r, int c) {
		/*
			Pre-conditions: None

			Post-conditions:
				rows and cols will be set to the number specified and
				squares will be instantiated.

		*/
	}

	boolean hasPiece(int x, int y) {
		/*
			Pre-conditions: None

			Post-conditions: 
				Will return true if a playing piece is at squares[x][y]

		*/
	}

	void setPiece(int x, int y, Piece p) {
		/*
			Pre-conditions: None

			Post-conditions: 
				p will be at position [x][y] on the board.

		*/
	}

	void movePiece(int x, int y, Piece p) {
		/*
			Pre-conditions: None

			Post-conditions: 
				p will be at position [x][y] on the board having been
				moved from whatever square the board found it on.

			Semantics: The board will first search for piece p. 
						When p is found it will be removed from its
						current square and placed at [x][y]

		*/
	}
}