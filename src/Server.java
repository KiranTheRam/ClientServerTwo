import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static List<ClientHandler> clients = new ArrayList<>();
    //    Setting max number of clients or threads to 5
    private static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4999);
        Date date;

//        Loop to keep server running
        while (!serverSocket.isClosed()) {
            date = new Date();
            System.out.println(new Timestamp(date.getTime()) + " [Server] Server running, waiting for a client to connect");
//            Accepting client request to connect. Program is halted here until a client connects
            Socket socket = serverSocket.accept();
            date = new Date();
            System.out.println(new Timestamp(date.getTime()) + " [Server] Client has connected");
//            Using ClientHandler to spawn a new thread for each client that connects (allowing multiple connections)
            ClientHandler clientThread = new ClientHandler(socket);
//            Add each new client to the array list and use the clientHandler class to preform work
            clients.add(clientThread);
            pool.execute(clientThread);
        }

    }
}
