package servidor;

import servidor.controladores.ControladorGrupos;
import servidor.controladores.ControladorUsuarios;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService extends Thread {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private ControladorUsuarios controladorUsuarios;
    private ControladorGrupos controladorGrupos;

    public NetworkService(int port, int poolSize, ControladorUsuarios controladorUsuarios, ControladorGrupos controladorGrupos) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newFixedThreadPool(poolSize);
        this.controladorUsuarios = controladorUsuarios;
        this.controladorGrupos = controladorGrupos;
    }

    public void run() { // run the service
        try {
            for (;;) {
                pool.execute(new Handler(serverSocket.accept(), controladorUsuarios, controladorGrupos));
            }
        } catch (IOException ex) {
            pool.shutdown();
        }
    }
}
