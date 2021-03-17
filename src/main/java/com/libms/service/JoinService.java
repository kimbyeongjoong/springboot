package com.libms.service;

import com.libms.mapper.JoinMapper;
import com.libms.vo.User_emailVo;
import com.libms.vo.User_infoVo;
import com.libms.vo.User_telVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class JoinService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JoinMapper mapper;

    public void userJoin(User_infoVo user_infoVo){
        mapper.userJoin(user_infoVo);
    }
    public void insertUserTel(User_telVo user_telVo){
        mapper.insertUserTel(user_telVo);
    }
    public void insertUserEmail(User_emailVo user_emailVo) {
        mapper.insertUserEmail(user_emailVo);
    }

    public String idDuplicateCheck(String user_id){
        return mapper.idDuplicateCheck(user_id);
    }
    public String emailDuplicateCheck(User_emailVo user_emailVo){
        return mapper.emailDuplicateCheck(user_emailVo);
    }


    @Transactional
    public void transactionJoin(
            User_infoVo user_infoVo,
            User_telVo user_telVo,
            User_emailVo user_emailVo){

        String raw = user_infoVo.getPassword();

        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            String salt = new String(Base64.getEncoder().encode(bytes));

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            md.update(raw.getBytes());
            String hex = String.format("%064x", new BigInteger(1, md.digest()));

            user_infoVo.setPassword(hex);
            user_infoVo.setSalt(salt);

            logger.info("회원의 원래 비밀번호(확인용) = " + raw);
            logger.info("password = " + user_infoVo.getPassword());
            logger.info("salt = " + user_infoVo.getSalt());

            insertUserTel(user_telVo);
            insertUserEmail(user_emailVo);
            userJoin(user_infoVo);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
