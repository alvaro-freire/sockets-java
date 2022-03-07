package ej7;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * Implements an echo client using UDP
 */
public class UdpClient {

    public static void main(String argv[]) {
        if (argv.length != 4) {
            System.err.println("Format: ej5.UdpClient <server_address> <port_number> <name> <surname>");
            System.exit(-1);
        }
        DatagramSocket sDatagram = null;

        Persona persona = new Persona(argv[2], argv[3]);

        try {
            // Create a non connection-oriented socket
            // (use any available port)
            sDatagram = new DatagramSocket();
            // Set timeout to 300 secs
            sDatagram.setSoTimeout(300000);
            // Obtain server IP address from first argument
            InetAddress serverAddress = InetAddress.getByName(argv[0]);
            // Obtain server port from second argument
            int serverPort = Integer.parseInt(argv[1]);

            // Serialize to a byte array
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            ObjectOutput oo = new ObjectOutputStream(bStream);
            oo.writeObject(persona);

            byte[] serializedObject = bStream.toByteArray();

            // Prepare the datagram
            DatagramPacket dgramSent = new DatagramPacket(serializedObject,
                    serializedObject.length, serverAddress, serverPort);
            // Send the datagram
            sDatagram.send(dgramSent);

            System.out.println("CLIENT: Sending "
                    + persona.getNombre() + " " + persona.getApellido() + " to "
                    + dgramSent.getAddress().toString() + ":"
                    + dgramSent.getPort());


            // Prepare datagram for data reception
            byte response[] = new byte[1024];
            DatagramPacket dgramRec = new DatagramPacket(response, response.length);

            // Receive the message
            sDatagram.receive(dgramRec);

            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(response));
            Persona newPersona = (Persona) iStream.readObject();

            String name = newPersona.getNombre();
            String surname = newPersona.getApellido();

            System.out.println("CLIENT: Received "
                    + name + " " + surname + " from "
                    + dgramRec.getAddress().toString() + ":" + dgramRec.getPort());

            iStream.close();
            oo.close();
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Close socket to release connection
            sDatagram.close();
        }
    }
}
