package Application;

import java.io.*;
import java.net.*;

public class Communications extends Thread{
    /*
     * Open communication between two IP adresses
     * Creates thread for each recieved message and closes it once the respons is sent
     * */
    public void communication()
        throws Exception
    {

        int socket_number = 888;
        while(true){
            // Create client socket
            Socket s = new Socket(socket_number);
            Socket socket = s.accept();

            // to send data to the server
            DataOutputStream dataout
                    = new DataOutputStream(
                    socket.getOutputStream());

            // to read data coming from the server
            BufferedReader bufferread
                    = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            // to read data from the keyboard
            BufferedReader keyboard_read
                    = new BufferedReader(
                    new InputStreamReader(System.in));

            Thread thread = new Thread();
            start();
            socket_number++;
        }


        public void run(){
            funktion();
        }
    }

    private void funktion() {
        //handel PoA
    }



}

