package Application;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Date;

import Library.*;
//import com.sun.security.ntlm.Server;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Agent  extends Thread{
    private final String agentName;
    private final int agentID;
    private final String agentIP;
    private final KeyPair agentKeyPair;
    private Communications com;

    public Agent(String agentName,
                 int agentID,
                 String agentIP,
                 KeyPair agentKeyPair){

        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.agentKeyPair = agentKeyPair;
        this.com = new Communications(); // Malkolm idé om ip och port grejs
    }

    // Set values of PoA (Transferable, public key, time, etc) & Send NEW PoA with requested time from agent
    public PoA setValues(int recourceOwnerID,
                         int transferable,
                         String pricipalPublicKey,
                         String principalName,
                         String agentKey,
                         String agentName,
                         Date expiredAt,
                         String[] metaData){

        PoA poa = PoAGen.generate(recourceOwnerID, transferable, pricipalPublicKey, principalName, agentKey, agentName, expiredAt, metaData);
        return poa;
    };

    public PoA recivePoA(int socketNumber, Key principalPublicKey){   // ? är det så här vi skickar med public keys?
        String message = this.com.receiveCom(socketNumber);
        //validatePoA(message, principalPublicKey); //Behövs nog inte om vi ska skicka vidare
        PoA poa = PoAGen.reconstruct(message,principalPublicKey);

        if(poa.getTransferable() > 0){
            poa.setTransferable(poa.getTransferable()-1);
        }

        return(poa);

    }

    // Send to recipient &  Pass on to next agent (if transferable) & Send NEW PoA with requested time from agent
    public void sendPoA(PoA poa,
                        String ip,
                        Key publicKey,
                        int portNumber){


        String jwt = poa.exportJWT(agentKeyPair.getPrivate());

        this.com.transmitCom(jwt, ip, portNumber);
    };

    public void requestNewTime(String PrincipalKey, String principalIP, Date newExpiredAt){
        // save this for later when we know more
    };

    public boolean validatePoA(String token, Key key){
        return PoAValid.validate(token, key);
    }

    public void run(){

    }
}

