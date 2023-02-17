package Application;

import java.security.*;
import java.util.Date;
import Library.*;
import java.security.Key;
public class Agent  extends Thread{
    private final String agentName;
    private final int agentID;
    private final String agentIP;
    private final KeyPair agentKeyPair;
    private final Key nextAgentPubKey;
    private Communications com;

    public Agent(String agentName,
                 int agentID,
                 String agentIP,
                 KeyPair agentKeyPair,
                 Key nextAgentPubKey){

        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.agentKeyPair = agentKeyPair;
        this.nextAgentPubKey = nextAgentPubKey;
        this.com = new Communications();
    }

    // Generate and set the values of the requested PoA
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

    // Receive a poa, reconstruct to be able to send it again, decrement transferable and validate
    public PoA recivePoA(int socketNumber, Key principalPublicKey){
        String message = this.com.receiveCom(socketNumber);
        PoA poa = PoAGen.reconstruct(message,principalPublicKey);

        if(poa.getTransferable() > 0){
            poa.setTransferable(poa.getTransferable()-1);
        }
        System.out.println( "Result from agent validating the PoA:\n" + validatePoA(message, principalPublicKey));
        return(poa);
    }

    // Send to recipient
    public void sendPoA(PoA poa,
                        String ip,
                        Key publicKey,
                        int portNumber){

        // Converts the PoA to a JasonWebToken
        String jwt = poa.exportJWT(agentKeyPair.getPrivate());
        // Sends the token to the destination specified by ip and portnumber
        this.com.transmitCom(jwt, ip, portNumber);
    };

    // Uses the validate method from the library for the PoA
    public boolean validatePoA(String token, Key key){
        return PoAValid.validate(token, key);
    }

    // When main runs .start on an object, this function is invoked
    public void run(){
        recivePoA(888, nextAgentPubKey);
    }

    // Should be implemented later to enable the end-of-the-line-agent to request a new expiration date for the PoA
    public void requestNewTime(String PrincipalKey, String principalIP, Date newExpiredAt){ };
}

