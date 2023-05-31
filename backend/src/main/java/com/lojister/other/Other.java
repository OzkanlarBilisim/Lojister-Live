package com.lojister.other;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

@Service
public class Other {
    // rasgele key oluşturmak için
    public String randomLetter(int piece){
        String alfabe = "abcdefghijklmnopqrstuvwxyz";

        String key = null;
        for(int i=0; i<piece; i++){
            Random r = new Random();
            char rasgeleHarf = alfabe.charAt(r.nextInt(alfabe.length()));

            if (i==0){
                key = String.valueOf(rasgeleHarf);
            }else {
                key = key + rasgeleHarf;
            }
        }
        return key;
    }

}
