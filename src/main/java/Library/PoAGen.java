package Library;

import io.jsonwebtoken.Jwts;

import java.util.ArrayList;
import java.util.Date;

public class PoAGen {

    private int                 recourceOwnerID;
    private int                 transferable;
    private String              pricipalPublicKey;
    private String              principalName;
    private String              agentKey;
    private String              signingAlogrithm;
    private Date                issuedAt;
    private Date                expiredAt;
    private ArrayList<String>   metaData;


    public static PoA generate(){
        return new PoA(
                1,
                2,
                "",
                "",
                "",
                "",
                new Date(),
                new ArrayList<String>());
    }
    public static PoA reconstruct(Jwts jwt){

        return new PoA(
                1,
                2,
                "",
                "",
                "",
                "",
                new Date(),
                new ArrayList<String>());
    }
}
