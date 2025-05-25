import java.io.BufferedReader;  // Importing BufferedReader class for reading input
import java.io.PrintWriter;     // Importing PrintWriter class for writing output
import java.net.Socket;         // Importing Socket class for networking
import java.net.ServerSocket;   // Importing ServerSocket class for accepting client connections
import java.io.IOException;     // Importing IOException class for handling input/output exceptions
import java.io.InputStreamReader; // Importing InputStreamReader for reading data from streams

class Chat_Server { 
    public static void main(String[] args) { 
        ServerSocket serverSocket = null;    // Declare ServerSocket object for listening to client requests
        Socket clientSocket = null;          // Declare Socket object for client connection
        PrintWriter output = null;           // Declare PrintWriter object to send messages to client
        BufferedReader userInput = null;     // Declare BufferedReader object for reading user input from the console

        try { 
            // Create ServerSocket object that listens on port 5000
            serverSocket = new ServerSocket(5000);
            System.out.println("Server Started : Waiting For Clients...");  // Print server status message

            // Accept client connection when a client tries to connect
            clientSocket = serverSocket.accept(); 
            System.out.println("Client Connected : " + clientSocket); // Print client connection info

            // Create BufferedReader object to read data sent by the client
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Create PrintWriter object to send data to the client
            output = new PrintWriter(clientSocket.getOutputStream(), true);

            // Create a new thread to read messages from the client asynchronously
            Thread readThread = new Thread(() -> {
                String message; // String variable to store incoming message
                try { 
                    // Loop to continuously read messages from the client
                    while ((message = input.readLine()) != null) {
                        System.out.println("User : " + message);  // Print received message from client
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();  // Print error if something goes wrong while reading
                } finally {
                    try {
                        input.close();  // Close input stream after communication ends
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);  // Handle error during stream close
                    }
                }
            });

            // Start the reading thread
            readThread.start();

            // Create BufferedReader to read user input from the console
            userInput = new BufferedReader(new InputStreamReader(System.in));

            String message;  // String variable to store user input
            // Loop to continuously take input from the server-side user
            while ((message = userInput.readLine()) != null) {
                output.println(message);  // Send user input to the client
            }

        } catch (Exception exception) {
            exception.printStackTrace();  // Handle any exceptions that occur during server operation
        } finally {
            // Cleanup: Close all resources (streams, sockets)
            output.close(); 
            try { 
                userInput.close();  // Close user input stream
                clientSocket.close();  // Close the client socket
                serverSocket.close();  // Close the server socket
            } catch (IOException exception) {
                throw new RuntimeException(exception);  // Handle any errors during cleanup
            }
        }
    }
}
