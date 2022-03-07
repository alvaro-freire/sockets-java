package ej6;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Implements a client using TCP
 */
public class Client {

    public static void main(String argv[]) {
        if (argv.length != 3) {
            System.err.println("Format: ej6.TcpClient <server_address> <port_number> <number>");
            System.exit(-1);
        }
        Socket socket = null;
        try {
            // Obtains the server IP address
            InetAddress serverAddress = InetAddress.getByName(argv[0]);
            // Obtains the server port
            int serverPort = Integer.parseInt(argv[1]);
            // Initializes object
            Numeros numeros = new Numeros(Integer.parseInt(argv[2]));
            // Creates the socket and establishes connection with the server
            socket = new Socket(serverAddress, serverPort);
            // Set a maximum timeout of 300 secs
            socket.setSoTimeout(300000);

            System.out.println("CLIENT: Connection established with "
                    + serverAddress.toString() + " - Remote Port: " + socket.getPort()
                    + " - Local port: " + socket.getLocalPort());

            // get the output stream from the socket.
            OutputStream sOutput = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(sOutput);

            System.out.println("CLIENT: Sending number " + numeros.getNumero() +
                    " to " + socket.getInetAddress().toString() +
                    ":" + socket.getPort());

            // Send message to the server
            objectOutputStream.writeObject(numeros);

           // get the input stream from the connected socket
           InputStream sInput = socket.getInputStream();
           // create a DataInputStream so we can read data from it.
           ObjectInputStream objectInputStream = new ObjectInputStream(sInput);

           Numeros response = (Numeros) objectInputStream.readObject();

           //// Receive the server message
           int number = response.getNumero();
           long square = response.getCuadrado();
           long cube = response.getCubo();

           System.out.println("CLIENT: Received number " + number
                   + ", square " + square + ", cube " + cube
                   + " from " + socket.getInetAddress().toString()
                   + ":" + socket.getPort());

            // Close streams and release connection
            sOutput.close();
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
