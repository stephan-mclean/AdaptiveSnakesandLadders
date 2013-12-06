/*
	Class: Client

	ROLES:
		1. This class is responsible for getting the players name.
		2. It is responsible for making contact with the server.
		3. This class will also handle setting up the interface
			and modifying it when moves come in from the server.


*/

// Swing imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

// Networking & utils
import java.net.*;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

class Client extends JFrame {
	private String name = "";
	private Socket server;
	private PrintWriter out;
	// Map players to colours for chat.
	private HashMap<String, String> players;

	// GUI

	// basic will hold our left and right panels
	// Left panel will hold the board
	// Right panel will hold online users & roll button.
	private JPanel basic, left, right;
	private JButton [] [] board;
	private JTabbedPane pane;
	private JList onlineUsers;
	private DefaultListModel listModel;
	private JTextPane messages;
	private JTextField messagesIn;
	private JButton roll, start;

	private final int ROWS = 10;
	private final int COLS = 10;

	public Client() {
		/*
			Pre-conditions: None

			Post-conditions:
				name will be instantiated. 
				server will have been set up and a connection to the server
				established.
				The GUI will have been set up.

		*/
		super("Adaptive Snakes and Ladders.");
		while(name.equals("")) {
			name = JOptionPane.showInputDialog("Enter your name");

			// Cancel button pressed?
			if(name == null) {
				System.exit(0);
			}
		}
		players = new HashMap<String, String>();
		initGUI();
		initNetwoking();
	}

	void initGUI() {
		// Set up the board, online users, messages and buttons.

		board = new JButton[ROWS][COLS];

		basic = new JPanel();
		basic.setLayout(new BoxLayout(basic, BoxLayout.X_AXIS));
		add(basic);

		// Set up the board.
		left = new JPanel();
		left.setLayout(new GridLayout(ROWS, COLS));
		setupBoard();
		basic.add(left);

		right = new JPanel();
		
		listModel = new DefaultListModel<>();
		onlineUsers = new JList(listModel);

		// Show player pieceColour beside name in online users list.
		onlineUsers.setCellRenderer(new DefaultListCellRenderer() {
    		@Override
    		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        		
        		String name = (String) listModel.get(index);
        		String colour = players.get(name);
        		ImageIcon icon = new ImageIcon("Images/" + colour + ".jpg");
        		Image img = icon.getImage();
        		Image scaled = img.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        		icon = new ImageIcon(scaled);
        		label.setIcon(icon);
        		return label;
    		}
		});
		onlineUsers.setPreferredSize(new Dimension(200, 500));

		messages = new JTextPane();
		messages.setPreferredSize(new Dimension(200, 470));
		messagesIn = new JTextField();
		messagesIn.addActionListener(new SendMessageListener());
		messagesIn.setPreferredSize(new Dimension(200, 30));
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.add(new JScrollPane(messages));
		messagePanel.add(messagesIn);

		roll = new JButton("Roll");
		roll.setEnabled(false);
		roll.addActionListener(new MakeMoveListener());
		start = new JButton("start");
		start.addActionListener(new StartGameListener());
		pane = new JTabbedPane();
		pane.setPreferredSize(new Dimension(300, 500));
		pane.addTab("Online Users", onlineUsers);
		pane.addTab("Messages", messagePanel);
		right.add(pane);
		right.add(roll); right.add(start);

		basic.add(right);

		setSize(new Dimension(1050, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	void initNetwoking() {
		// Establish a connection with the server.
		// Send the players name, and start the server listener.
		try {
			server = new Socket("localhost", 9999);
			out = new PrintWriter(server.getOutputStream(), true);
			out.println(name);
			BufferedReader in = new BufferedReader(new 
								InputStreamReader(server.getInputStream()));
			new ServerListener(this, in).start();
		}
		catch(Exception e) {}
	}

	void setupBoard() {
		// Layout the buttons on the panel.
		for(int i = 0; i < ROWS; i++) {
			if(i % 2 == 0)
				setupWhiteRow(i);
			else
				setupBlackRow(i);
		}
		setBoardVisible(false);
	}

	void setupWhiteRow(int i) {
		for(int j = 0; j < COLS; j++) {
			JButton b = new JButton();
			if(j % 2 == 0) {
				b.setBackground(Color.WHITE);
			}
			else {
				b.setBackground(Color.BLACK);
				b.setForeground(Color.WHITE);
			}
			addButton(b, i, j);
		}
	}

	void setupBlackRow(int i) {
		for(int j = 0; j < COLS; j++) {
			JButton b = new JButton();
			if(j % 2 == 0) {
				b.setBackground(Color.BLACK);
				b.setForeground(Color.WHITE);
			}
			else {
				b.setBackground(Color.WHITE);
			}
			addButton(b, i, j);
		}
	}

	void addButton(JButton b, int i, int j) {
		b.setOpaque(true);
		b.setBorderPainted(false);
		b.setBorder(null);
		b.setEnabled(false);
		board[i][j] = b;
		left.add(b);
	}

	void setBoardVisible(boolean visibility) {
		// Show / Hide the board
		// Enable / disable buttons.
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				board[i][j].setVisible(visibility);
			}
		}

		repaint();
	}

	void showMessage(String from, String message) {
		/*
			Pre-conditions: None

			Post-conditions: message will be shown on the interface.

		*/

		if(from.equals("SERVER")) {
			appendToPane(from + ":" + message + "\n", Color.BLACK);
		}
		else {
			// Get player colour.
			String colour = players.get(from);
			appendToPane(from + ":" + message + "\n", getColour(colour));
		}
	}

	private Color getColour(String colour) {
		switch(colour) {
			case "blue":
				return Color.BLUE;
			case "red":
				return Color.RED;
			case "green":
				return Color.GREEN;
			case "orange":
				return Color.ORANGE;
			case "yellow":
				return Color.YELLOW;
			case "cyan":
				return Color.CYAN;
			default:
				return Color.BLACK;
		}
	}

	private void appendToPane(String msg, Color c) {
	
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

		aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
		aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

		int len = messages.getDocument().getLength();
		messages.setCaretPosition(len);
		messages.setCharacterAttributes(aset, false);
		messages.replaceSelection(msg);
	}

	void gameOver() {
		JOptionPane.showMessageDialog(null, "Game over", "Game over",
				JOptionPane.INFORMATION_MESSAGE);

		setBoardVisible(false);
		clearBoard();
	}

	void clearBoard() {
		// Remove all icons and text from the board.
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLS; j++) {
				board[i][j].setText("");
				board[i][j].setIcon(null);
			}
		}
	}

	void movePiece(int fromX, int fromY, int x, int y, String colour) {
		/*
			Pre-conditions: None

			Post-conditions: The piece at position [fromX][fromY] will be removed
								and set at position [x][y] on the board.

		*/

		ImageIcon i = new ImageIcon("Images/" + colour + ".jpg", 
									"Images/" + colour + ".jpg");
		CombineIcon icon = (CombineIcon) board[fromX][fromY].getIcon();
		// Remove colour from this icon.
		icon.remove(i);
		board[fromX][fromY].setIcon(icon);
		repaint();

		setPiece(x, y, colour);

	}

	void setPiece(int x, int y, String colour) {
		/*
			Pre-conditions: None

			Post-conditions: A player piece with the specified colour
							 will be set at [x][y]
		*/

		CombineIcon icon = (CombineIcon) board[x][y].getIcon();
		
		if(icon == null) {
			icon = new CombineIcon();
			
		}
		ImageIcon i = new ImageIcon("Images/" + colour + ".jpg", 
									"Images/" + colour + ".jpg");
		Image img = i.getImage();
        Image scaled = img.getScaledInstance(15, 15, java.awt.Image.SCALE_SMOOTH);
        i = new ImageIcon(scaled, "Images/" + colour + ".jpg");
		icon.add(i);
		board[x][y].setIcon(icon);
		repaint();
	}

	void setPiece(int x, int y, int id) {
		/*
			Pre-conditions: None

			Post-conditions: A snake or ladder with the specified id
							 will be set at [x][y]

		*/

		if(id < 3) {
			board[x][y].setText("S" + id);
		}
		else {
			board[x][y].setText("L" + id);
		}

	}

	void turn(String playerName) {
		/*
			Server has sent out a message saying it is
			a certain players turn.
			Check is it our turn and if so show a message

		*/
		if(playerName.equals(name)) {
			// Our turn
			roll.setEnabled(true);
			JOptionPane.showMessageDialog(null, "Your turn", "It's your turn",
				JOptionPane.INFORMATION_MESSAGE);
		}
	}

	void tellServer(String message) {
		/*
			Pre-conditions: None

			Post-conditions: message will be sent to the server.

		*/
		out.println(message);
	}

	void addPlayer(String name, boolean host, String colour) {
		// Add to list of online players and also to our hashmap
		// for keeping track of player colours.
		players.put(name, colour);
		if(!listModel.contains(name)) {
			listModel.addElement(name);
		}
	}

	void removePlayer(String name, String colour) {

		listModel.removeElement(name);
	}

	class SendMessageListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String message = messagesIn.getText();
			tellServer(MessageFormatter.chatMessage(name, message));
			messagesIn.setText("");
		}

	}

	class StartGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String startMessage = MessageFormatter.gameMessage("start");
			tellServer(startMessage);
		}
	}

	class MakeMoveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tellServer(MessageFormatter.playerTurn(name));
			roll.setEnabled(false);
		}
	}

	public static void main(String [] args) {
		Client c = new Client();
		c.setVisible(true);
	}
}