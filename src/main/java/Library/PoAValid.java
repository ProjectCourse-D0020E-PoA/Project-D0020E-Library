package Library;
import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.Date;

public class PoAValid {

    private int                 recourceOwnerID;
    private int                 transferable;
    private String              pricipalPublicKey;
    private String              principalName;
    private String              agentKey;
    private String              signingAlogrithm;
    private Date                issuedAt;
    private Date                expiredAt;
    private ArrayList<String>   metaData;

    public boolean isValid(PoA poa){return valid(poa);}
    public boolean isValid(Jwts jwt){return valid(PoAGen.reconstruct(jwt));}
    private boolean valid(PoA poa){
        /* do stuff */
        return true;
    }
}
