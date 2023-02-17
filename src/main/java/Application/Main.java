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

        Setters.InsertNew("agent0", agent0Keypair.getPrivate(), agent0Keypair.getPublic(), "localhost", 888);
        Setters.InsertNew("Agent1", agent1Keypair.getPrivate(), agent1Keypair.getPublic(), "localhost", 888);



    }
    // Creating the correct "long" value for "i" days (86400000 = number of ms for a day)
    private static long Days(int i) {
        return i * 86400000;
    }
}
