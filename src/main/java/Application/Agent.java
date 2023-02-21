package Application;

import java.util.Date;

import Database.Getters;
import Library.*;
import java.security.Key;
public class Agent  extends Thread{
    private final String agentName;
    private final int agentID;
    private final Key agentPrivateKey;
    private final Key agentPublicKey;
    private final String agentIP;
    private Communications com;

    public Agent(String agentName,
                 int agentID,
                 String agentIP){


        this.agentPrivateKey = Getters.getPriv(agentName);
        this.agentPublicKey = Getters.getPub(agentName);
        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.com = new Communications();
    }

    // Generate and set the values of the requested PoA
    public PoA setValues(int recourceOwnerID,
                         int transferable,
                         String nextAgentName,
                         Date expiredAt,
                         String[] metaData){

        PoA poa = PoAGen
                .generateDefault()
                .setResourceOwnerID(recourceOwnerID)
                .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(Getters.getPub(nextAgentName)))
                .setPrincipalName(this.agentName)
                .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(this.agentPublicKey))
                .setExpiredAt(expiredAt)
                .setTransferable(transferable)
                .setMetaData(metaData);
        return poa;
    };

    // Receive a poa, reconstruct to be able to send it again, decrement transferable and validate
    public PoA recivePoA(int socketNumber, Key previousAgentPubKey){
        String message = this.com.receiveCom(socketNumber);
        PoA poa = PoAGen.reconstruct(message, previousAgentPubKey);

        if(poa.getTransferable() > 0){
            PoA encapsulatedPoA = PoAGen.transfer(message, previousAgentPubKey);
            sendPoA(encapsulatedPoA, "localhost", 888);
        }
        System.out.println( "Result from " + this.agentName + " validating the PoA:\n" + validatePoA(message, previousAgentPubKey));
        return(poa);
    }

    // Send to recipient
    public void sendPoA(PoA poa,
                        String ip,
                        int portNumber){

        // Converts the PoA to a JasonWebToken
        String jwt = poa.exportJWT(agentPrivateKey);
        // Sends the token to the destination specified by ip and portnumber
        this.com.transmitCom(jwt, ip, portNumber);
    };

    // Uses the validate method from the library for the PoA
    public boolean validatePoA(String token, Key key){
        return PoAValid.validate(token, key);
    }

    // When main runs .start on an object, this function is invoked
    public void run(){
        System.out.println("Now " + this.agentName + " starts!\n");
        String prevAgentName = "agent" + (Integer.parseInt(this.agentName.substring(5, 6)) - 1);
        //System.out.println(prevAgentName);
        recivePoA(888, Getters.getPub(prevAgentName));
    }

    // Should be implemented later to enable the end-of-the-line-agent to request a new expiration date for the PoA
    public void requestNewTime(String agent0Key, String agent0IP, Date newExpiredAt){ };
}

