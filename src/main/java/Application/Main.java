package Application;

import java.security.KeyPair;
import java.util.Date;
import Library.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


public class Main{
    public static void main(String[] args){

        // generating keys for the agent and principal
        KeyPair principalKeypair    = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent1Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);

        // creating instances of principal and agent
        Agent principal = new Agent("principal",
                                    0,
                                    "localhost",
                                    principalKeypair, agent1Keypair);
        Agent agent1 = new Agent("agent1",
                                 1,
                                 "localhost",
                                 agent1Keypair, principalKeypair); //Sending the same keypair because there is no more transactions
        try {
            agent1.start();
        }catch (Exception e){
            System.out.println("Error when starting agent1: (\"bingo bango det funkar inte\") " + e);
            System.exit(0);
        }

        String[] metadata = {};
        Date date =  new Date(System.currentTimeMillis()+ Days(5));

        // Setting valus for the PoA first handed out by the principal
        PoA poa = principal.setValues(0,
                            0,
                            KeyEncDec.stringEncodedKey(principalKeypair.getPublic()),
                            "principal",
                            KeyEncDec.stringEncodedKey(agent1Keypair.getPublic()),
                            "agent1",
                            date,
                            metadata);

        // Send the PoA from the principal
        principal.sendPoA(poa, "localhost", agent1Keypair.getPublic(), 888);

        // We end by validating the PoA before terminating the thread
        System.out.println("Result check: ");
    }
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {
        return i * 86400000;
    }
}
