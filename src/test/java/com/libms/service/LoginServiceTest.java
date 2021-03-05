package com.libms.service;

import com.libms.vo.User_infoVoTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

class LoginServiceTest {

    @Test
    public void Authentication() throws Exception {

        //회원가입
        JoinServiceTest join = new JoinServiceTest();
        User_infoVoTest info;
        String id = "tester01";
        String raw = "12345";
        info = join.userJoinTest(id, raw);
        System.out.println("회원 아이디 = " + info.getUser_id());
        System.out.println("회원 비밀번호 = " + info.getPassword());
        System.out.println("회원 salt 값 = " + info.getSalt());
        System.out.println("회원가입 끝");

        //로그인
        String input_id = "tester01";
        String input_pw = "12345";
        String salt = info.getSalt();

        System.out.println("로그인 시도합니다");

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt.getBytes());
        md.update(input_pw.getBytes());
        String hex = String.format("%064x", new BigInteger(1, md.digest()));
        // %064x에서 x는 16진수이고, 총 64자리의 16진수에서 값이 들어가지 못한 자리는 0으로 채워지게 됩니다.

        if(input_id.equals(info.getUser_id())){

            if(info.getPassword().equals(hex)){
                System.out.println("비밀번호 일치");
            }else{
                System.out.println("비밀번호 불일치");
                System.out.println("입력한 비밀번호 = " + hex);
                return;
            }
            System.out.println("로그인 성공");
            System.out.println("입력한 비밀번호 = " + hex);
        }else{
            System.out.println("아이디 불일치");
        }


    }

    @Test
    void testtest() throws NoSuchAlgorithmException {

        String pw = "1";

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        String salt = new String(Base64.getEncoder().encode(bytes));

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.reset();
        md.update(salt.getBytes());
        md.update(pw.getBytes());
        String result = String.format("%064x", new BigInteger(1, md.digest()));
        // %064x에서 x는 16진수이고, 총 64자리의 16진수에서 값이 들어가지 못한 자리는 0으로 채워지게 됩니다.
        System.out.println("salt = " + salt);
        System.out.println("result = " + result);

    }

}
