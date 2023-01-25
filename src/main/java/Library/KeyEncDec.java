package Library;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

    // The method takes in a string and returns a key object. 
    // The string is decoded using the Base64 class and then converted to a key object.
public class KeyEncDec {
    // The code starts by declaring the variables that are needed for decoding and then declares a method 
    // called decodeKeyBytesPublic().
    // This method takes in a String, which contains the encoded public key,
    //  and returns a byte array of the decoded public key.
    public static Key decodeKeyBytesPublic(String encodedPublicKey){
        byte[] keyBytes = Base64.getDecoder().decode(encodedPublicKey);
        try {
    // The next step is to create an instance of KeyFactory using "RSA" as its algorithm name.
            final KeyFactory fact = KeyFactory.getInstance("RSA");
    // Then it creates an X509EncodedKeySpec object with the bytes contained 
    // in the byte array returned by decodeKeyBytesPublic() as its input parameter.
            final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
            return fact.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Error("Failed to read PoaOnboarding public key");
        }
    }
    // The method takes in a key object and returns a string. 
    // The key is encoded using the Base64 class and then converted to a string.
    public static String stringEncodedKey(Key publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}
