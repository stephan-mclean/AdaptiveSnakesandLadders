/*
	Helper class to format XML messages for both the client and server.
	
*/

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;
import java.io.StringWriter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

class MessageFormatter {
	
	static String chatMessage(String name, String message) {

		/*
			Create XML for a chat message.
		*/

		StringWriter buffer = new StringWriter();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("message");

			// Format the message
			root.setAttribute("type", "chat");

			// From
			Element from = doc.createElement("from");
			from.appendChild(doc.createTextNode(name));
			root.appendChild(from);

			// Body of message
			Element body = doc.createElement("body");
			body.appendChild(doc.createTextNode(message));
			root.appendChild(body);

			// Create String 
			//createString(buffer, root);
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(root),
    								new StreamResult(buffer));
		}
		catch(Exception e) {} 

		return buffer.toString();
	}

	static String presenceMessage(String name, String colour, String host, String type) {
		
		/*
			Create XML for a presence message which which will be sent
			from the server to the clients.
		*/

		StringWriter buffer = new StringWriter();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("presence");
			root.setAttribute("type", type);

			Element n = doc.createElement("name");
			n.appendChild(doc.createTextNode(name));
			n.setAttribute("host", host);
			root.appendChild(n);

			Element col = doc.createElement("colour");
			col.appendChild(doc.createTextNode(colour));
			root.appendChild(col);

			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(root),
    								new StreamResult(buffer));

		}
		catch(Exception e) {} 

		return buffer.toString();
	}

	static String gameMessage(String type) {
		StringWriter buffer = new StringWriter();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("message");
			root.setAttribute("type", "game");

			// Game message
			Element t = doc.createElement(type);
			root.appendChild(t);


			// Create String 
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(root),
    								new StreamResult(buffer));
		}
		catch(Exception e) {}

		return buffer.toString();
	}

	static String setMessage(String x, String y, String colour, int id) {
		StringWriter buffer = new StringWriter();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("message");
			root.setAttribute("type", "set");

			// x & y
			Element xElem = doc.createElement("x");
			xElem.appendChild(doc.createTextNode(x));
			Element yElem = doc.createElement("y");
			yElem.appendChild(doc.createTextNode(y));
			root.appendChild(xElem); root.appendChild(yElem);

			// Colour or id
			Element col, idElem = null;
			if(id == -1) {
				col = doc.createElement("colour");
				col.appendChild(doc.createTextNode(colour));
				root.appendChild(col);
			}
			else {
				idElem = doc.createElement("id");
				idElem.appendChild(doc.createTextNode("" + id));
				root.appendChild(idElem);
			}

			// Create String 
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(root),
    								new StreamResult(buffer));
		}
		catch(Exception e) {} 

		return buffer.toString();
	}

	static String moveMessage(String x, String y, String toX, String toY, String colour) {
		StringWriter buffer = new StringWriter();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("message");
			root.setAttribute("type", "move");

			Element from = doc.createElement("from");
			Element xElem = doc.createElement("x");
			Element yElem = doc.createElement("y");
			xElem.appendChild(doc.createTextNode(x));
			yElem.appendChild(doc.createTextNode(y));
			from.appendChild(xElem); from.appendChild(yElem);
			root.appendChild(from);

			Element to = doc.createElement("to");
			Element toXElem = doc.createElement("x");
			Element toYElem = doc.createElement("y");
			toXElem.appendChild(doc.createTextNode(toX));
			toYElem.appendChild(doc.createTextNode(toY));
			to.appendChild(toXElem); to.appendChild(toYElem);
			root.appendChild(to);

			// Colour
			Element col = doc.createElement("colour");
			col.appendChild(doc.createTextNode(colour));
			root.appendChild(col);

			// Create String 
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(root),
    								new StreamResult(buffer));
		}
		catch(Exception e) {} 

		return buffer.toString();
	}

	static String playerTurn(String name) {
		StringWriter buffer = new StringWriter();
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
			// root elements
			Document doc = docBuilder.newDocument();
			Element root = doc.createElement("message");
			root.setAttribute("type", "game");

			Element turn = doc.createElement("turn");
			turn.appendChild(doc.createTextNode(name));
			root.appendChild(turn);

			// Create String 
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			buffer = new StringWriter();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.transform(new DOMSource(root),
    								new StreamResult(buffer));
		}
		catch(Exception e) {}
		return buffer.toString();
	}
}