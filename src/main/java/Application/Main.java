package Application;

import java.security.KeyPair;
import java.util.Date;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import Library.*;
import Database.*;


public class Main extends Thread{
    public static void main(String[] args) throws InterruptedException {
        // generating keys for the agent and agent0
        KeyPair agent0Keypair    = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent1Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent2Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent3Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);


        Setters.InsertNew("agent0",
                KeyEncodeDecode.stringEncodedKey(agent0Keypair.getPrivate()),
                KeyEncodeDecode.stringEncodedKey(agent0Keypair.getPublic()),
                "localhost",
                888);
        Setters.InsertNew("agent1",
                KeyEncodeDecode.stringEncodedKey(agent1Keypair.getPrivate()),
                KeyEncodeDecode.stringEncodedKey(agent1Keypair.getPublic()),
                "localhost",
                889);
        Setters.InsertNew("agent2",
                KeyEncodeDecode.stringEncodedKey(agent2Keypair.getPrivate()),
                KeyEncodeDecode.stringEncodedKey(agent2Keypair.getPublic()),
                "localhost",
                890);
        Setters.InsertNew("agent3",
                KeyEncodeDecode.stringEncodedKey(agent3Keypair.getPrivate()),
                KeyEncodeDecode.stringEncodedKey(agent3Keypair.getPublic()),
                "localhost",
                891);

        Getters.getPriv("agent0");

    }
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {
        return i * 86400000;
    }
}
