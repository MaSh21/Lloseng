// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;

/**
 * This class constructs the UI for a server client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned from ClientConsole 
 *
 * @author Mariam Shlepchyan
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ServerConsole.
   */
  EchoServer server;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  
  public ServerConsole(int port) 
  {
    try 
    {
      server= new EchoServer(port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating server.");
      System.exit(1);
    }
  }

  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the server's message handler.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        server.handleMessageFromServerUI(message);
      }
    } 
    catch (Exception ex) 
    {
      System.out.println
        ("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }

  
//Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server UI.
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    ServerConsole sv = new ServerConsole(port);
    
    try 
    {
      sv.accept(); //Start listening for
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of ConsoleChat class
