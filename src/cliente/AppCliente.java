package cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AppCliente {
    final static String HOST = "127.0.0.1";
    final static int PORT = 5000;

    public static void main(String[] args) {
        DataInputStream in;
        DataOutputStream out;

        try {
            Socket sc = new Socket(HOST, PORT);
            System.out.println("Cliente conectado!");
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            String comando = "";
            while( ! comando.equals("exit")){
                System.out.println( "\n" + in.readUTF());
                comando = pedirComando();
                out.writeUTF( comando );
            }

            sc.close();
            in.close();
            out.close();

            System.out.println("Cerramos el cliente.java");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static String pedirComando(){
        System.out.print( "\n" + "Comando>");
        Scanner teclado = new Scanner(System.in);
        return teclado.nextLine();
    }
}
