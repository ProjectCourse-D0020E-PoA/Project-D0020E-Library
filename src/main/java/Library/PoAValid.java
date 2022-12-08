package Library;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;

import static io.jsonwebtoken.Jwts.parserBuilder;

public class PoAValid {

    public static Jws<Claims> decodeJWT(String token, Key publicKey){
        try {
            Jws<Claims> decoded = parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return decoded;
        }catch (SignatureException e){
            throw new Error("Invalid");
            //this Error gets thrown whenever the token/key pair is un able to be verified
        }catch (ExpiredJwtException e){
            throw new Error("Expired token");
        }
    }

    public static boolean validate(String token, Key publicKey){
        try {
            parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            return false;
        }
        //add handling in the case of an expired token
    }
    
    public static boolean validateRecursively(String token, Key publicKey){
        try {
            Claims decoded = decodeJWT(token,publicKey).getBody();
            if (decoded.get("metaData").toString() != "default"){
                //split seams to be not working
                String[] metaData = decoded.get("metaData").toString().replace("jwt = ","").replace( " sender = ","").split("-----");
                return validateRecursively(metaData[0],KeyEncDec.decodeKeyBytesPublic(metaData[1]));
            }
            else{
                return validate(token,publicKey);
            }
        }catch (Error e){
            return false;
        }
    }
}

