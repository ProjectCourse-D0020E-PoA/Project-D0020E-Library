package Application;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;

import Library.*;
import com.sun.security.ntlm.Server;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Communications{
    /*
     * Open communication between two IP adresses
     * Creates thread for each recieved message and closes it once the respons is sent
     * */
    public String receiveCom(int socketNumber)
    {

        // Create client socket
        String str = "";
        try {
            Socket s = new ServerSocket(socketNumber).accept();

        // to read data coming from the server
        BufferedReader dataIn
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));
        str = dataIn.readLine();
        dataIn.close();
        s.close();

        }catch(Exception e){
            System.out.print("receiveCom Error: " + e);
            System.exit(0);
        }
        return(str);

    }

    public void transmitCom(String jwt, String ip, int portNumber){





        try {
            Socket s = new Socket(ip, portNumber);

            // to send data to agent
            DataOutputStream dataOut
                    = new DataOutputStream(
                    s.getOutputStream());

            dataOut.writeBytes("NaNaNa med movitz");
            dataOut.close();
            s.close();
        }catch (Exception e){
            System.out.print("transmitCom Error: " + e);
            System.exit(0);
        }
    }
}

