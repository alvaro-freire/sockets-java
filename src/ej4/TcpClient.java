package ej4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Implements an echo client using TCP
 */
public class TcpClient {

    public static void main(String argv[]) {
        if (argv.length != 2) {
            System.err.println("Format: ej4.TcpClient <server_address> <port_number>");
            System.exit(-1);
        }
        Socket socket = null;
        try {
            // Obtains the server IP address
            InetAddress serverAddress = InetAddress.getByName(argv[0]);
            // Obtains the server port
            int serverPort = Integer.parseInt(argv[1]);
            // Creates the socket and establishes connection with the server
            socket = new Socket(serverAddress, serverPort);
            // Set a maximum timeout of 300 secs
            socket.setSoTimeout(300000);
            System.out.println("CLIENT: Connection established with "
                    + serverAddress.toString() + " - Remote Port: " + socket.getPort()
                    + " - Local port: " + socket.getLocalPort());
            // Set the input channel
            BufferedReader sInput = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            // Receive server message
            String received = sInput.readLine();
            System.out.println("CLIENT: Received " + received
                    + " from " + socket.getInetAddress().toString()
                    + ":" + socket.getPort());
            // Close input stream and release connection
            sInput.close();
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
