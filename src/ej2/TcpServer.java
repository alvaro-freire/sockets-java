package ej2;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

/**
 * MonoThread TCP echo server.
 */
public class TcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: ej2.TcpServer <port>");
            exit(-1);
        }

        int connections = 0;

        System.out.println("Opened server on port " + argv[0]);

        ServerSocket serverSocket = null;

        try {
            // Create a server socket
            serverSocket = new ServerSocket(Integer.parseInt(argv[0]));

            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);

            while (true) {
                if (connections == 2) {
                    serverSocket.close();
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("No more connections available");
                    exit(1);
                }

                connections++;
                // Wait for connections
                Socket socket = serverSocket.accept();

                // Set the input channel
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                // Set the output channel
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                // Receive the client message
                String message = reader.readLine();
                System.out.println("SERVER: Received " + message
                        + " from " + socket.getInetAddress().toString()
                        + ":" + socket.getPort());

                message = message.toLowerCase();

                // Send response to the client
                writer.println(message);
                System.out.println("SERVER: Sending " + message +
                        " to " + socket.getInetAddress().toString() +
                        ":" + socket.getPort());

                // Close the streams
                input.close();
                output.close();
            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //Close the socket
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
