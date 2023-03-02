package Library;

import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;

// Code starts by declaring the variables and constants used in this class, then it delcares the
// constructor for this class. next it creates an instance of the class with these parameters

/**
 * The intended interface for library users to access creating PoAs.
 */
public class PoAGen {

    /**
     * Passes the parameters to the PoA class.
     * @implNote This roundabout implementation is due to the PoA class having constructors not intended for direct access.
     *
     * @param resourceOwnerID
     * @param transferable
     * @param principalPublicKey
     * @param principalName
     * @param agentKey Agents public key
     * @param agentName
     * @param expiredAt
     * @param metaData Gets convetred to string for the JWT
     */
    public static PoA generate(
            int resourceOwnerID,
            int transferable,
            String principalPublicKey,
            String principalName,
            String agentKey,
            String agentName,
            Date expiredAt,
            String[] metaData){

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

    /**
     * @return Returns a PoA object setup with default values that's valid for a week from time of creation
     */
    
    // The code is a class that generates a new PoA
    // The generatedeault() method returns an instanace of the class, which is initalized with no
    // fields and has no methods.
    public static PoA generateDefault(){
        return new PoA();
    }
    
    // reconstruct() method takes in two parameters, token and key
    // It decodes the JWT from the token to get the body of claims, then gets the 
    // resourceOwnerID, transferable, principalPublickey, principalName, agentKey and iat from it

    /**
     * Reconstructs a PoA from the information stored within the JWT,
     * this should result in an identical PoA object to the one that was encoded.
     *
     * @param token JWT
     * @param key Issuers public key
     *
     * @return PoA reconstructed
     */
    public static PoA reconstruct(String token, Key key){
        Claims body = PoAValid.decodeJWT(token,key).getBody();
        
        //Precession loss on the New Dates due to the way JWT stores the dates in the token body
        //somewhat close to the PoA that's being recreated
        return new PoA(
                (int) body.get("resourceOwnerID"),
                (int) body.get("transferable"),
                (String) body.get("principalPublicKey"),
                (String) body.get("principalName"),
                (String) body.get("agentKey"),
                (String) body.get("agentName"), //todo Might be this is missed
            
            // then it setse up date information for when this object was generated (iat),
            // when it expires (exp), what its metaData is (path) as well as setting up how to encode
            // its public key into string form using KeyEncDecoder
            // Finally it returns an instance of itself with these changed made to it
                new Date((long) (int) body.get("iat") *1000),
                new Date((long) (int) body.get("exp") *1000),
                (String) body.get("metaData"))
                .setPath((String) body.get("path"));

    }
    
       // The code starts by creating a new PoA object
       // the constructor of the PoA class takes two arguments, a string token and an instance of Key PublicKeySource

    /**
     * Creates a modified PoA with added path information to enable recursive validation of the PoAs path
     *
     * @param token PoA in JWT format
     * @param PublicKeySource PublicKey of the device that sent you the PoA
     * @return Modified PoA with a reduced transferability
     */
    public static PoA transfer(String token, Key PublicKeySource){
        PoA poa = reconstruct(token,PublicKeySource);
        
        // the code then checks if the transferable property is not 0, meaning that there are tokens
        // left to be transferred. if so, it subtracts 1 from the trnaferable property 
        // and sets the path for this transaction to include "----" + KeyEncDec.stringEncodedKey(PublicKeySource
        // this will make sure that this transaction can only be used once before being invalidated
        // the code will set the retransmiters public key in metadata to enable recursive validation
        if(poa.getTransferable() != 0){
            poa.setTransferable(poa.getTransferable() - 1);

            //maby replade this usage of metadata with its own variable in the poa object
            poa.setPath(token + "-----" + KeyEncodeDecode.stringEncodedKey(PublicKeySource));
        }else{
            //throw some error (eg transferable == 0)
        }
        return poa;
        
        // the code above will first reconstruct the token, then check if it has a transferable value
    }
}
