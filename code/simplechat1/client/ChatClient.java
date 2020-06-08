// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;
import java.util.Scanner;

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

  Object loginID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(Object loginID, String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    openConnection();
    sendToServer("#login " + loginID);
  }

  public ChatClient(String host, int port, ChatIF clientUI){
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.loginID = loginID;
    connectionLogger = true;
    //openConnection();
    //sendToServer("#login " + loginID);
  }


  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    //System.out.println("DATA INCOMING" + msg.toString());
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try{

      if (message.equals("#quit")|| message.equals("#logoff")|| message.equals("#sethost")|| message.equals("#setport")|| message.equals("#login")|| message.equals("#gethost")|| message.equals("#getport")){
        specialCommand(message);
      }

      else{
        sendToServer(message);
      }
      
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  int loginChecker = 0;

  private void specialCommand(String message){

    if (message.equals("#quit")){
      try{
        closeConnection();

      }

      catch(Exception e){
        System.exit(0);
      }

      finally{
        System.exit(0);
      }
    }

    else if (message.equals("#logoff")) {
      try{
        closeConnection();
        connectionClosed(true);

      }

      catch(Exception e){
        System.exit(0);
      }
    }

    else if (message.equals("#sethost")) {
     
      if (connectionLogger == true){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Host Name ");
        String s = in.nextLine();        
        setHost(s);
      }

      else if(connectionLogger == false){
        System.out.println("You are still connected to the server, disconnect first");
      }
      
    }

    else if (message.equals("#setport")) {
      if (connectionLogger == true){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Port Number ");
        int n = in.nextInt();
        setPort(n);
      }

      else if(connectionLogger == false){
        System.out.println("You are still connected to the server, disconnect first");
      }
    }

    else if (message.equals("#login")) {
      try{
        //openConnection();
        if (connectionLogger == true){
          openConnection();
          sendToServer("#login " + loginID);
        }
        else{
          System.out.println("You are already logged in");
        }
        connectionLogger = false;

      }

      catch(Exception e){
        System.exit(0);
      }
    }

    else if (message.equals("#gethost")) {
      System.out.println("The Host is " + getHost());
    }

    else if (message.equals("#getport")) {
      System.out.println("The Port is " + getPort());
    }

    else{
      System.out.println("Invalid Keyword");
    }
  }

  boolean connectionLogger = false;

  protected void connectionClosed(boolean value) 
  {
    if (value == true){

      connectionLogger = true;
    }
  }

  /**
   * Hook method called each time an exception
   * is raised by the client listening thread.
   *
   * @param exception the exception raised.
   */
  protected void connectionException(Exception exception) 
  {
    try{
      closeConnection();
      System.out.println("Disconnected from server");
      connectionLogger = true;
      //quit();
    }

    catch(Exception e){
      System.exit(0);
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
