import java.io.BufferedReader;  // Importing BufferedReader class for reading input
import java.io.PrintWriter;     // Importing PrintWriter class for writing output
import java.net.Socket;         // Importing Socket class to connect to the server
import java.net.ServerSocket;   // Importing ServerSocket class (though not used here, it's useful for server-side)
import java.io.InputStreamReader; // Importing InputStreamReader to read data from streams
import java.io.IOException;     // Importing IOException class to handle I/O exceptions

class Chat_Client {  // Main class for the client-side of the chat
    public static void main(String[] args) {  // Main method to execute the client program
        Socket clientSocket = null;  // Declare the Socket object to represent client connection to server
        PrintWriter output = null;   // Declare the PrintWriter to send data to the server
        BufferedReader userInput = null;  // Declare the BufferedReader to read user input from the console

        try {
            // Create a socket that connects to the server at localhost and port 5000
            clientSocket = new Socket("localhost", 5000);  
            System.out.println("Connected To Server : " + clientSocket);  // Print the socket connection information

            // Create BufferedReader to receive messages from the server (input stream)
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Create PrintWriter to send messages to the server (output stream)
            output = new PrintWriter(clientSocket.getOutputStream(), true);    

            // Create a new thread to handle reading messages from the server asynchronously
            Thread readThread = new Thread(() -> {
                String message;  // Variable to hold incoming messages
                try {
                    // Continuously read messages from the server until the stream ends
                    while ((message = input.readLine()) != null) {
                        System.out.println("Server : " + message);  // Print the received message from server
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();  // Handle exceptions during message reading
                } finally {
                    try {
                        input.close();  // Close the input stream once done
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);  // Handle any errors when closing the stream
                    }
                }
            });

            // Start the thread that reads messages from the server
            readThread.start();

            // Create BufferedReader to take user input from the console
            userInput = new BufferedReader(new InputStreamReader(System.in));
            String message;  // Variable to hold user input from the console
            // Continuously read user input and send it to the server
            while ((message = userInput.readLine()) != null) {
                output.println(message);  // Send user input to the server
            }
        } catch (IOException exception) {
            exception.printStackTrace();  // Handle I/O exceptions during client-side operations
        } finally {
            // Cleanup: Close resources when done
            output.close();  // Close the output stream
            try {
                userInput.close();  // Close the user input stream
                clientSocket.close();  // Close the client socket
            } catch (IOException exception) {
                throw new RuntimeException(exception);  // Handle errors during cleanup
            }
        }
    }
}