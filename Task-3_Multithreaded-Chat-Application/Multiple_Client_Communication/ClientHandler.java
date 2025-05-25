// Importing required packages for input/output, networking, and utilities
import java.io.*;
import java.net.*;
import java.util.*;

// This class handles each client connected to the chat server
public class ClientHandler implements Runnable 
{
    // A shared list to keep track of all active client handlers (i.e., connected clients)
    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>(); 

    // Instance variables for each client
    private Socket socket;                    // The client socket connection
    private BufferedReader bufferedReader;    // To read messages from the client
    private BufferedWriter bufferedWriter;    // To send messages to the client
    private String clientUserName;            // Username of the connected client

    // Constructor that sets up the client handler when a new client connects
    public ClientHandler(Socket socket)
    {	
        try
        {
            this.socket = socket;

            // Set up input and output streams
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read the client's username (sent first by the client)
            this.clientUserName = bufferedReader.readLine();

            // Add this client handler to the shared list
            clientHandler.add(this);

            // Notify all other clients that a new user has joined
            broadcastMessage("Server : " + clientUserName + " Has Entered The Chat!");
        }
        catch(IOException exception)
        {
            // If something goes wrong, close everything
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }

    // This method runs in a separate thread for each client
    @Override
    public void run()
    {
        String messageFromClient;

        // Keep running while the client is connected
        while(socket.isConnected())
        {
            try
            {
                // Read a message sent from this client
                messageFromClient = bufferedReader.readLine();

                // Broadcast that message to all other clients
                broadcastMessage(messageFromClient);
            }
            catch(IOException exception)
            {
                // If an error occurs, clean up and break the loop
                closeEverything(socket, bufferedWriter, bufferedReader);
                break;
            }
        }
    }

    // Method to send a message to all other connected clients
    private void broadcastMessage(String messageToSend)
    {
        for(ClientHandler clientHandler : clientHandler)
        {
            try
            {
                // Don't send the message back to the sender
                if(!clientHandler.clientUserName.equals(clientUserName))
                {
                    clientHandler.bufferedWriter.write(messageToSend);  // Write the message
                    clientHandler.bufferedWriter.newLine();             // Move to the next line
                    clientHandler.bufferedWriter.flush();               // Send the message immediately
                }
            }
            catch(IOException exception)
            {
                // If an error occurs while broadcasting, close everything
                closeEverything(socket, bufferedWriter, bufferedReader);
            }
        }
    }

    // Method to remove a client from the active list and notify others
    public void removeClientHandler()
    {
        clientHandler.remove(this);  // Remove this handler from the list

        // Let other users know that this client has left
        broadcastMessage("Server : " + clientUserName + " Has Left The Chat!");
    }

    // Method to close all resources related to a client
    public void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader)
    {
        removeClientHandler(); // Remove client from the list first

        try
        {
            if(bufferedReader != null)
            {
                bufferedReader.close(); // Close input stream
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close(); // Close output stream
            }
            if(socket != null)
            {
                socket.close(); // Close socket connection
            }
        }
        catch(IOException exception)
        {
            // Print any errors during closing
            exception.printStackTrace();
        }
    }
}
