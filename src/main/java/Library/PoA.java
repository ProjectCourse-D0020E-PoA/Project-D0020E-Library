package Library;

import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;

public class PoA {

    private int resourceOwnerID = 0;
    private int transferable = 0;
    private String principalPublicKey = "default";
    private String principalName = "default";
    private String agentPublicKey = "default";
    private String agentName = "default";
    private String signingAlogrithm = "RS256";
    private Date issuedAt;
    private Date expiredAt;

    public String getPath() {
        return path;
    }

    public PoA setPath(String path) {
        this.path = path;
        return this;
    }

    private String path = "";
    private String metaData = "default";

    public PoA setResourceOwnerID(int resourceOwnerID) {
        this.resourceOwnerID = resourceOwnerID;
        return this;
    }

    public PoA setTransferable(int transferable) {
        this.transferable = transferable;
        return this;
    }

    public PoA setPrincipalPublicKey(String principalPublicKey) {
        this.principalPublicKey = principalPublicKey;
        return this;
    }

    public PoA setPrincipalName(String principalName) {
        this.principalName = principalName;
        return this;
    }

    public PoA setAgentPublicKey(String agentPublicKey) {
        this.agentPublicKey = agentPublicKey;
        return this;
    }
    public PoA setAgentName(String agentName) {
        this.agentName = agentName;
        return this;
    }
    /*
    public PoA setSigningAlogrithm(String signingAlogrithm) {
        this.signingAlogrithm = signingAlogrithm;
        return this;
    }
    */
    public PoA setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
        return this;
    }

    public PoA setMetaData(String metaData) {
        this.metaData = metaData;
        return this;
    }
    public PoA setMetaData(String[] metaData) {
        this.metaData = metaData.toString();
        return this;
    }

    /**
     * @param resourceOwnerID
     * @param transferable
     * @param principalPublicKey
     * @param principalName
     * @param agentKey
     * @param expiredAt
     * @param metaData
     */
    protected PoA(
            int recourceOwnerID,
            int transferable,
            String pricipalPublicKey,
            String principalName,
            String agentKey,
            String agentName,
            Date expiredAt,
            String[] metaData) {

        this.principalPublicKey = pricipalPublicKey;
        this.resourceOwnerID = recourceOwnerID;
        this.transferable = transferable;
        this.principalName = principalName;
        this.agentPublicKey = agentKey;
        this.agentName = agentName;
        this.signingAlogrithm = signingAlogrithm;
        this.issuedAt = new Date(System.currentTimeMillis());
        this.expiredAt = expiredAt;
        this.metaData = Arrays.toString(metaData);
    }

    public int getTransferable() {
        return transferable;
    }

    protected PoA(
            int recourceOwnerID,
            int transferable,
            String pricipalPublicKey,
            String principalName,
            String agentKey,
            Date issuedAt,
            Date expiredAt,
            String metaData) {

        this.principalPublicKey = pricipalPublicKey;
        this.resourceOwnerID = recourceOwnerID;
        this.transferable = transferable;
        this.principalName = principalName;
        this.agentPublicKey = agentKey;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.metaData = metaData;
    }

    protected PoA(){
        this.issuedAt = new Date();
        this.expiredAt = new Date(System.currentTimeMillis()+604800000);
    };
    /**
     * @param privateKey (RS256 compatible)
     * @return String containing JWT with added claims for all the PoAs stored information.
     */
    public String exportJWT(Key privateKey) {
        return Jwts.builder()
                .signWith(privateKey)
                .setIssuedAt(this.issuedAt)
                .setExpiration(this.expiredAt)
                .claim("pricipalPublicKey", this.principalPublicKey)
                .claim("principalName", this.principalName)
                .claim("recourceOwnerID", this.resourceOwnerID)
                .claim("transferable", this.transferable)
                .claim("metaData", this.metaData)
                .claim("agentKey", this.agentPublicKey)
                .claim("signingAlogrithm", "RS256")
                .claim("path",this.path)
                .compact();
    }
}
