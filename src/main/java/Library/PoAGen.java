package Library;

import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;

public class PoAGen {

    public static PoA generate(
            int recourceOwnerID,
            int transferable,
            String pricipalPublicKey,
            String principalName,
            String agentKey,
            String agentName,
            Date expiredAt,
            String[] metaData){

            return new PoA(
                    recourceOwnerID,
                    transferable,
                    pricipalPublicKey,
                    principalName,
                    agentKey,
                    agentName,
                    expiredAt,
                    metaData);
    }

    /**
     * @return returns a PoA object setup with default values that's valid for a week from time of creation
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
    public static PoA reconstruct(String token, Key key){
        Claims body = PoAValid.decodeJWT(token,key).getBody();
        
        //Precession loss on the New Dates due to the way JWT stores the dates in the token body
        //somewhat close to the PoA that's being recreated
        return new PoA(
                (int) body.get("recourceOwnerID"),
                (int) body.get("transferable"),
                (String) body.get("pricipalPublicKey"),
                (String) body.get("principalName"),
                (String) body.get("agentKey"),
            
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
    public static PoA transfer(String token, Key PublicKeySource){
        PoA poa = reconstruct(token,PublicKeySource);
        
        // the code then checks if the transferable property is not 0, meaning that there are tokens
        // left to be transferred. if so, it subtracts 1 from the trnaferable property 
        // and sets the path for this transaction to include "----" + KeyEncDec.stringEncodedKey(PublicKeySource
        // this will make sure that this transaction can only be used once before being invalidated
        // the code attempts to transfer the token to the public key soruce PublicKeySource
        if(poa.getTransferable() != 0){
            poa.setTransferable(poa.getTransferable() - 1);

            //maby replade this usage of metadata with its own variable in the poa object
            poa.setPath(token + "-----" + KeyEncDec.stringEncodedKey(PublicKeySource));
        }
        return poa;
        
        // the code above will first reconstruct the token, then check if it has a transferable value
    }
}
