package Library;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.ArrayList;
import java.util.Date;
import java.security.Key;

public class PoA {

    private int                 recourceOwnerID;
    private int                 transferable;
    private String              pricipalPublicKey;
    private String              principalName;
    private String              agentKey;
    private String              signingAlogrithm;
    private Date                issuedAt;
    private Date                expiredAt;
    private ArrayList<String>   metaData;

    protected PoA(
            int                 recourceOwnerID,
            int                 transferable,
            String              pricipalPublicKey,
            String              principalName,
            String              agentKey,
            String              signingAlogrithm,
            Date                expiredAt,
            ArrayList<String>   metaData){

        this.pricipalPublicKey  = pricipalPublicKey;
        this.recourceOwnerID    = recourceOwnerID;
        this.transferable       = transferable;
        this.principalName      = principalName;
        this.agentKey           = agentKey;
        this.signingAlogrithm   = signingAlogrithm;
        this.issuedAt           = new Date();
        this.expiredAt          = expiredAt;
        this.metaData           = metaData;
    }
    public String exportJWT(Key privateKey) {
        return Jwts.builder()
            .signWith(SignatureAlgorithm.RS256, privateKey)
            .setIssuedAt(                  this.issuedAt)
            .setExpiration(                this.expiredAt)
            .claim("pricipalPublicKey", this.pricipalPublicKey)
            .claim("principalName",     this.principalName)
            .claim("recourceOwnerID",   this.recourceOwnerID)
            .claim("transferable",      this.transferable)
            .claim("metaData",          this.metaData.toString())
            .claim("agentKey",          this.agentKey)
            .claim("signingAlogrithm", "RS256")
            .compact();
    }
}
