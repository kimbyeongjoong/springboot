package com.libms.controller;

import com.libms.service.MailService;
import com.libms.vo.JoinConfirmVo;
import com.libms.vo.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MailController {

    @Autowired
    MailService mailService;

    @PostMapping("/mail")
    public String execMail(@ModelAttribute MailVo mailVo){
        mailService.mailSend(mailVo);
        return "index";
    }

    // 회원가입 이메일 인증
    @PostMapping("/sendAuthMail")
    @ResponseBody
    public Map<String, String> sendAuthMail(
            @ModelAttribute JoinConfirmVo joinConfirmVo,
            HttpServletRequest request){

        String authKey = mailService.sendAuthMail(joinConfirmVo);
        HttpSession session = request.getSession();
        session.setAttribute("authKey", authKey);
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("authKey", authKey);
        return resultMap;
    }

}
