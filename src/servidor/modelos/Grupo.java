package servidor.modelos;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Grupo {
    private String nombre;
    List<Usuario> listaUsuarios;

    public Grupo(String nombre){
        this.nombre = nombre.split(" ")[0];
        this.listaUsuarios = new LinkedList<>();
    }

    public synchronized String getNombre() {return nombre;}

    public synchronized boolean mandarMensajeAlGrupo(String autor, String mensaje) throws IOException {
        if(estaEnElGrupo(autor)){
            for (Usuario usuario : listaUsuarios)
                if(usuario.isOnline())
                    usuario.mandarMensaje("Grupo:"+nombre+" Autor:"+autor, mensaje);
            return true;
        }else
            return false;
    }

    public synchronized boolean mandarMensajeAlGrupo(String mensaje) {
        try {
            for (Usuario usuario : listaUsuarios)
                if(usuario.isOnline())
                    usuario.mandarMensaje("Grupo:"+nombre, mensaje);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public synchronized void aniadirUsuarioAlGrupo(Usuario user){
        if(!estaEnElGrupo(user)) {
            listaUsuarios.add(user);
            mandarMensajeAlGrupo("'"+user.getNickName()+"' se ha unido al grupo");
        }
    }

    public synchronized boolean eliminarUsuario(Usuario user){
        boolean eliminado = false;
        Iterator<Usuario> it = listaUsuarios.iterator();
        while(!eliminado && it.hasNext())
            if(user.equals(it.next())) {
                it.remove();
                mandarMensajeAlGrupo("'"+user.getNickName()+"' se salido del grupo");
                eliminado = true;
            }
        return eliminado;
    }

    public synchronized boolean estaEnElGrupo(Usuario user){
        boolean esta = false;
        Iterator<Usuario> it = listaUsuarios.iterator();
        while(it.hasNext() && !esta){
            if(user.equals( it.next() ))
                esta = true;
        }
        return esta;
    }


    public synchronized boolean estaEnElGrupo(String nombre){
        boolean esta = false;
        Iterator<Usuario> it = listaUsuarios.iterator();
        while(it.hasNext() && !esta){
            if(it.next().mismoNombre(nombre))
                esta = true;
        }
        return esta;
    }

    public boolean mismoNombre(String nombre){
        return this.nombre.equalsIgnoreCase(nombre);
    }
}
