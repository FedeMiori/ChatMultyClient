package servidor;

import servidor.controladores.ControladorGrupos;
import servidor.controladores.ControladorUsuarios;

import java.io.IOException;

public class Servidor {
    private static ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
    private static ControladorGrupos controladorGrupos = new ControladorGrupos();

    public static void main(String[] args) {
        try{
            NetworkService miServer = new NetworkService(5000,11, controladorUsuarios, controladorGrupos);
            miServer.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}



