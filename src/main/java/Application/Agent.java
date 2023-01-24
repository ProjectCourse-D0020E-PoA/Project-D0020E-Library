package Application;

import Library.*;
import java.util.Date;
import java.security.Key;

public class Agent {
    public Agent(String agentName, int agentID, String agentIP, String agentPublicKey){
        this.agentName = agentName;
        this.agentID = agentID;
        this.agentIP = agentIP;
        this.agentPublicKey = agentPublicKey;
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
    public sendPoA(){

    };

    // Request NEW expiration date for PoA
    public requestNewTime(String PrincipalKey, String principalIP, Date newExpiredAt){
        // save this for later when we know
    };

    public boolean validatePoA(String token, Key key){
        return PoAValid.validate(token, key);
    }

}