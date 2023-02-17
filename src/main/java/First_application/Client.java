package First_application;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;

import Library.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

class Client{

    public static void main(String args[])
            throws Exception
    {

        // Create client socket
        Socket s = new Socket("localhost", 888);

        // to send data to the server
        DataOutputStream dos
                = new DataOutputStream(
                s.getOutputStream());

        // to read data coming from the server
        BufferedReader br
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));

        // to read data from the keyboard
        BufferedReader kb
                = new BufferedReader(
                new InputStreamReader(System.in));
        String str;
        ArrayList<String> lst = new ArrayList<String>();

        KeyPair agent1Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);

        lst.add("PKey---"+ KeyEncodeDecode.stringEncodedKey(agent1Keypair.getPublic()));
        lst.add("PoAR");

        Key principlePubKey = null;
        // repeat as long as exit
        // is not typed at client
        int i = 0;
        while (!(lst.get(i) == null)) {

            // send to the server
            dos.writeBytes(lst.get(i) + "\n");
            System.out.println("Skickar till server: " + lst.get(i));

            // receive from the server
            str = br.readLine();
            System.out.println("Fått från server: " + str);
            i++;
            String[] varibabel = str.split("---");
            switch(varibabel[0]){
                case "PoA-Pkey":
                    principlePubKey = KeyEncodeDecode.decodeKeyBytesPublic(varibabel[2]);
                    PoA poa = PoAGen.reconstruct(varibabel[1], principlePubKey);
                    System.out.println("valid?:" + PoAValid.validate(varibabel[1],principlePubKey));
                    break;
                case "PoAT":
                    //doStuffPoAT();
                    break;
            }

        }

        // close connection.
        dos.close();
        br.close();
        kb.close();
        s.close();
    }
}