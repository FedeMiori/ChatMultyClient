package servidor.modelos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Usuario {
    private String nickName;
    private Socket socket;
    private boolean online;

    public Usuario(String nickName) {this.nickName = nickName.split(" ")[0].toLowerCase();}

    public Socket getSocket() {return socket;}

    public boolean isOnline() {return online;}

    public void setOnline(boolean online) {this.online = online;}

    public void setSocket(Socket socket) {this.socket = socket;}

    public String getNickName() {return nickName;}

    public boolean mismoNombre(String nombre){
        return this.nickName.equalsIgnoreCase(nombre);
    }

    public synchronized void mandarMensaje(String autor, String mensaje) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        if(autor == null)
            out.writeUTF(mensaje);
        else
            out.writeUTF(autor + ":" +mensaje);
    }

    public void iniciarSesion(Socket socket) {
        this.socket = socket;
        online = true;
    }

    public void cerrarSesion(){
        socket = null;
        online = false;
    }
}
