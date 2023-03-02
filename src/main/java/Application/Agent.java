package Application;

import java.util.Date;

import Database.Getters;
import Library.*;
import io.jsonwebtoken.Claims;
import java.security.Key;

public class Agent  extends Thread{
    private final String agentName;
    private final int agentID;
    private final Key agentPrivateKey;
    private final Key agentPublicKey;
    private final String agentIP;
    private Communications com;
    private final int lastAgent;

    public Agent(String agentName,
                 int agentID,
                 String agentIP, int lastAgent){


        this.agentPrivateKey = Getters.getPriv(agentName);
        this.agentPublicKey = Getters.getPub(agentName);
        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.com = new Communications();
        this.lastAgent = lastAgent;
    }
    public Agent(String agentName,
                 int agentID,
                 String agentIP, int lastAgent, Key agentPublicKey, Key agentPrivateKey){


        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.com = new Communications();
        this.lastAgent = lastAgent;
        this.agentPrivateKey = agentPrivateKey;
        this.agentPublicKey = agentPublicKey;
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
                .setTransferable(transferable)
                .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(this.agentPublicKey))
                .setPrincipalName(this.agentName)
                .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(Getters.getPub(nextAgentName)))
                .setAgentName(nextAgentName)
                .setExpiredAt(expiredAt)
                .setMetaData(metaData);
        return poa;
    };

    // Receive a poa, reconstruct to be able to send it again, decrement transferable and validate
    public PoA recivePoA(int socketNumber, Key previousAgentPubKey){
        //System.out.println(this.agentName + " calling reciveCom");
        String message = this.com.receiveCom(Integer.valueOf(Getters.getPort(this.agentName)));
        //System.out.println(this.agentName + " recived from reciveCom");
        PoA poa = PoAGen.reconstruct(message, previousAgentPubKey);
        print(poa);


        if(poa.getTransferable() > 1 && this.lastAgent == 0){
            PoA encapsulatedPoA = PoAGen.transfer(message, previousAgentPubKey);
            String nextAgent = "agent" + (Integer.parseInt(this.agentName.substring(5, 6)) + 1);
            encapsulatedPoA.setAgentName(nextAgent);
            encapsulatedPoA.setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(Getters.getPub(nextAgent)));
            sendPoA(encapsulatedPoA, this.agentIP, Integer.parseInt(Getters.getPort(nextAgent)));

        }
        System.out.println( "-Result from " + this.agentName + " validating the PoA: " + validatePoA(message, previousAgentPubKey));
        return(poa);
    }

    // Send to recipient
    public void sendPoA(PoA poa,
                        String ip,
                        int portNumber){

        // Converts the PoA to a JasonWebToken
        String jwt = poa.exportJWT(agentPrivateKey);
        // Sends the token to the destination specified by ip and portnumber
        //System.out.println(this.agentName + " Calling transmittCom");
        this.com.transmitCom(jwt, ip, portNumber);
        //System.out.println(this.agentName + " transmittCom finished");
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

    public void print(PoA poa){
        String resourceOwnerID = poa.getResourceOwnerID();
        String transferable = (poa.getTransferable() + "");
        String principalPublicKey = poa.getPrincipalPublicKey();
        String principalName = poa.getPrincipalName();
        String agentKey = poa.getAgentPublicKey();
        String agentName = poa.getAgentName();
        String issuedAt = poa.getIssuedAt();
        String expiredAt = poa.getExpiredAt();


        System.out.print("PoA: \n"
                + resourceOwnerID + "\n"
                + transferable + "\n"
                + principalPublicKey + "\n"
                + principalName + "\n"
                + agentKey + "\n"
                + agentName + "\n"
                + issuedAt + "\n"
                + expiredAt + "\n\n" );
    }
}

