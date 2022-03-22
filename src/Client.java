import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4999);
        Scanner scanner = new Scanner(System.in);
        Date date = new Date();

//        In a loop so multiple message can be sent to the server
        while (socket.isConnected()) {
            System.out.println("\nWhat would you like to tell the server? (exit to end)");
            String messageToBeSent = scanner.nextLine();

//            Send the message captured with scanner
            PrintWriter sending = new PrintWriter(socket.getOutputStream(), true);
            sending.println(messageToBeSent);
//            sending.flush();

//            Receive message from server
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String message = bufferedReader.readLine();

//            Handling the closing of the socket
            System.out.println(new Timestamp(date.getTime()) + " " + message);
            if (message.equals("[Server] Socket closed")) {
                System.exit(1);
            }

//            if (message.equals("[Server] Socket closed")) {
//                System.out.println(new Timestamp(date.getTime()) + " " + message);
//                System.exit(1);
//            }
//            else {
//                System.out.println(new Timestamp(date.getTime()) + " " + message);
//            }
        }
    }
}
