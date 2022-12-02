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
            Date expiredAt,
            String[] metaData){

            return new PoA(
                    recourceOwnerID,
                    transferable,
                    pricipalPublicKey,
                    principalName,
                    agentKey,
                    expiredAt,
                    metaData);
    }

    public static PoA generateDefault(){
        return new PoA();
    }

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
                new Date((long) (int) body.get("iat") *1000),
                new Date((long) (int) body.get("exp") *1000),
                (String) body.get("metaData"));
    }
}
