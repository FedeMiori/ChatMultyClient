package servidor;

import servidor.controladores.ControladorUsuario;
import servidor.modelos.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Handler implements Runnable {
    private final Socket socket;
    DataInputStream in;
    DataOutputStream out;
    ControladorUsuario controladorUsuario;
    Usuario usuario;

    Handler(Socket socket, ControladorUsuario controladorUsuario) {
        this.socket = socket;
        this.controladorUsuario = controladorUsuario;
    }

    public void run() {
        try{
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            System.out.println("Comunicación establecida.");

            if(login())
                interfazlogeada();

            in.close();
            out.close();
            socket.close();
            if(usuario != null)
                System.out.println("Cliente: '" +usuario.getNickName()+ "' desconectado");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            if(usuario != null)
                usuario.cerrarSesion();
        }
    }

    private boolean login() throws IOException {
        out.writeUTF("Inserte su nombre de usuario para iniciar sesion");
        String nombreusuario = in.readUTF();
        this.usuario = controladorUsuario.logearUsuario(nombreusuario,socket);
        System.out.println("test "+nombreusuario);
        if(usuario != null){
            out.writeUTF("Usuario: '"+usuario.getNickName()+"' registrado con exito");
            System.out.println("Usuario: '"+usuario.getNickName()+"' registrado con exito");
            return true;
        }
        else{
            out.writeUTF("ERROR: Usuario: '"+nombreusuario.toLowerCase()+"' Ya está en linea");
            return false;
        }
    }

    private void interfazlogeada() throws IOException {
        boolean salir = false;
        String inputCrudo;
        String[] argumentos;

        while (!salir){
            inputCrudo = in.readUTF();
            argumentos = inputCrudo.split(" ");

            switch ( argumentos[0] ){
                case "msjbroadcast":
                    String mensaje = quitarArgumetos(argumentos,1);
                    List<Usuario> lista = controladorUsuario.getUsuariosEnLinea();
                    for(Usuario usuarioDestino : lista)
                        usuarioDestino.mandarMensaje(this.usuario.getNickName(),mensaje);
                    break;
                case "exit":
                    usuario.cerrarSesion();
                    salir = true;
                    break;
                default:
                    out.writeUTF("ERROR: '" + argumentos[0] + "' no se reconoce como un comando");
            }
        }
    }

    private String quitarArgumetos(String[] argumentos, int argumentosAQuitar){
        String resultado = "";
        if(argumentosAQuitar < argumentos.length)
            for (int i = argumentosAQuitar; i < argumentos.length; i++) {
                resultado += argumentos[i] + " ";
            }
        else resultado = null;
        return resultado;
    }
}
