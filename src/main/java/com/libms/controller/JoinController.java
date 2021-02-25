package com.libms.controller;

import com.libms.service.JoinService;
import com.libms.vo.JoinConfirmVo;
import com.libms.vo.User_emailVo;
import com.libms.vo.User_infoVo;
import com.libms.vo.User_telVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class JoinController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    JoinService joinService;

    @PostMapping("joinConfirm")
    @ResponseBody
    public JoinConfirmVo joinConfirm(@ModelAttribute JoinConfirmVo joinConfirmVo){
        //StringBuffer 써봤을뿐 의미없다. 이렇게 하나 +연산자로 붙이나 똑같다 아마도
        //StringBuffer 는 값이 계속 변할 때 메모리를 적게 먹는다.
        //그래서 동일한 객체 내에서 문자열이 계속 변해야하는 로직일 때 사용하면 좋다.
        //StringBuffer 와 StringBuilder 의 차이점은 동기화의 유무이다.
        //StringBuilder 는 동기화를 지원하지 않는다. 그래서 단일쓰레드환경에서 사용하는 것이 좋다.
        StringBuffer title = new StringBuffer(joinConfirmVo.getTitle());
        title.append("_controller 갔다왔습니다~");
        joinConfirmVo.setTitle(title.toString());
        System.out.println("joinConfirmVo = " + joinConfirmVo);//vo에 있는 toString 불러오게 된다.
        return joinConfirmVo;
    }

    @PostMapping("idDuplicateCheck")
    @ResponseBody
    public String idDuplicateCheck(@RequestParam("id") String inputId){
        String idCheck = joinService.idDuplicateCheck(inputId);
        if(idCheck == null)
            return "success";
        return "duplicate";
    }

    @PostMapping("emailDuplicateCheck")
    @ResponseBody
    public String emailDuplicateCheck(@ModelAttribute User_emailVo user_emailVo){
        String emailCheck = joinService.emailDuplicateCheck(user_emailVo);
        System.out.println("emailCheck = " + emailCheck);
        if(emailCheck == null)
            return "success";
        return "duplicate";
    }

    // 회원가입 (이메일 인증 부분은 MailController 확인할 것)
    @PostMapping("user_join_do")
    public String user_join_do(
            @ModelAttribute User_infoVo user_infoVo,
            Model model,
            HttpServletRequest request){
        if(user_infoVo.getAuthKey() == null){
            logger.info("user_infoVo = " + user_infoVo);
            logger.info("* email authentication is null");
        }
        User_telVo tel = new User_telVo();
        User_emailVo email = new User_emailVo();

        // 따로따로 쓴 이메일 합치기
        String email1 = request.getParameter("email1");
        String email2 = request.getParameter("email2");
        String user_email = email1 + "@" + email2;

        // 따로따로 쓴 전화번호 합치기
        String phone1 = request.getParameter("phone1");
        String phone2 = request.getParameter("phone2");
        String phone3 = request.getParameter("phone3");
        String user_tel = phone1 + phone2 + phone3;

        // 따로따로 쓴 생년월일 합치기
        String birth1 = request.getParameter("birth1");
        String birth2 = request.getParameter("birth2");
        String birth3 = request.getParameter("birth3");
        String user_birth = birth1 + "-" + birth2 + "-" + birth3;

        // vo에 합친 String 정보 집어넣기
        tel.setUser_tel(user_tel);
        tel.setSns_agree("y");
        email.setUser_email(user_email);
        email.setEmail_agree("y");
        user_infoVo.setUser_email(user_email);
        user_infoVo.setUser_tel(user_tel);
        user_infoVo.setUser_birth(user_birth);
        // 유저 권한 정보 넣기
        user_infoVo.setRole("ROLE_USER");

        // join 메소드를 실행하기 전에 이메일 인증을 하게 만든다.
        // 이메일 인증을 하지 않으면 가입 할 수 없게 만든다.
        // 아이디, 비밀번호, 이메일 중복체크도 만든다.

        joinService.transactionalJoin(user_infoVo, tel, email);

        // toString 출력
        logger.info("user_infoVo = " + user_infoVo);

        return "redirect:user/user_board";
    }
}
