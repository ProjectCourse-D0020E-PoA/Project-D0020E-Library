package Library;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

class KeyEncDec {
    public static Key decodeKeyBytesPublic(String encodedPublicKey){
        byte[] keyBytes = Base64.getDecoder().decode(encodedPublicKey);
        try {
            final KeyFactory fact = KeyFactory.getInstance("RSA");
            final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(keyBytes);
            return fact.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new Error("Failed to read PoaOnboarding public key");
        }
    }

    public static String stringEncodedKey(Key publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }
}
