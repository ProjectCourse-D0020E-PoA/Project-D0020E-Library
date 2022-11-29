package Library;


import java.security.KeyPair;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class Main {

    public static void main(String[] args){

        // We need a signing key, so we'll create two just for this example.
        // Usually the key would be read from your application configuration instead.
        KeyPair KeyPair1 = Keys.keyPairFor(SignatureAlgorithm.RS256);
        KeyPair KeyPair2 = Keys.keyPairFor(SignatureAlgorithm.RS256);

        String[] bob ={"bob", "bob"};

        PoA testPoA = PoAGen.generate(
                1,
                2,
                "principalPublicKey",
                "principalName",
                "agentKey",
                "RS256",
                new Date(System.currentTimeMillis()+ 86400),
                bob
               );

        // Converting the data stored in the PoA to a JWT using KeyPair1
        String JWT = testPoA.exportJWT(KeyPair1.getPrivate());
        System.out.println("Json web token: " + JWT);


        // Decoding the JWT signed with KeyPair1 using KeyPair1
        Jws<Claims> res = PoAValid.decodeJWT(JWT, KeyPair1.getPublic());
        System.out.println("Decoding with correct key example: " + res.getBody().values());

        // Decoding the JWT signed with KeyPair1 using KeyPair2.
        // This should throw the Invalid Error
        try {
            res = PoAValid.decodeJWT(JWT, KeyPair2.getPublic());
            System.out.println(res.getBody().values());
        }catch(Error e){
            System.out.println("Decoding with wrong key example: " + e.getMessage());
        }

        //validating with the correct key
        System.out.println("valid example: " + PoAValid.validate(JWT,KeyPair1.getPublic()));

        //validating with the incorrect key
        System.out.println("invalid example: " + PoAValid.validate(JWT,KeyPair2.getPublic()));

        //getting a specific claim out of the token
        System.out.println("getting claim Example: " + (int)res.getBody().get("iat"));

        //Reconstruction a PoA from the JWT
        String token = testPoA.exportJWT(KeyPair1.getPrivate());
        PoA reconstructedPoA = PoAGen.reconstruct(token,KeyPair1.getPublic());
        //signing the reconstructed PoA to enable comparison
        String reconPoAToken = reconstructedPoA.exportJWT(KeyPair1.getPrivate());
        System.out.println("Reconstruct example: " + reconPoAToken.equals(token));
    }

}
