package servidor.controladores;

import servidor.modelos.Grupo;

import java.util.ArrayList;
import java.util.List;

public class ControladorGrupos {
    private static ControladorGrupos instancia;

    private List<Grupo> listaGrupos;

    private ControladorGrupos(){
        listaGrupos = new ArrayList<>();
    }

    private boolean nombreValido(String nombre){
        return !nombre.isEmpty() && !nombre.equals("exit");
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

    public static ControladorGrupos getInstance(){
        if(instancia == null)
            instancia = new ControladorGrupos();
        return instancia;
    }
}
