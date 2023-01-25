package Library;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;

import java.security.Key;

import static io.jsonwebtoken.Jwts.parserBuilder;

// The code is trying to decode a JWT token
public class PoAValid {
// The code start by creating a parser builder and sets the signing key as pubklicKey
// Then it builds the decoded claims jwt, which is then parsed with parseClaimsJws()
    public static Jws<Claims> decodeJWT(String token, Key publicKey){
        try {
            // if there are no errors in parsing the token, then this function returns
            // decoded Claims object that was created from decoding the JWT token
            Jws<Claims> decoded = parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return decoded;
        }catch (SignatureException e){
            // if there is an error parsing the token, such as if it's expired
            // or invalid, then this Error gets thrown
            throw new Error("Invalid");
            //this Error gets thrown whenever the token/key pair is unable to be verified
        }catch (ExpiredJwtException e){
            throw new Error("Expired token");
        }
    }
// Code is trying to validate a JWT token
    // starts by creating a parser builder and setting the signing key for it
    // then, the code parses claims from the token using this parser builder
    public static boolean validate(String token, Key publicKey){
        try {
            // if there is an error parsing the claims, then true will be returned as false otherwise
            // true will be returned
            parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            return false;
        }
        //add handling in the case of an expired token
        // the code above does the following, 
        // parses the token using parser builder with the public key as its signing key
        // if validation fails, returns false
    }
    // The code is trying to validate JWT token
    // the code starts by decoding the toke nadn then getting the decoded JWT
    public static boolean validateRecursively(String token, Key publicKey){
        try {
            Claims decoded = decodeJWT(token,publicKey).getBody();
            String metaDat = decoded.get("path").toString();
            // if there's metadata, it will split up the string into array of strings and then check
            // each one for the "path"
            // if path exists in any of them, it will call validateRecursively on that value
            // with KeyEncDecodeKeyBytesPublic as its key
            if (metaDat.equals("path")){
                String[] metaData = decoded.get("metaData").toString().split("-----");
                return validateRecursively(metaData[0],KeyEncDec.decodeKeyBytesPublic(metaData[1]));
            }
            else{
                return validate(token,publicKey);
            }
             // if there is no metadata, it will return false
        }catch (Error e){
            return false;
            
            // the code is trying to validate the token and public key in a recursive manner
        }
    }
}

