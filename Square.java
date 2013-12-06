/*
	Class: Square

	ROLES:
		1. Hold one or more player pieces or hold a snake or ladder piece.

	Objects of this class will be created when a new board is created and
	deleted when the board is deleted.


*/
import java.util.*;
class Square {
	private ArrayList<Piece> pieces;
	private int row, col;

	public Square() {
		/*
			Pre-conditions: None

			Post-conditions: pieces will be instantiated.
		*/
		
	}

	public Square(int r, int c) {
		pieces = new ArrayList<Piece>();
		row = r; col = c;
	}

	int getRow() {
		return row;
	}

	int getCol() {
		return col;
	}

	boolean hasPiece() {
		/*
			Pre-conditions: None

			Post-conditions:
				Will return true if there are one or more player pieces 
				on the square, false otherwise.

		*/
		return pieces.size() > 0;
	}

	boolean hasPiece(Piece p) {
		/*
			Pre-conditions: None

			Post-conditions:
				Will return true if the given piece p is on the square,
				false otherwise.
		*/
		return pieces.contains(p);
	}

	boolean hasSnake() {
		boolean result = false;
		for(Piece p : pieces) {
			if(p.getId() >= 0 && p.getId() < 3) {
				result = true;
				break;
			}
		}
		return result;
	}

	boolean hasLadder() {
		boolean result = false;
		for(Piece p : pieces) {
			if(p.getId() >= 3 && p.getId() < 6) {
				result = true;
				break;
			}
		}
		return result;
	}

	Piece getSnake() {
		Piece result = null;
		for(Piece p : pieces) {
			if(p.getId() >= 0 && p.getId() < 3) {
				result = p;
			}
		}
		return result;
	}

	Piece getLadder() {
		Piece result = null;
		for(Piece p : pieces) {
			if(p.getId() >= 3 && p.getId() < 6) {
				result = p;
			}
		}
		return result;
	}

	void setPiece(Piece p) {
		pieces.add(p);
	}

	void removePiece(Piece p) {
		// What if p not present?
		pieces.remove(p);
	} 
}