package Application;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;

import Library.*;
//import com.sun.security.ntlm.Server;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;



public class Communications{
    /*
     * Open communication between two IP adresses
     * Creates thread for each recieved message and closes it once the respons is sent
     * */
    public String receiveCom(int socketNumber)
    {

        // Create client socket
        String message = "";
        try {
            System.out.print("Nu börjar vi lyssna efter en kommunikation");
            Socket s = new ServerSocket(socketNumber).accept();

        // to read data coming from the server
        BufferedReader dataIn
                = new BufferedReader(
                new InputStreamReader(
                        s.getInputStream()));
        message = dataIn.readLine();
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
            System.out.print("Nu försöker vi fixa en kommunikation");
            Socket s = new Socket(ip, portNumber);

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

