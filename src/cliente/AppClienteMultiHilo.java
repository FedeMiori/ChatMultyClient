package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AppClienteMultiHilo {
    final static String HOST = "127.0.0.1";
    final static int PORT = 5000;

    public static void main(String[] args) {
        try {
            Socket sc = new Socket(HOST, PORT);
            System.out.println("Cliente conectado!");

            Thread recibirHilo = new Thread(() -> {
                try {
                    DataInputStream in = new DataInputStream(sc.getInputStream());
                    while (true) {
                        String mensaje = in.readUTF();
                        System.out.println("\nServidor: " + mensaje + "\n");
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

            // Iniciar los hilos
            recibirHilo.start();
            enviarHilo.start();

            // Esperar a que el hilo de enviar termine
            enviarHilo.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Error en el cliente: " + e.getMessage());
        }
    }
}
