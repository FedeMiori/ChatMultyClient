package servidor.modelos;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Usuario {
    private String nickName;
    private Socket socket;
    private boolean online;

    public Usuario(String nickName) {this.nickName = nickName.split(" ")[0].toLowerCase();}

    public boolean isOnline() {return online;}

    public synchronized String getNickName() {return nickName;}

    public boolean mismoNombre(String nombre){
        return this.nickName.equalsIgnoreCase(nombre);
    }

    public synchronized void mandarMensaje(String autor, String mensaje) throws IOException {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        if(autor == null)
            out.writeUTF(mensaje);
        else
            out.writeUTF(autor + ": " +mensaje);
    }

    public void iniciarSesion(Socket socket) {
        this.socket = socket;
        online = true;
    }

    public void cerrarSesion(){
        socket = null;
        online = false;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Usuario)
            return this.nickName.equalsIgnoreCase( ((Usuario) obj).getNickName() );
        else
            return false;
    }
}
