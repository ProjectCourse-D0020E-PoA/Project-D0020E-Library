package Application;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;

import Library.*;
import com.sun.security.ntlm.Server;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Agent {
    private Communications com;

    public Agent(String agentName,
                 int agentID,
                 String agentIP,
                 String agentPublicKey,
                 String agentPrivetKey){

        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.agentPublicKey = agentPublicKey;
        this.agentPrivetKey = agentPrivetKey;
        this.com = new Communications();
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


    // Send to recipient &  Pass on to next agent (if trasferable) & Send NEW PoA with requested time from agent
    public void sendPoA(PoA poa,
                        String ip,
                        String publicKey,
                        int portNumber){

        this.com.transmitCom(poa, ip, publicKey, portNumber);
    };

    public String recivePoA(String ip, int socketNumber){
        String message = this.com.receiveCom(ip, socketNumber);

        PoA poa = PoAGen.transfer(message,principalKeypair.getPublic());
        if(poa.getTransferable() > 0){
            poa
                .setAgentName("agent2")
                .setAgentPublicKey(KeyEncDec.stringEncodedKey(agent2Keypair.getPublic()));
        }   // inte klart är bara copy pasta



        //pakaupp
        //kolla transmitt eller validera


        return();

    }

    public void requestNewTime(String PrincipalKey, String principalIP, Date newExpiredAt){
        // save this for later when we know more
    };

    public boolean validatePoA(String token, Key key){
        return PoAValid.validate(token, key);
    }

}