import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to chat server");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread to listen to server messages
            Thread listener = new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.err.println("Disconnected from server: " + e.getMessage());
                }
            });
            listener.start();

            // Main thread to send messages
            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (!scanner.hasNextLine()) {
                    break; // prevents "cannot complete normally" warning
                }
                String userMsg = scanner.nextLine();
                if ("exit".equalsIgnoreCase(userMsg)) {
                    System.out.println("Goodbye!");
                    break;
                }
                out.println("Client: " + userMsg);
            }

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}
