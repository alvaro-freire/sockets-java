package ej6;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

/**
 * MonoThread TCP server.
 */
public class Server {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: ej6.TcpServer <port>");
            exit(-1);
        }

        System.out.println("Opened server on port " + argv[0]);

        ServerSocket serverSocket = null;

        try {
            // Create a server socket
            serverSocket = new ServerSocket(Integer.parseInt(argv[0]));

            // Set a timeout of 300 secs
            serverSocket.setSoTimeout(300000);

            while (true) {
                // Wait for connections
                Socket socket = serverSocket.accept();

                // get the input stream from the connected socket
                InputStream sInput = socket.getInputStream();
                // create a DataInputStream so we can read data from it.
                ObjectInputStream objectInputStream = new ObjectInputStream(sInput);

                // get the output stream from the socket.
                OutputStream sOutput = socket.getOutputStream();
                // create an object output stream from the output stream so we can send an object through it
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(sOutput);

                // Receive the client object
                Numeros numeros = (Numeros) objectInputStream.readObject();
                int number = numeros.getNumero();

                System.out.println("SERVER: Received number " + number
                        + " from " + socket.getInetAddress().toString()
                        + ":" + socket.getPort());

                if (number == 0) {
                    serverSocket.close();

                    numeros.setCuadrado(0);
                    numeros.setCubo(0);

                    // Send response to the client
                    objectOutputStream.writeObject(numeros);

                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Received 0. Closing server...");
                    exit(1);
                }

                // Calculate results
                long square = (long) number * number;
                numeros.setCuadrado(square);
                long cube = (long) number * number * number;
                numeros.setCubo(cube);

                // Send response to the client
                objectOutputStream.writeObject(numeros);

                System.out.println("SERVER: Sending number " + number
                        + ", square " + square + ", cube " + cube
                        + " to " + socket.getInetAddress().toString() +
                        ":" + socket.getPort());

                // Close the streams
                sInput.close();
                sOutput.close();
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
