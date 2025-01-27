package servidor;

import servidor.controladores.ControladorUsuario;

import java.io.IOException;

public class Servidor {
    private static ControladorUsuario  controladorUsuario = new ControladorUsuario();

    public static void main(String[] args) {
        try{
            NetworkService miServer = new NetworkService(5000,11, controladorUsuario);
            miServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}



