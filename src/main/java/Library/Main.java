package Library;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static Key pub;
    public static Key pvt;

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println("test");
        PoA testPoA = Library.PoAGen.generate();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        pub = kp.getPublic();
        pvt = kp.getPrivate();

        print(testPoA.exportJWT(pvt));


    }
    public static void print(String t){ System.out.println(t); }

}
