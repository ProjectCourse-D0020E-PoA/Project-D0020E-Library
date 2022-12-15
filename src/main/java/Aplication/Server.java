package Aplication;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;

import Library.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class Server{

    public static void main(String args[])
            throws Exception
    {

        // Create server Socket
        ServerSocket ss = new ServerSocket(888);

        // connect it to client socket
        Socket s = ss.accept();
        System.out.println("Connection established");

        // to send data to the client
        PrintStream ps
                = new PrintStream(s.getOutputStream());

        // to read data coming from the client
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        // to read data from the keyboard
        BufferedReader kb
                = new BufferedReader(
                new InputStreamReader(System.in));

        ArrayList<String> lst = new ArrayList<String>();
        int i = 0;
        String str;

        /** Own Key*/
        KeyPair principalKeypair    = Keys.keyPairFor(SignatureAlgorithm.RS256);

        PoA poa = PoAGen.generateDefault();
        lst.add("PoA-Pkey---" + poa.exportJWT(principalKeypair.getPrivate()) + "---" + KeyEncDec.stringEncodedKey(principalKeypair.getPublic()));


        /** Agent Key*/
        Key agent1public = null;
        // repeat as long as the client
        // does not send a null string

        // read from client
        while ((str = br.readLine()) != null) {

            System.out.println("Fått från Client: " + str);
            String[] varibabel = str.split("---",3);
            switch(varibabel[0]){
                case "PKey":
                    agent1public = KeyEncDec.decodeKeyBytesPublic(varibabel[1]);
                    ps.println(lst.get(i));
                    System.out.println("Skickar PoA|Pkey till client: " + lst.get(i));
                    i++;
                    break;
                case "PoAR":doStuffPoA();
                    break;
                case "PoAT":doStuffPoAT();
                    break;
            }
            /**
            // send to client
            ps.println(lst.get(i));
            System.out.println("Skickar till client: " + lst.get(i));
            i++;*/
        }

        // close connection
        ps.close();
        br.close();
        kb.close();
        ss.close();
        s.close();
        System.out.println("Exiting");
        // terminate application

    }

    private static void doStuffPoAT() {
    }

    private static void doStuffPoA() {
    }
}