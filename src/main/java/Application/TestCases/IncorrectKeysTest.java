package Application.TestCases;

import java.security.KeyPair;
import java.util.Date;

import Application.Agent;
import Database.Setters;
import Library.KeyEncodeDecode;
import Library.PoA;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class IncorrectKeysTest {

    public static void main(String[] args) throws InterruptedException {

        // Generate test keys

        KeyPair agent6Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent7Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);


        Setters.UpdateKeys("agent6",
                KeyEncodeDecode.stringEncodedKey(agent6Keypair.getPrivate()),
                KeyEncodeDecode.stringEncodedKey(agent6Keypair.getPublic()));

        Setters.UpdateKeys("agent7",
                KeyEncodeDecode.stringEncodedKey(agent7Keypair.getPrivate()),
                KeyEncodeDecode.stringEncodedKey(agent7Keypair.getPublic()));
        // Giving agent6 the keys for agent7 and vice versa
        Agent agent6 = new Agent(
                "agent6",
                0,
                "localhost",
                0,
                agent7Keypair.getPublic(),
                agent7Keypair.getPrivate());

        Agent agent7 = new Agent(
                "agent7",
                1,
                "localhost",
                1,
                agent6Keypair.getPublic(),
                agent6Keypair.getPrivate());
        try {
            agent7.start();
        }catch (Exception e){
            System.out.println("Error when starting agent7" + e);
            System.exit(0);
        }

        String[] metadata = {};
        Date date =  new Date(System.currentTimeMillis()+ Days(1));

        PoA poa = agent6.setValues(
                0,
                1,
                "agent7",
                date,
                metadata);

        // Send the PoA from the agent0
        agent6.sendPoA(poa, "localhost", 889);
    }

    private static long Days(int i) {
        return i * 86400000;
    }
}
