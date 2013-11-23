/*
	Skeleton Class: PlayerListener

	ROLES: 
		1. Listen for incoming moves and messages from an individual
			player.
		2. Inform the game of the players moves & messages.
		3. Inform the game when the connection to the player has been 
			lost so the game can remove the player from the players
			list.

	Objects of this class are created by the game for each player and are
	deleted when the connection to the player is lost.
*/
class PlayerListener extends Thread {
	private Player p;
	private Game game;

	public PlayerListener(Player p1, Game g1) {
		/*
			Pre-conditions: None

			Post-conditions: 
				p and game will have been instantiated.

		*/
	}

	public void run() {
		/*
			Pre-conditions: p and game have been instantiated.

			Post-conditions: 
				There will no longer be a connection to the player
				and the player can be removed from the game.

			Semantics:
				This method will loop while listening for messages
				and moves from p. When the connection to the player
				is lost this method will inform the game and stop
				looping.

		*/
	}
}