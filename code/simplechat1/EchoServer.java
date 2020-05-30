// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import common.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	
	// Instance variables ******************************************
	
	ChatIF serverUI;
	boolean isClosed=false;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) throws IOException
  {
    super(port);
	this.serverUI = serverUI;
	listen();
  }


  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the end-user of the server.
   *
   * @param msg The message received from the end-user.
   */
  public void handleMessageFromServerUI (String message)throws IOException {
	  if (message.charAt(0)=='#'){
		String[] msg = message.split(" ");
		switch(msg[0]){
			case "#quit":
				serverClosed();
				isClosed=true;
				break;
			case "#stop":
				stopListening();
				break;
			case "#close":
				close();
				isClosed=true;
				break;
			case "#setport":
				if (isClosed){
					try{
						setPort(Integer.parseInt(msg[1]));
					} catch(IndexOutOfBoundsException | NumberFormatException e){
						System.out.println("No port number was provided.");
					}
				}
				else {
					System.out.println("Can't set a port number when open.");
				}
				break;
			case "#start":
				if (!isListening())
					listen();
				else {
					System.out.println("The server is already listening to clients.");
				}
				break;
			case "#getport":
				System.out.println("The port number is "+getPort());
				break;
			default:
				System.out.println ("Unknown command");
		}
	} 
	else{ 
		serverUI.display("YOU TYPED: " + message);
		String serverMsg = "SERVER MSG> " + message;
		this.sendToAllClients(serverMsg);	  
	  }
  }
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
  }
  
  /**
   * This method is called each time a new client connection is
   * accepted.
   * @param client the connection connected to the client.
   */
  
  public void clientConnected(ConnectionToClient client) {
		System.out.println
      ("The client " + client + " has connected."); 
	}
	
	/**
   * This method is called each time a client disconnects.
   * @param client the connection connected to the client.
   */
 synchronized public void clientDisconnected(ConnectionToClient client) {
		System.out.println("The client " + client +" has disconnected.");
	}
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  public void serverClosed() {
	  System.out.println("The server is being closed.");
	  System.exit(0);
  }
  
}
//End of EchoServer class
