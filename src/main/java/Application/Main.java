package Application;

import java.io.*;
import java.net.*;
import java.security.Key;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;

import Library.*;
//import com.sun.security.ntlm.Server;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Main{
    public static void main(String[] args){

        KeyPair principalKeypair    = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent1Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);

        Agent principal = new Agent("principal",
                                    0,
                                    "localhost",
                                    principalKeypair);
        Agent agent1 = new Agent("agent1",
                                 1,
                                 "localhost",
                                 agent1Keypair);
        try {
            agent1.start();
        }catch (Exception e){
            System.out.print("Bingo Bango bongo det funka inte" + e);
            System.exit(0);
        }
        String[] metadata = {};
        Date date =  new Date(System.currentTimeMillis()+ Days(5));

        PoA poa = principal.setValues(0,
                            0,
                            KeyEncDec.stringEncodedKey(principalKeypair.getPublic()),
                            "principal",
                            KeyEncDec.stringEncodedKey(agent1Keypair.getPublic()),
                            "agent1",
                            date,
                            metadata);
        agent1.recivePoA(888, principalKeypair.getPublic());
        principal.sendPoA(poa, "localhost", agent1Keypair.getPublic(), 888);
        System.out.print("eyy yo det kanske funnkar");
    }

    private static long Days(int i) {
        return i * 86400000;
    }
}
