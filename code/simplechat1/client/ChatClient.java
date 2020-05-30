// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
	ChatIF clientUI; 

	String loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginID, String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	this.loginID = loginID;
    openConnection();
	sendToServer("#login "+loginID);
  }

  
  //Instance methods ************************************************
   	/**
	 * This method handles the closure of connection.
	 */
	
	public void connectionClosed() {
		System.out.println("The connection has been closed.");
		System.exit(0);
	} 
	
	/**
	 * This method is called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
	
	public void connectionException(Exception exception) {
		System.out.println("ERROR: "+ exception+". The server has shut down.");
		System.exit(0);
	} 
	
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)throws IOException{
	  if (message.charAt(0)=='#'){
		String[] msg = message.split(" ");
		switch(msg[0]){
			case "#quit":
				quit();
				break;
			case "#logoff":
				if (isConnected()){
					try{
						closeConnection();
					} catch(IOException e){
						System.out.println("You are logging off the chat");
					}
				}
				else {
					System.out.println("You are not connected");
				}
				break;
			case "#sethost":
				if (!isConnected()){
					try{
						setHost(msg[1]);
					} catch(IndexOutOfBoundsException e){
						System.out.println("No host name was provided.");
					}
				}
				else {
					System.out.println("Can't set a host name when connected.");
				}
				break;
			case "#setport":
				if (!isConnected()){
					try{
						setPort(Integer.parseInt(msg[1]));
					} catch(IndexOutOfBoundsException | NumberFormatException e){
						System.out.println("No port number was provided.");
					}
				}
				else {
					System.out.println("Can't set a port number when connected.");
				}
				break;
			case "#login":
				try 
				{
					if (msg[1]!=null)
						sendToServer(message);
				}
				catch(IndexOutOfBoundsException e)
				{
					if (!isConnected())
						openConnection();
					else 
					{
						System.out.println("Already connected.");
					}
				}
				break;
			case "#gethost":
				System.out.println("The host name is "+getHost());
				break;
			case "#getport":
				System.out.println("The port number is "+getPort());
				break;
		}
	} 
	else{ 
		try
		{
		  sendToServer(message);
		}
		catch(IOException e)
		{
		  clientUI.display
			("Could not send message to server.  Terminating client.");
		  quit();
		}
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
