// Importing required classes
import java.util.*;           // Not directly used here, but useful for general-purpose classes
import java.io.*;             // For Input/Output handling (e.g., IOException)
import java.net.ServerSocket; // To create the server socket
import java.net.Socket;       // To create client socket (individual client connections)

// Main class for the server-side chat application
public class ChatServer 
{
    // Declaring a ServerSocket variable to listen for incoming client connections
    private ServerSocket serverSocket;

    // Constructor to initialize the serverSocket with the one passed
    public ChatServer(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket; // Assign the passed ServerSocket to the class variable
    }

    // Method to start the server and accept incoming clients
    public void startServer()
    {
        try
        {
            // Keep running until the server socket is closed
            while(!serverSocket.isClosed())
            {
                // Accept an incoming client connection (this is a blocking call)
                Socket socket = serverSocket.accept();

                // Print message to console when a client connects
                System.out.println("A New Client Has Connected");

                // Create a new ClientHandler object to manage this client's communication
                ClientHandler clientHandler = new ClientHandler(socket);

                // Create a new thread for this client handler to handle client separately
                Thread thread = new Thread(clientHandler);

                // Start the thread so communication with client can happen in parallel
                thread.start();
            }
        }
        catch(IOException exception)
        {
            // Print any errors that occur while accepting connections
            exception.printStackTrace();
        }
    }

    // Method to close the server socket gracefully
    public void closeServerSocket()
    {
        try
        {
            // Check if the server socket is not already null
            if(serverSocket != null)
            {
                // Close the server socket
                serverSocket.close();
            }
        }
        catch(IOException exception)
        {
            // Print any error that happens while closing the socket
            exception.printStackTrace();
        }
    }

    // Main method  entry point of the server program
    public static void main(String[] args) throws IOException
    {
        int port = 9090; // Port number on which server will listen for connections

        // Create a new ServerSocket that listens on the specified port
        ServerSocket serverSocket = new ServerSocket(port);

        // Create ChatServer object using the server socket
        ChatServer server = new ChatServer(serverSocket);

        // Start the server to begin accepting clients
        server.startServer();
    }
}
