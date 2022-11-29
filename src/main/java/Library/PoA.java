package Library;

import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;

public class PoA {

    private int recourceOwnerID;
    private int transferable;
    private String pricipalPublicKey;
    private String principalName;
    private String agentKey;
    private String signingAlogrithm;
    private Date issuedAt;
    private Date expiredAt;
    private String metaData;

    /**
     * @param recourceOwnerID
     * @param transferable
     * @param pricipalPublicKey
     * @param principalName
     * @param agentKey
     * @param signingAlogrithm
     * @param expiredAt
     * @param metaData
     */
    protected PoA(
            int recourceOwnerID,
            int transferable,
            String pricipalPublicKey,
            String principalName,
            String agentKey,
            String signingAlogrithm,
            Date expiredAt,
            String[] metaData) {

        this.pricipalPublicKey = pricipalPublicKey;
        this.recourceOwnerID = recourceOwnerID;
        this.transferable = transferable;
        this.principalName = principalName;
        this.agentKey = agentKey;
        this.signingAlogrithm = signingAlogrithm;
        this.issuedAt = new Date(System.currentTimeMillis());
        this.expiredAt = expiredAt;
        this.metaData = Arrays.toString(metaData);
    }
    protected PoA(
            int recourceOwnerID,
            int transferable,
            String pricipalPublicKey,
            String principalName,
            String agentKey,
            String signingAlogrithm,
            Date issuedAt,
            Date expiredAt,
            String metaData) {

        this.pricipalPublicKey = pricipalPublicKey;
        this.recourceOwnerID = recourceOwnerID;
        this.transferable = transferable;
        this.principalName = principalName;
        this.agentKey = agentKey;
        this.signingAlogrithm = signingAlogrithm;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.metaData = metaData;
    }

    /**
     * @param key
     * @return
     */
    public String exportJWT(Key key) {
        return Jwts.builder()
                .signWith(key)
                .setIssuedAt(this.issuedAt)
                .setExpiration(this.expiredAt)
                .claim("pricipalPublicKey", this.pricipalPublicKey)
                .claim("principalName", this.principalName)
                .claim("recourceOwnerID", this.recourceOwnerID)
                .claim("transferable", this.transferable)
                .claim("metaData", this.metaData)
                .claim("agentKey", this.agentKey)
                .claim("signingAlogrithm", "RS256")
                .compact();
    }
}
