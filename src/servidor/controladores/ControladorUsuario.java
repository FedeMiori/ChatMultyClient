package servidor.controladores;

import servidor.modelos.Usuario;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ControladorUsuario {
    ArrayList<Usuario> listaUsuarios;

    public ControladorUsuario(){
        listaUsuarios = new ArrayList<>();
    }

    public synchronized Usuario logearUsuario(String nombre, Socket socket){
        Usuario resultadoBusqueda = buscarPorNombre(nombre);
        Usuario usuarioLogeado;
        System.out.println(nombre + " buscado");
        if(resultadoBusqueda == null){
            usuarioLogeado = new Usuario(nombre);
            listaUsuarios.add(usuarioLogeado);
            usuarioLogeado.iniciarSesion(socket);
        }else{
            if(resultadoBusqueda.isOnline())
                usuarioLogeado = null;
            else{
                usuarioLogeado = resultadoBusqueda;
                usuarioLogeado.iniciarSesion(socket);
            }
        }
        return usuarioLogeado;
    }

    private Usuario buscarPorNombre(String nombre){
        Usuario encontrado = null;
        int i = 0;
        while(encontrado == null && i < listaUsuarios.size()){
            if(listaUsuarios.get(i).mismoNombre(nombre))
                encontrado = listaUsuarios.get(i);
            i++;
        }
        return encontrado;
    }

    public List<Usuario> getUsuariosEnLinea(){
        ArrayList<Usuario> usuariosEnLinea = new ArrayList<>();
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if(listaUsuarios.get(i).isOnline())
                usuariosEnLinea.add(listaUsuarios.get(i));
        }
        return usuariosEnLinea;
    }
}
