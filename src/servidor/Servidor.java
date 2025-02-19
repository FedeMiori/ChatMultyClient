package servidor;

import java.io.IOException;

public class Servidor {
    private static final int POOL_SIZE = 11;
    private static final int DEFAULT_PORT = 5000;

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
        try{
            NetworkService miServer = new NetworkService( getPort(args) , POOL_SIZE);
            miServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}



