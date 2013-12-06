/*
	Class: Board

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
		rows = 10; cols = 10;
		squares = new Square[rows][cols];
		setupSquares();
	}

	public Board(int r, int c) {
		/*
			Pre-conditions: None

			Post-conditions:
				rows and cols will be set to the number specified and
				squares will be instantiated.

		*/
		rows = r; cols = c;
		squares = new Square[rows][cols];
		setupSquares();
	}

	void setupSquares() {
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				squares[i][j] = new Square(i, j);
			}
		}
	}

	boolean hasPiece(int x, int y) {
		/*
			Pre-conditions: None

			Post-conditions: 
				Will return true if a playing piece is at squares[x][y]

		*/
		return squares[x][y].hasPiece();
	}

	boolean hasPiece(int x, int y, Piece p) {
		return squares[x][y].hasPiece(p);
	}

	void removePiece(int x, int y, Piece p) {
		squares[x][y].removePiece(p);
	}

	void setPiece(int x, int y, Piece p) {
		/*
			Pre-conditions: None

			Post-conditions: 
				p will be at position [x][y] on the board.

		*/
		squares[x][y].setPiece(p);
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
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				if(squares[i][j].hasPiece(p)) {
					squares[i][j].removePiece(p);
				}
			}
		}

		squares[x][y].setPiece(p);
	}

	Square square(int row, int col) {
		return squares[row][col];
	}

	int rows() {
		return rows;
	}

	int cols() {
		return cols;
	}
}