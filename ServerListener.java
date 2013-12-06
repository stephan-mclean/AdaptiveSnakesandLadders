/*
	Class: ServerListener

	ROLES:
		1. Listen for incoming messages from the server.
		2. Parse the message and inform the clients interface
		   of the appropriate action.

*/
import java.io.BufferedReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.io.StringReader;

class ServerListener extends Thread {
	private Client client;
	private BufferedReader in;
	private String message;

	public ServerListener(Client c, BufferedReader i) {
		/*
			Pre-conditions: None

			Post-conditions: client will be set to c and in will be
							 set to i.

		*/
		client = c;
		in = i;
	}

	public void run() {
		/*
			Pre-conditions: None

			Post-conditions: Connection to the server will be closed and the
							 client interface will be informed.

			Semantics:
				This method will loop constantly listening for messages from the server.
				Messages may be player moves, messages from the server or messages
				from other players.
				This method will parse the message and inform the client interface.
		*/
		try {
			while(true) {
				// Parse the message and update the interface if necessary.
				message = in.readLine();
				if(message != null) {
					DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    				DocumentBuilder builder = factory.newDocumentBuilder();
    				InputSource is = new InputSource(new StringReader(message));
    				Document doc = builder.parse(is);

    				Element root = doc.getDocumentElement();

    				// Message or presence?
    				if(root.getTagName().equals("message")) {
    					parseMessage(root);
    				}
    				else if(root.getTagName().equals("presence")) {
    					parsePresence(root);
    				}
    			}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		} 

		// Connection to server has been lost -- do something here.
	}

	void parseMessage(Element root) {
		/*
			Messages may be chat messages, player moves or 
			game related messages e.g start game.
			This method will parse them and take appropriate action.
		*/
		String type = root.getAttribute("type"); // Null?
		if(type.equals("chat")) {
			Element from = (Element) root.getElementsByTagName("from").item(0);
    		Element body = (Element) root.getElementsByTagName("body").item(0);
    		client.showMessage(from.getTextContent(), body.getTextContent());
		}
		else if(type.equals("move")) {
			// Get current position of piece to move first.
			Element from = (Element) root.getElementsByTagName("from").item(0);
			Element xElem = (Element) from.getElementsByTagName("x").item(0);
			Element yElem = (Element) from.getElementsByTagName("y").item(0);
			int fromX = Integer.parseInt(xElem.getTextContent());
			int fromY = Integer.parseInt(yElem.getTextContent());

			// New position
			Element to = (Element) root.getElementsByTagName("to").item(0);
			Element toXElem = (Element) to.getElementsByTagName("x").item(0);
			Element toYElem = (Element) to.getElementsByTagName("y").item(0);
			int x = Integer.parseInt(toXElem.getTextContent());
			int y = Integer.parseInt(toYElem.getTextContent());


			Element col = (Element) root.getElementsByTagName("colour").item(0);
			String colour = col.getTextContent();

			// Move the piece
			client.movePiece(fromX, fromY, x, y, colour);

		}
		else if(type.equals("set")) {
			// Setting a piece on the board for the first time
			Element toXElem = (Element) root.getElementsByTagName("x").item(0);
			Element toYElem = (Element) root.getElementsByTagName("y").item(0);
			Element col = (Element) root.getElementsByTagName("colour").item(0);
			Element id = (Element) root.getElementsByTagName("id").item(0);
			int x = Integer.parseInt(toXElem.getTextContent());
			int y = Integer.parseInt(toYElem.getTextContent());

			if(col != null) {
				String colour = col.getTextContent();
				client.setPiece(x, y, colour);
			}
			else if(id != null) {
				int i = Integer.parseInt(id.getTextContent());
				client.setPiece(x, y, i);
			}
		}
		else {
			Element start, finish, turn = null;
			start = (Element) root.getElementsByTagName("start").item(0);
			finish = (Element) root.getElementsByTagName("finish").item(0);
			turn = (Element) root.getElementsByTagName("turn").item(0);

			if(start != null) {
				// Game is starting, show the board.
				client.setBoardVisible(true);
			}
			else if(finish != null) {
				client.gameOver();
			}
			else if(turn != null) {
				String name = turn.getTextContent();
				client.turn(name);
			}
		}
	}

	void parsePresence(Element root) {
		/*
			TOOD:
				Improve this method later.
		*/
		Element n = (Element) root.getElementsByTagName("name").item(0);
		Element col = (Element) root.getElementsByTagName("colour").item(0);
		String name = n.getTextContent();
		boolean host = Boolean.parseBoolean(n.getAttribute("host"));
		String colour = col.getTextContent();
		String type = root.getAttribute("type");
		if(type.equals("online")) {
			client.addPlayer(name, host, colour);
		}
		else {
			client.removePlayer(name, colour);
		}
	}
}