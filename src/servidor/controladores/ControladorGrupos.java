package servidor.controladores;

import servidor.modelos.Grupo;
import servidor.modelos.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ControladorGrupos {
    private List<Grupo> listaGrupos;

    public ControladorGrupos(){
        listaGrupos = new ArrayList<>();
    }

    private boolean nombreValido(String nombre){
        return nombre != ""
                && nombre != null
                && nombre != "exit";
    }

    public synchronized boolean crearGrupo(String nombre){
        if(nombreValido(nombre) && !existeGrupo(nombre)) {
            listaGrupos.add(new Grupo(nombre));
            return true;
        }
        else
            return false;
    }

    public synchronized Grupo buscarPorNombre(String nombre){
        Grupo encontrado = null;
        int i = 0;
        while(encontrado == null && i < listaGrupos.size()){
            if(listaGrupos.get(i).mismoNombre(nombre))
                encontrado = listaGrupos.get(i);
            i++;
        }
        return encontrado;
    }

    public boolean existeGrupo(String nombre){
        return buscarPorNombre(nombre) != null;
    }
}
