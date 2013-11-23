/*
	Skeleton class: Piece

	ROLES:
		1. Represent a players piece on the board or a snake or ladder
			on the board.

	Objects of this class will be created when a player is assigned a piece and
	also when a snake or ladder is placed on the board. They will be deleted
	when the board is deleted.

	NOTE: id is being used to match snakes & ladders with their counterpart.
			For player pieces this field will be -1

*/

class Piece {
	private String colour;
	private int id;

	public Piece(String c) {
		/*
			Pre-conditions: None

			Post-conditions: colour will be set to c and id will be set to -1

		*/
	}

	public Piece(int i) {
		/*
			Pre-conditions: None

			Post-conditions: id will be set to i

		*/
	}

	String getColour() {
		/*
			Pre-conditions: None

			Post-conditions: colour will be returned.
		*/
	}

	int getId() {
		/*
			Pre-conditions: None

			Post-conditions: id will be returned.
		*/
	}
}