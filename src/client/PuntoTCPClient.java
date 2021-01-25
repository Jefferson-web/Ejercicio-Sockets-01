package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import model.Punto;

public class PuntoTCPClient {

    private Socket clientSocket;

    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    private final String HOST_ADDRESS = "127.0.0.1";
    private final int PORT = 9000;
    
    private DecimalFormat df = new DecimalFormat("0.00");

    public PuntoTCPClient() {

        try {

            clientSocket = new Socket(HOST_ADDRESS, PORT);

            crearFlujos();

            Punto p1 = new Punto(7, 4);

            send(p1);

            System.out.println(p1);

            Punto p2 = new Punto(1, 2);

            send(p2);

            System.out.println(p2);

            double distancia = (double) receive();

            System.out.println("La distancia entre los puntos es " + df.format(distancia) + " unidades.");

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        } finally {

            try {

                if (outToServer != null) {
                    outToServer.close();
                }
                if (inFromServer != null) {
                    inFromServer.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public static void main(String[] args) {

        new PuntoTCPClient();

    }

    private void crearFlujos() throws IOException {

        outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        inFromServer = new ObjectInputStream(clientSocket.getInputStream());

    }

    public void send(Object o) throws IOException {

        outToServer.writeObject(o);

    }

    public Object receive() throws IOException, ClassNotFoundException {

        return inFromServer.readObject();

    }

}
