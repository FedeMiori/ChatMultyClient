package servidor;

import servidor.controladores.ControladorGrupos;
import servidor.controladores.ControladorUsuarios;
import servidor.modelos.Grupo;
import servidor.modelos.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

class Handler implements Runnable {
    private final Socket socket;
    DataInputStream in;
    DataOutputStream out;
    ControladorGrupos controladorGrupos;
    ControladorUsuarios controladorUsuarios;
    Usuario usuario;

    Handler(Socket socket) {
        this.socket = socket;
        this.controladorUsuarios = ControladorUsuarios.getInstance();
        this.controladorGrupos = ControladorGrupos.getInstance();
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
            System.out.println("Cliente: '" +getAutor()+ "' desconectado");
        } catch (Exception e) {
            System.out.println("Cliente: '" +getAutor()+ "' desconectado de forma inesperada");
            //throw new RuntimeException(e);
        }finally {
            if (usuario != null)
                usuario.cerrarSesion();
        }
    }

    private boolean login() throws IOException {
        out.writeUTF("Inserte su nombre de usuario para iniciar sesion");
        String nombreusuario = in.readUTF();
        this.usuario = controladorUsuarios.logearUsuario(nombreusuario,socket);
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
        String inputCrudo, mensaje;
        String[] argumentos;

        while (!salir){
            inputCrudo = in.readUTF();
            argumentos = inputCrudo.split(" ");

            switch ( argumentos[0] ){
                case "msgbroadcast":
                    mensaje = quitarArgumetos(argumentos,1);
                    List<Usuario> lista = controladorUsuarios.getUsuariosEnLinea();
                    for(Usuario usuarioDestino : lista)
                        usuarioDestino.mandarMensaje(getAutor(),mensaje);
                    break;

                case "msg":
                    if(argumentos.length >= 2){
                        Usuario user = controladorUsuarios.getUsuarioEnLinea(argumentos[1]);
                        mensaje = quitarArgumetos(argumentos,2);
                        user.mandarMensaje( getAutor(), mensaje );
                        //out.writeUTF("Mensaje: "+mensaje+" Enviado a: "+user.getNickName());
                    }
                    else
                        out.writeUTF("ERROR: Faltan argumentos" +
                                "\n sintaxis: msg [usuario_destino] [mensaje]");
                    break;

                case "msgchannel":
                    if(argumentos.length >= 2){
                        Grupo grupo = controladorGrupos.buscarPorNombre( argumentos[1] );
                        mensaje = quitarArgumetos(argumentos,2);
                        grupo.mandarMensajeAlGrupo(getAutor(), mensaje);
                    }
                    else
                        out.writeUTF("ERROR: Faltan argumentos" +
                                "\n sintaxis: msgchannel [nombre_canal] [mensaje]");
                    break;

                case "createchannel":
                    if(argumentos.length >= 2){
                        String posibleNombre = argumentos[1];
                        if(controladorGrupos.existeGrupo(posibleNombre))
                            out.writeUTF("El grupo '"+posibleNombre+"' ya existe");
                        else {
                            controladorGrupos.crearGrupo(posibleNombre);
                            out.writeUTF("Grupo '"+posibleNombre+"' creado con exito");
                        }
                    }
                    else
                        out.writeUTF("ERROR: Faltan argumentos" +
                            "\n sintaxis: createchannel [nombre_canal]");
                    break;

                case "joinchannel":
                    if(argumentos.length >= 2){
                        Grupo grupo = controladorGrupos.buscarPorNombre( argumentos[1] );
                        if(grupo != null){
                            if(!grupo.estaEnElGrupo(usuario)) {
                                grupo.aniadirUsuarioAlGrupo(usuario);
                                out.writeUTF("Te has unido al grupo '" + grupo.getNombre() + "'");
                            }
                            else
                                out.writeUTF("ERROR: El usuario ya estaba en ese grupo");
                        }
                        else
                            out.writeUTF("ERROR: El grupo '"+argumentos[1]+"' no existe");

                    }
                    else
                        out.writeUTF("ERROR: Faltan argumentos" +
                                "\n sintaxis: joinchannel [canal]");
                    break;

                case "exitchannel":
                    if(argumentos.length >= 2){
                        Grupo grupo = controladorGrupos.buscarPorNombre( argumentos[1] );
                        if(grupo.eliminarUsuario(usuario))
                            out.writeUTF("Te has salido del grupo");
                        else
                            out.writeUTF("ERROR: ya estabas fuera de ese grupo");
                    }
                    else
                        out.writeUTF("ERROR: Faltan argumentos" +
                                "\n sintaxis: exitchannel [canal]");
                    break;

                case "help":
                    out.writeUTF("Ayuda Comandos:\n" +
                            "\nmsgbroadcast [mensaje]\n" +
                            "   El mensaje será recibido por todos los clientes conectados al\n" +
                            "   servidor\n" +
                            "\nmsg [usuario_destino] [mensaje]\n" +
                            "   El mensaje es privado y sólo lo recibirá el usuario destino\n" +
                            "\nmsgchannel [nombre_canal] [mensaje]\n" +
                            "   El mensaje llegará a todos los miembros de un un canal\n" +
                            "\ncreatechannel [nombre_canal]\n" +
                            "   Crea un nuevo grupo con el nombre indicado\n" +
                            "\njoinchannel [canal]\n" +
                            "   Une el cliente al canal indicado\n" +
                            "\nexitchannel [canal]\n" +
                            "   Elimina al cliente del canal indicado\n" +
                            "\nexit\n" +
                            "   El cliente se desconecta del servidor");
                    break;

                case "":
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

    private String getAutor(){
        if(usuario != null)
            return usuario.getNickName();
        else
            return "anonimo";
    }
}
