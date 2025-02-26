package Library;


import java.security.*;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import static Library.PoAValid.*;

public class Main {

    public static void main(String[] args){

        //3 pairs of keys to represent the 3 parts
        //this is based on the assumption everyone knows everyone's public keys and private keys are only known to yourself
        KeyPair principalKeypair    = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent1Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair agent2Keypair       = Keys.keyPairFor(SignatureAlgorithm.RS256);

        String encPrivKey = KeyEncodeDecode.stringEncodedKey(principalKeypair.getPrivate());
        PrivateKey decPrivKey = (PrivateKey) KeyEncodeDecode.decodeKeyBytesPrivate(encPrivKey);
        System.out.println(decPrivKey.equals(principalKeypair.getPrivate()));


        PoA principalPoa = PoAGen
                .generateDefault()
                .setTransferable(1)
                .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(principalKeypair.getPublic()))
                .setPrincipalName("bob")
                .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(agent1Keypair.getPublic()))
                .setAgentName("agent1");
        String transmitToken = principalPoa.exportJWT(principalKeypair.getPrivate());


        /** Agent 1 */
        //agent1 receives the JWT and transfers it to agent2
        String recivedToken = transmitToken; /* agent 1 receives the token */
        PoA principalPoaEncapsulated = PoAGen.transfer(recivedToken,principalKeypair.getPublic());
        principalPoaEncapsulated
                .setAgentName("agent2")
                .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(agent2Keypair.getPublic()));
        String transmitToken2 = principalPoaEncapsulated.exportJWT(agent1Keypair.getPrivate()); //agent 1 signs using their own key so that the chain of the Poa is verifiable


        /** Agent 2 */
        //agent 2 receives the transferred poa and verifies it recursively
        String reciveToken2 = transmitToken2;
        System.out.println("The transferred token is valid: " + validateRecursively(reciveToken2,agent1Keypair.getPublic()));
        PoA recon = PoAGen.reconstruct(reciveToken2, agent1Keypair.getPublic());


        //a bunch of basic examples using the "library"

        String[] bob ={"bob", "bob"};

        PoA testPoA = PoAGen.generate(
                1,
                2,
                "principalPublicKey",
                "principalName",
                "agentKey",
                "agentName",
                new Date(System.currentTimeMillis()+ Days(5)),
                bob);

        //sets everything to default values of 0 and "default"
        //the issuing time to current time
        //and Expiration time to current time + 7days
        PoA def = PoAGen.generateDefault()
                .setAgentPublicKey("AgentKey")
                .setExpiredAt(new Date(System.currentTimeMillis()+Days(5)))
                .setAgentName("AgentName")
                .setPrincipalPublicKey("ppk")
                .setPrincipalName("pn")
                .setTransferable(2)
                .setMetaData(bob);

        // Converting the data stored in the PoA to a JWT using principalKeypair
        String JWT = testPoA.exportJWT(principalKeypair.getPrivate());
        System.out.println("Json web token: " + JWT);


        // Decoding the JWT signed with principalKeypair using principalKeypair
        Jws<Claims> res = PoAValid.decodeJWT(JWT, principalKeypair.getPublic());
        System.out.println("Decoding with correct key example: " + res.getBody().values());

        // Decoding the JWT signed with principalKeypair using agent1Keypair.
        // This should throw the Invalid Error
        try {
            res = PoAValid.decodeJWT(JWT, agent1Keypair.getPublic());
            System.out.println(res.getBody().values());
        }catch(Error e){
            System.out.println("Decoding with wrong key example: " + e.getMessage());
        }

        //validating with the correct key
        System.out.println("valid example: " + PoAValid.validate(JWT,principalKeypair.getPublic()));

        //validating with the incorrect key
        System.out.println("invalid example: " + PoAValid.validate(JWT,agent1Keypair.getPublic()));

        //getting a specific claim out of the token
        System.out.println("getting claim Example: " + (int)res.getBody().get("iat"));

        //Reconstruction a PoA from the JWT
        String token = testPoA.exportJWT(principalKeypair.getPrivate());
        PoA reconstructedPoA = PoAGen.reconstruct(token,principalKeypair.getPublic());
        //signing the reconstructed PoA to enable comparison
        String reconPoAToken = reconstructedPoA.exportJWT(principalKeypair.getPrivate());
        System.out.println("Reconstruct example: " + reconPoAToken.equals(token));

    }

    private static long Days(int i) {
        return i * 86400000;
    }

}
