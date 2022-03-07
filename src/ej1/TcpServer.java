package ej1;

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
            System.err.println("Format: ej1.TcpServer <port>");
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

                // Open client connection
                System.out.println("SERVER: Opened connection on " + socket.getInetAddress().toString()
                        + ":" + socket.getPort() + " Local port:" + socket.getLocalPort());
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
