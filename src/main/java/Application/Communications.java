package Application;

import java.io.*;
import java.net.*;


public class Communications{
    /*
     * Open communication between two IP adresses
     * Creates thread for each recieved message and closes it once the respons is sent
     * */
    public String receiveCom(int socketNumber) {
        // Creating the client socket
        // Initialize message string
        String message = "";
        try {
            System.out.println("Starting listening for communication:\n");
            Socket s = new ServerSocket(socketNumber).accept();
            System.out.println("Connection established\n");
            // To read data coming from the server
            BufferedReader dataIn
                    = new BufferedReader(
                    new InputStreamReader(
                            s.getInputStream()));
            message = dataIn.readLine();
            System.out.println("Message recived\n");
            dataIn.close();
            s.close();

        }catch(Exception e){
            System.out.print("receiveCom Error: " + e);
            System.exit(0);
        }
        return(message);
    }

    public void transmitCom(String jwt, String ip, int portNumber){

        try {
            // Tries to connect to a socket with specified ip and portnumber
            System.out.println("transmitCom trying to establish a connection\n");
            Socket s = new Socket(ip, portNumber);
            System.out.println("Communication established\n");
            // to send data to agent
            DataOutputStream dataOut
                    = new DataOutputStream(
                    s.getOutputStream());

            dataOut.writeBytes(jwt);
            dataOut.close();
            s.close();
        }catch (Exception e){
            System.out.print("transmitCom Error: " + e);
            System.exit(0);
        }
    }
}

