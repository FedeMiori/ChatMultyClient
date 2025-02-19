package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AppClienteMultiHilo {
    private final static String HOST = "127.0.0.1";
    private final static int DEFAULT_PORT = 5000;


    private static int getPort(String[] args){
        int port;
        try{
            port = Integer.parseInt(args[0]);
            System.out.println("Puerto seleccionado " + port);
        }catch (Exception e){
            port = DEFAULT_PORT;
            System.out.println("Puerto por defecto: " + port);
        }

        return port;
    }

    public static void main(String[] args) {
        try {
            Socket sc = new Socket(HOST, getPort(args));
            System.out.println("Cliente conectado!");

            Thread recibirHilo = new Thread(() -> {
                try {
                    DataInputStream in = new DataInputStream(sc.getInputStream());
                    while (true) {
                        String mensaje = in.readUTF();
                        System.out.println("[SERVIDOR]: " + mensaje);
                    }
                } catch (IOException e) {
                    System.out.println("Conexión cerrada por el servidor.");
                    System.exit(0);
                }
            });

            Thread enviarHilo = new Thread(() -> {
                try {
                    DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                    Scanner teclado = new Scanner(System.in);
                    String comando = "";

                    while (!comando.equals("exit")) {
                        comando = teclado.nextLine();
                        out.writeUTF(comando);
                    }

                    sc.close();
                    teclado.close();
                    System.out.println("Cerramos el cliente.");
                } catch (IOException e) {
                    System.out.println("Error al enviar comandos: " + e.getMessage());
                }
            });

            recibirHilo.start();
            enviarHilo.start();

            enviarHilo.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
