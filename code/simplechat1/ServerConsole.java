// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import client.*;
import common.*;
import java.util.Scanner;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ServerConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  int port;

  EchoServer server;
 public ServerConsole(int port) 
  {
    server= new EchoServer(port, this);

  }


  //Instance methods ************************************************
  
  /**
   * This method waits for input from the Server.  Once it is 
   * received, it sends it to the client's.
   */
  public void accept() 
  {
    try
    {
      BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
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
        System.out.println
        (ex);
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
    System.out.println("SERVER MSG> " + message);
  }

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {


    String host = "";
    int port = 0;  //The port number

    try{
    	port = Integer.parseInt(args[0]);
    }

    catch(Exception e){
    	port = DEFAULT_PORT;
    }  

    ServerConsole chat= new ServerConsole(port); 

    try
    {
      chat.server.listen();
    }
    catch(Exception e)
    {
      System.out.println(e);
    }
   
    chat.accept();  //Wait for console data
  }
}
//End of ConsoleChat class
