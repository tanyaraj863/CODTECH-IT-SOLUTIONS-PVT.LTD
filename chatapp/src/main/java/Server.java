import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 1234;
    private static final Set<PrintWriter> clientWriters = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            boolean running = true;  // ✅ use flag instead of while(true)
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("New client connected: " + socket);

                    // Create a new thread for each client
                    new ClientHandler(socket).start();
                } catch (IOException e) {
                    System.err.println("Error accepting client: " + e.getMessage());
                    running = false; // exit loop gracefully
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                clientWriters.add(out);

                while (true) {
                    String message = in.readLine();
                    if (message == null) break; // client disconnected

                    System.out.println("Received: " + message);

                    // Broadcast to all clients
                    for (PrintWriter writer : clientWriters) {
                        writer.println(message);
                    }
                }
            } catch (IOException e) {
                System.err.println("Client error: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
