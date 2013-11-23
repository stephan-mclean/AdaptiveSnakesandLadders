/*
	Skeleton class: ServerListener

	ROLES:
		1. Listen for incoming messages from the server.
		2. Parse the message and inform the clients interface
		   of the appropriate action.

*/

class ServerListener extends Thread {
	private Client client;
	private Server server;

	public ServerListener(Client c, Socket s) {
		/*
			Pre-conditions: None

			Post-conditions: client will be set to c and server will be
							 set to s.

		*/
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
	}
}