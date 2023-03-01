package Application.TestCases;

import java.security.KeyPair;

import Application.Agent;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class IncorrectKeysTest {

    public static void main(String[] args) throws InterruptedException {

        // Generate test keys
        KeyPair testKeyPair0    = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair testKeyPair1    = Keys.keyPairFor(SignatureAlgorithm.RS256);

        // Set keys manually instead of fetching from database
        Agent agent0 = new Agent(
                "agent0",
                0,
                "localhost",
                0,
                testKeyPair0.getPublic(),
                testKeyPair0.getPrivate());

        Agent agent1 = new Agent(
                "agent1",
                1,
                "localhost",
                1,
                testKeyPair1.getPublic(),
                testKeyPair1.getPrivate());
        try {
            agent1.start();
        }catch (Exception e){
            System.out.println("Error when starting agent1" + e);
            System.exit(0);
        }
    }

}
