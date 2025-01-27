package servidor;

import servidor.controladores.ControladorUsuario;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkService extends Thread {
    private final ServerSocket serverSocket;
    private final ExecutorService pool;
    private ControladorUsuario controladorUsuario;

    public NetworkService(int port, int poolSize, ControladorUsuario controladorUsuario) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newFixedThreadPool(poolSize);
        this.controladorUsuario = controladorUsuario;
    }

    public void run() { // run the service
        try {
            for (;;) {
                pool.execute(new Handler(serverSocket.accept(),controladorUsuario));
            }
        } catch (IOException ex) {
            pool.shutdown();
        }
    }
}
