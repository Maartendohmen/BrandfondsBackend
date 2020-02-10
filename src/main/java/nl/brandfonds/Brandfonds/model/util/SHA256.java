package nl.brandfonds.Brandfonds.model.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    private static final String salt = "e24AzH";

    private SHA256() {

    }

    public static String SHA256(String base){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(base.getBytes("UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i<hash.length;i++){
                String hex =Integer.toHexString(0xff & hash[i]);
                //“0xff”masks the variable so it leaves only the value in the last 8 bits
                if (hex.length()==1){
                    stringBuffer.append('0');
                }
                stringBuffer.append(hex);
            }
            return stringBuffer.toString() + salt;
        }
        catch (NoSuchAlgorithmException |UnsupportedEncodingException e){
            throw new RuntimeException();
        }

    }
}
