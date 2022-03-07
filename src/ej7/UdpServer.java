package ej7;

import java.io.*;
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
        byte[] buf = new byte[1024];


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

                ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(buf));
                Persona receivedPerson = (Persona) iStream.readObject();

                String name = receivedPerson.getNombre();
                String surname = receivedPerson.getApellido();

                System.out.println("SERVER: Received " + name + " " + surname + " from " +
                        request.getAddress().toString() + ":" + request.getPort());

                // Prepare datagram to send response
                InetAddress clientAddress = request.getAddress();
                int clientPort = request.getPort();

                char c;
                // Reverse name
                StringBuilder newName = new StringBuilder();
                for (int i = 0; i < name.length(); i++) {
                    c = name.charAt(i); // extracts each character
                    newName.insert(0, c); // adds each character in front of the existing string
                }

                // Reverse surname
                StringBuilder newSurname = new StringBuilder();
                for (int i = 0; i < surname.length(); i++) {
                    c = surname.charAt(i); // extracts each character
                    newSurname.insert(0, c); // adds each character in front of the existing string
                }

                Persona newPersona = new Persona(newName.toString(), newSurname.toString());

                // Serialize to a byte array
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                ObjectOutput oo = new ObjectOutputStream(bStream);
                oo.writeObject(newPersona);

                byte[] serializedObject = bStream.toByteArray();

                // Send response
                DatagramPacket response = new DatagramPacket(serializedObject, serializedObject.length, clientAddress, clientPort);
                socket.send(response);

                System.out.println("SERVER: Sending " + newPersona.getNombre() + " "
                        + newPersona.getApellido() + " to " + clientAddress.toString() + ":"
                        + clientPort);

                iStream.close();
                oo.close();
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
