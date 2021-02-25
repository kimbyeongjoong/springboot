package com.libms.controller;

import com.libms.service.MailService;
import com.libms.vo.JoinConfirmVo;
import com.libms.vo.MailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String sendAuthMail(@ModelAttribute JoinConfirmVo joinConfirmVo){
        // 테스트 할 때는 주석처리해둘것
//        mailService.sendAuthMail(joinConfirmVo);
        return "success";
    }

}
