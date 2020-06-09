// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import ocsf.server.*;
import common.ChatIF;
import java.util.Scanner;

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
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */

  int clientJoined;
  int clientLeft; 
  String loginID;
  ChatIF serverUI;;
  public EchoServer(int port)
  {
    super(port);
    int clientJoined = 0;
    int clientLeft = 0;
  }

  public EchoServer(int port, ChatIF serverUI)
  {
    super(port);
    int clientJoined = 0;
    int clientLeft = 0;       
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */

  

  int temp = 0;
  public void handleMessageFromClient(Object msg, ConnectionToClient client)
  {
    try{
      if (msg.toString().charAt(0) == '#'){
        String message = msg.toString();
        System.out.println("A new client is attempting to connect to the server.");
        System.out.println("Message received: " + msg + " from null");
        System.out.println("#login " + message.substring(7, message.length()) + " has joined the server");
        setInfo(message.substring(7, message.length()));
      }
      else{
        System.out.println("Message received: " + msg + " from " + loginID);
      }

      this.sendToAllClients(getInfo() + " " + msg);
    }

    catch(Exception e){
      System.exit(0);
    }
  }

  protected void setInfo(String loginID){
    this.loginID = loginID;
  }

  protected String getInfo(){
    return this.loginID;
  }

  protected void setClientJoined(){
    clientJoined++;
  }

  protected void setClientLeft(){
    clientLeft++;
  }

  protected int getClientJoined(){
    return clientJoined;
  }

  protected int getClientLeft(){
    return clientLeft;
  }  


  protected void clientConnected(ConnectionToClient client) {

    //System.out.println(client + " has joined the server");
    setClientJoined();
  }

  protected void clientDisconnected(ConnectionToClient client) {
    System.out.println(loginID + " left the server");

    setClientLeft();

  }


  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
    clientDisconnected(client);
  }

  public void handleMessageFromServerUI(String message){
    //try{

      if (message.equals("#quit")|| message.equals("#stop")|| message.equals("#close")|| message.equals("#setport")|| message.equals("#start") || message.equals("#getport")){
        specialCommand(message);
      }

      else{    
        serverUI.display(message);    
        sendToAllClients("SERVER MSG> " + message);        
      }

  }

  protected void serverClosed(){
    stopListening();
  }

  private void specialCommand(String message){

    if (message.equals("#quit")){
      try{
        close();
      }

      catch(Exception e){
        System.exit(0);
      }

      finally{
        System.exit(0);
      }
    }

    else if (message.equals("#stop")) {
      try{
        serverUI.display("Server has stopped listening for new connections."); 
        sendToAllClients("WARNING - Server has stopped listening for new connections.");
        serverClosed();
      }

      catch(Exception e){
        System.exit(0);
      }
    }

    else if (message.equals("#close")) {     
      try{
        sendToAllClients("SERVER SHUTTING DOWN! DISCONNECTING!");
        sendToAllClients("Abnormal termination of connection.");
        close();
      }

      catch(Exception e){
        System.exit(0);
      }
      connectionClosed(true);
    }

    else if (message.equals("#setport")) {
      if (connectionLogger == true){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Port Number ");
        int n = in.nextInt();
        setPort(n);
      }

      else if(connectionLogger == false){
        System.out.println("You need to close connection before you can do that");
      }
    }

    else if (message.equals("#start")) {
      try{
        connectionLogger = false;
        //serverStarted();
        listen();
      }

      catch(Exception e){
        System.exit(0);
      }
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
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {

    System.out.println("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {

    if (clientJoined == clientLeft){
      System.out.println("Server has stopped listening for connections.");
      try{
        close();
      }

      catch(Exception e){
        System.exit(0);
      }
    }
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {


    int port = 0; //Port to listen on

    try {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
     
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
