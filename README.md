To install this library simply copy the Library package and use the methods within that.
There is a Main file within the folder that could be used to test the library functionality

# Project-D0020E POA lib
The library is intended to be published to some public java Library site ex Maven.
Currently, the library code is just in a separate repository to emulate having it as an import for our test applications.

The implementation is object based, where you can generate a PoA object.
```java
    public class PoA {

    //sets default values
    private int resourceOwnerID = 0;
    private int transferable = 0;
    private String principalPublicKey = "default";
    private String principalName = "default";
    private String agentPublicKey = "default";
    private String agentName = "default";
    private String signingAlogrithm = "RS256";
    private Date issuedAt;
    private Date expiredAt;
    private String path = "";
    private String metaData = "default";
    }
```
   Using build in methods in ``PoAGen`` to generate the object and using setters to set the variables in the generated PoA object.
   
### Exaples for generating PoA´s
````java
public class example {
    
    PoA poa = PoAGen
            .generateDefault()
            .setTransferable(1)
            .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(PrivateKey))
            .setPrincipalName("bob")
            .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(PublicKey))
            .setAgentName("agent1");
}
````

This example generates a default PoA and sets the required variables using the setter methods.
Since the setters return the PoA u can call them in one after another like shown above.

````java
public class PoAGen {
    
    public static PoA generate(
            int resourceOwnerID,
            int transferable,
            String principalPublicKey,
            String principalName,
            String agentKey,
            String agentName,
            Date expiredAt,
            String[] metaData) {

        return new PoA(
                resourceOwnerID,
                transferable,
                principalPublicKey,
                principalName,
                agentKey,
                agentName,
                expiredAt,
                metaData);
    }
}
public class example{
    
    PoA testPoA = PoAGen.generate(
            someID,
            transferable,
            "principalPublicKey",
            "principalName",
            "agentKey",
            "agentName",
            new Date(System.currentTimeMillis() + Days(5)),
            SomeStringArray);
}
````
This method in ``PoAGen`` is also an option but since it requires all the variables at once its less flexible and annoying to use.
The roundabout implementation is due to the intended scope of the PoA methods.
This is to prevent direct accuses to the PoA methods to block un-intended utilization of them.

We implemented both the constructor and setter methods to enable developers to use whatever method they find the easiest.

### Example PoA Validation

 Attempts to parse the PoA token using the Key,
 if the key is wrong, or the token has been modified this will false otherwise true.
 This method will also check the PoA path and validate each step.

````java
class PoAValid {
    public static boolean validateRecursively(String PoAToken, key PublicKey) {...};
    public static boolean validate(String PoAToken, key PublicKey) {...};
}

public class example {

    PoA poa = PoAGen
            .generateDefault()
            .setTransferable(1)
            .setPrincipalPublicKey(KeyEncodeDecode.stringEncodedKey(PrivateKey))
            .setPrincipalName("bob")
            .setAgentPublicKey(KeyEncodeDecode.stringEncodedKey(PublicKey))
            .setAgentName("agent1");
    String JWT = poa.exportJWT(PrivateKey);
    
    //both these would in this case return true
    PoAValid.validate(JWT, PublicKey);
    PoAValid.validateRecursively(JWT, PublicKey);
}
````

One method for a simple 2 party validation and one for validating a transferred PoA token.
Both methods take the JWT, and the senders public key.

The intended use of this is for a Resource-owner to validate the PoAs validity before grating whatever resource the Agent is requesting.

### KeyEncodeDecode
Is a class containing helper methods to encode and decode sha256 keys to a string format and back.
_**Encoding**_ works the same for both Private and Public Keys, but _**Decoding**_ requires a different method for the different Key types



