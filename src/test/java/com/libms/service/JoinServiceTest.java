package com.libms.service;

import com.libms.vo.User_infoVoTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

class JoinServiceTest {


    @Test
    public User_infoVoTest userJoinTest(String id, String raw) throws Exception{

        User_infoVoTest info = new User_infoVoTest();

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = new String(Base64.getEncoder().encode(bytes));

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt.getBytes());
        md.update(raw.getBytes());
        String hex = String.format("%064x", new BigInteger(1, md.digest()));

        info.setUser_id(id);
        info.setPassword(hex);
        info.setSalt(salt);

        System.out.println("raw = " + raw);
        System.out.println("random = " + random);
        System.out.println("bytes = " + bytes);
        System.out.println("salt = " + salt);
        System.out.println("md = " + md);
        System.out.println("hex = " + hex);
        System.out.println("user_id = " + info.getUser_id());
        System.out.println("password = " + info.getPassword());
        System.out.println("///////////////////////");

        return info;
    }




}
