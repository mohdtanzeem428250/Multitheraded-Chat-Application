// Importing necessary classes for input/output, networking, and user input
import java.io.*;
import java.net.*;
import java.util.Scanner;

// Main class for the client-side chat application
public class ChatClient
{
    // Declaring the required variables for client communication
    private Socket socket;                       // To connect to the server
    private BufferedReader bufferedReader;       // To read messages from the server
    private BufferedWriter bufferedWriter;       // To send messages to the server
    private String username;                     // Client's username

    // Constructor to initialize the socket and input/output streams
    public ChatClient(Socket socket, String username)
    {
        try
        {
            this.socket = socket; // Assign the socket
            // Set up writer to send messages to server
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Set up reader to receive messages from server
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username; // Set the username
        }
        catch(IOException exception)
        {
            // If something goes wrong, close everything
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Method to send messages to the server
    public void sendMessage()
    {
        try
        {
            // Send the username to the server first
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            // Create a Scanner to take input from the user
            Scanner scanner = new Scanner(System.in);

            // Keep sending messages as long as the socket is connected
            while(socket.isConnected())
            {
                // Read user's message
                String messageToSend = scanner.nextLine();

                // Format: "Username : message"
                bufferedWriter.write(username + " : " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush(); // Make sure message is sent immediately
            }
        }
        catch(IOException exception)
        {
            // Close everything on exception
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Method to listen for incoming messages from the server (group chat)
    public void listenforMessage()
    {
        // Start a new thread so it can listen in background while user types
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                String msgFromGroupChat;

                // Keep listening while socket is connected
                while(socket.isConnected())
                {
                    try
                    {
                        // Read a message from the server
                        msgFromGroupChat = bufferedReader.readLine();
                        // Print it to the console
                        System.out.println(msgFromGroupChat);
                    }
                    catch(IOException exception)
                    {
                        // Close everything if there's a problem
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start(); // Start the thread
    }

    // Method to close all resources gracefully
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
    {
        try
        {
            if(bufferedReader != null)
            {
                bufferedReader.close();
            }
            if(bufferedWriter != null)
            {
                bufferedWriter.close();
            }
            if(socket != null)
            {
                socket.close();
            }
        }
        catch(IOException exception)
        {
            // Print the error if closing fails
            exception.printStackTrace();
        }
    }

    // Main method entry point for the client program
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        Scanner scanner = new Scanner(System.in); // Scanner for user input

        // Ask the user for a username to use in the chat
        System.out.println("Enter Your Username For The Group Chat :");
        String username = scanner.nextLine();

        // Connect to the server running on localhost at port 9090
        Socket socket = new Socket("localhost", 9090);

        // Create a ChatClient object
        ChatClient client = new ChatClient(socket, username);

        // Start listening for messages in a background thread
        client.listenforMessage();

        // Start sending messages from this thread
        client.sendMessage();
    }
}
