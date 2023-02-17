package Library;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

    // The method takes in a string and returns a key object. 
    // The string is decoded using the Base64 class and then converted to a key object.

/**
 * Handles encoding private and public keys to encoded strings and the reverse process
 */
public class KeyEncodeDecode {
    // The code starts by declaring the variables that are needed for decoding and then declares a method 
    // called decodeKeyBytesPublic().
    // This method takes in a String, which contains the encoded public key,
    //  and returns a byte array of the decoded public key.
        /**
         *
         * @param encodedPublicKey Base64 encoded string
         * @return PublicKey recreated from the encoded string
         * @throws Error Failed to read PoaOnboarding private key
         * @implNote This only works for publicKeys, will throw error if the String is formatted incorrectly.
         */
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

        /**
         * Returns the key in a base 64 encoded format encoded to a string.
         * @param key Private or Public key
         * @return String encoded base 64
         */
    public static String stringEncodedKey(Key key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

        /**
         *
         * @param encodedPrivateKey Base64 encoded string
         * @return PrivateKey recreated from the encoded string
         * @throws Error Failed to read PoaOnboarding private key
         * @implNote This only works for privateKeys, will throw error if the String is formatted incorrectly.
         */
    public static Key decodeKeyBytesPrivate(String encodedPrivateKey){
        byte[] keyBytes = Base64.getDecoder().decode(encodedPrivateKey);
        try {
            // The next step is to create an instance of KeyFactory using "RSA" as its algorithm name.
            final KeyFactory fact = KeyFactory.getInstance("RSA");
            // Then it creates an X509EncodedKeySpec object with the bytes contained
            // in the byte array returned by decodeKeyBytesPrivate() as its input parameter.
            final PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            return fact.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Error("Failed to read PoaOnboarding private key");
        }
    }
}
