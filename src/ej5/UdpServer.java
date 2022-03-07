package ej5;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: ej5.UdpServer <port_number>");
            System.exit(-1);
        }

        DatagramSocket socket = null;
        byte[] buf = new byte[256];


        try {
            // Create a server socket
            socket = new DatagramSocket(Integer.parseInt(argv[0]));

            // Set maximum timeout to 300 secs
            socket.setSoTimeout(300000);

            while (true) {
                // Prepare datagram for reception
                DatagramPacket request = new DatagramPacket(buf, buf.length);

                // Receive the message
                socket.receive(request);

                String message = new String(request.getData(), 0, request.getLength());

                System.out.println("SERVER: Received " + message + " from " +
                        request.getAddress().toString() + ":" + request.getPort());

                // count vowels of the message
                int count = 0;

                for (int i = 0; i < message.length(); i++) {
                    char ch = message.charAt(i);
                    if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                        count++;
                    }
                }

                // Prepare datagram to send response
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();
                String data = "Your text has " + count + " vowels";
                buf = data.getBytes();

                // Send response
                DatagramPacket response = new DatagramPacket(buf, buf.length, clientAddress, clientPort);
                socket.send(response);

                System.out.println("SERVER: Sending "
                        + new String(response.getData(), 0, response.getLength())
                        + " to " + clientAddress.toString() + ":"
                        + clientPort);
            }

            // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the socket
            socket.close();
        }
    }
}
