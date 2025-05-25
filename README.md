# Multitheraded-Chat-Application

COMPANY : CODTECH IT SOLUTIONS PVT. LTD.

NAME : MOHD TANZEEM

INTERN ID : CT08DA696

DOMAIN : JAVA PROGRAMMING

DURATION : 8 WEEKS

MENTOR : NEELA SANTHOSH KUMAR

# Task Description: Multithreaded Chat Application

This project is a multithreaded chat application developed in Java as part of my internship assignment. The primary objective of this application is to facilitate real-time communication between multiple clients via a single centralized server. The system leverages Java’s socket programming and multithreading capabilities to create a robust, scalable group chat environment that runs on the command line interface (CLI).

## Project Overview
The chat application implements a classic client-server architecture where a single server manages multiple client connections simultaneously. Each client connects to the server and can send messages that are broadcast to all other connected clients. The server handles each client connection in a separate thread, allowing concurrent communication without blocking.

This approach ensures smooth and real-time message exchange, mimicking a group chat platform where users can join, send messages, and leave the chat dynamically.

## Technologies and Concepts Used
- *Java Standard Edition (SE)*

- *Socket Programming:* Using Socket and ServerSocket classes to establish network connections.

- *Multithreading:* Each client connection is managed in an individual thread using the Runnable interface.

- *Input/Output Streams:* BufferedReader and BufferedWriter are used for efficient message transmission.

- *Command-Line Interface (CLI):* The user interacts with the application through the terminal.

## Detailed Architecture
### Server (ChatServer.java)
- Listens for incoming client connections on a designated port (9090).

- Upon accepting a connection, it spawns a new ClientHandler thread to manage communication with that client.

- Maintains a thread-safe collection of all active client handlers.

- Ensures continuous operation, accepting new clients as long as the server is running.

### Client Handler (ClientHandler.java)
- Dedicated to managing one client connection.

- Reads the client's username initially and adds the client handler to the shared list of active users.

- Listens for messages from the client in its own thread.

- Broadcasts incoming messages to all other connected clients.

- Handles client disconnection gracefully by removing the client from the list and notifying other users.

### Client (ChatClient.java)
- Connects to the server at localhost on port 9090.

- Sends the username to the server upon connection.

- One for sending user input messages to the server.

- Another for listening to broadcast messages from the server.

- Displays all incoming messages in real-time on the user’s console.

## Key Features
- Concurrent Client Handling: Supports multiple clients chatting simultaneously without blocking.

- Real-Time Broadcasting: Messages sent by any client are promptly delivered to all other connected clients.

- Username Identification: Each client is identified by a unique username to personalize conversations.

- Graceful Disconnection: Clients leaving the chat are handled without affecting the server or other clients.

- Resource Management: Proper closing of sockets and streams to avoid resource leaks.


# Outputs 
