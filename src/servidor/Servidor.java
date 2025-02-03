package servidor;

import java.io.IOException;

public class Servidor {
    public static void main(String[] args) {
        try{
            NetworkService miServer = new NetworkService(5000,11);
            miServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}



