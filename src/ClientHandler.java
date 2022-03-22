import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class ClientHandler implements Runnable {
    private Socket client;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    Date date;


    public ClientHandler(Socket client) throws IOException {
        this.client = client;
        this.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.printWriter = new PrintWriter(client.getOutputStream());
    }

    public void CloseEverything() {
//            If a client disconnects, close all following objects
        try {
            bufferedReader.close();
            printWriter.close();
            client.close();
        } catch (IOException e) {
            System.err.println("Error trying to CloseEverything");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (client.isConnected()) {
//         Set all objects to null initially
            InputStreamReader inputStreamReader = null;
            String message = null;
            PrintWriter printWriter = null;

//            Try to set up inputStreamReader, which converts bytes to characters
            try {
                inputStreamReader = new InputStreamReader(client.getInputStream());
            } catch (IOException e) {
                System.err.println("IO Exception in ClientHandler: InputStreamReader");
                e.printStackTrace();
            }

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

//        Put message contents into a string variable and display it
            try {
                message = bufferedReader.readLine();
//                If the client sent an exit message we must prompt the server, client, and close everything
                if (message.equals("exit")) {
                    date = new Date();
                    System.out.println(new Timestamp(date.getTime()) + " [Server] Connection to client closed");
                    printWriter = new PrintWriter(client.getOutputStream(), true);
                    printWriter.println("[Server] Socket closed");
                    CloseEverything();
                    break;
                } else {
//        Sending a message back to the client
                    printWriter = new PrintWriter(client.getOutputStream(), true);
                    printWriter.println("[Server] Your message was received!");
                }
            } catch (IOException e) {
                System.err.println("IO Exception in ClientHandler: BufferedReader.readLine");
                e.printStackTrace();
            }

//            Displays client's message in the server
            date = new Date();
            System.out.println(new Timestamp(date.getTime()) + " [Client]: " + message);

        }
    }
}
