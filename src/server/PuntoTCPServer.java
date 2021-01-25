package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import model.Punto;

public class PuntoTCPServer implements Runnable {

    private ServerSocket serverSocket;
    private Socket clientSocket;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private final int PORT = 9000;

    public PuntoTCPServer() {

        try {

            serverSocket = new ServerSocket(PORT);

            while (true) {

                System.out.println("Listen on port " + PORT);

                clientSocket = serverSocket.accept();

                Thread thread = new Thread(this);

                thread.start();

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (outToClient != null) {
                    outToClient.close();
                }
                if (inFromClient != null) {
                    inFromClient.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public static void main(String[] args) {

        new PuntoTCPServer();

    }

    @Override
    public void run() {

        try {

            crearFlujos();

            System.out.println(clientSocket.getInetAddress().getHostAddress() + " Conectado ");

            Punto p1 = (Punto) receive();

            System.out.println(p1);

            Punto p2 = (Punto) receive();

            System.out.println(p2);

            double distancia = calcularDistancia(p1, p2);
            
            send(distancia);

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    }

    private void crearFlujos() throws IOException {

        outToClient = new ObjectOutputStream(clientSocket.getOutputStream());
        inFromClient = new ObjectInputStream(clientSocket.getInputStream());

    }

    public void send(Object o) throws IOException {

        outToClient.writeObject(o);

    }

    public Object receive() throws IOException, ClassNotFoundException {

        return inFromClient.readObject();

    }

    public double calcularDistancia(Punto p1, Punto p2) {

        return Math.sqrt(Math.pow((p2.getX() - p1.getX()), 2) + Math.pow((p2.getY() - p1.getY()), 2));

    }

}
