/*
	Class: PlayerListener

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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.io.StringReader;
import java.io.BufferedReader;
class PlayerListener extends Thread {
	private Player p;
	private Game game;
	private String message;

	public PlayerListener(Player p1, Game g1) {
		/*
			Pre-conditions: None

			Post-conditions: 
				p and game will have been instantiated.

		*/
		p = p1; game = g1;
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
		try {
			while((message = p.getInput().readLine()) != null) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    			DocumentBuilder builder = factory.newDocumentBuilder();
    			InputSource is = new InputSource(new StringReader(message));
    			Document doc = builder.parse(is);

    			Element root = doc.getDocumentElement();

    			// Server only deals with messages
    			if(root.getTagName().equals("message")) {
    				parseMessage(root);
    			}
			}
		}
		catch(Exception e) {}

		game.removePlayer(p);
	}

	void parseMessage(Element root) {
		/*
			Deal with player chat messages, moves and game related messages.

			If the message is chat: send on to all clients, they can parse the XML.
			If it's a move:
				Check game has started, and it's the players turn then:
				Normal move: Get current pos of player piece
							 Calculate new pos.
							 Format XML - from, to, colour
				Adaptive move: 
								Can player make adaptive move?
								Validate move.
								Format XML - from, to, id.
			If it's game related:
				Must be host telling server to start game:
					Validate message is from host.
					Start the game.
		*/
		String type = root.getAttribute("type");
		if(type.equals("chat")) {
			game.tellAll(message);
		}
		else if(type.equals("game")) {
			// Start game or player move.
			Element start, finish, turn = null;
			start = (Element) root.getElementsByTagName("start").item(0);
			finish = (Element) root.getElementsByTagName("finish").item(0);
			turn = (Element) root.getElementsByTagName("turn").item(0);

			if(start != null) {
				// Game is starting, show the board.
				game.start(p);
			}
			else if(turn != null) {
				game.normalMove(p);
				// We will deal with adaptive moves here later.
			}
		}
	}
}