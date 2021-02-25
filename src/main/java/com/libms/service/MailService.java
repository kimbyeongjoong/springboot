package com.libms.service;

import com.libms.vo.JoinConfirmVo;
import com.libms.vo.MailVo;
import com.sun.mail.util.logging.MailHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
public class MailService {

    private JavaMailSender mailSender;
//    private static final String FROM_ADDRESS = "libms_test_noreply@gmail.com";

    MailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    private int size;

    //인증키 생성
    private String getKey(int size){
        this.size = size;
        return getAuthCode();
    }

    //인증코드 난수 발생
    private String getAuthCode(){
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while (buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }
        return buffer.toString();
    }

    // 회원가입 인증 메일 보내기
    public void sendAuthMail(JoinConfirmVo joinConfirmVo){
        //6자리 난수 인증번호 생성
        String authKey = getKey(6);
        System.out.println("authKey = " + authKey);
        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom("스프링부트 <your_email@address>");
                helper.setTo(joinConfirmVo.getEmail());
                helper.setSubject("회원가입 이메일 인증");
                helper.setText(new StringBuffer().append("<h1>이메일 인증</h1><br>")
                        .append("<p>이메일 인증 창에 아래 번호를 입력하세요.</p>")
                        .append("<h3>")
                        .append(authKey)
                        .append("</h3>")
                        .toString(), true); // setText 에 true 를 해줘야 메일에 html 적용 가능하다.
            }
        };
        mailSender.send(preparator);
    }



    // 관리자용 메일 보내기 시스템
    public void mailSend(MailVo mailVo){

        final MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom("스프링부트 <your_email@address>");
                helper.setTo(mailVo.getAddress());
                helper.setSubject(mailVo.getTitle());
                helper.setText(mailVo.getMessage(), true);
            }
        };

        mailSender.send(preparator);
    }
}
// 라이브러리 : spring-boot-starter-mail


//  참고용 코드
//    public void mailSend(MailVo mailVo){
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mailVo.getAddress()); //받는 사람 주소
//        message.setFrom(MailService.FROM_ADDRESS);  //보내는 사람 주소, 해당 메소드를 호출하지 않으면 spring.mail.username 으로 셋팅됨
//        message.setSubject(mailVo.getTitle());  //제목
//        message.setText(mailVo.getMessage());   //메세지 내용
//
//        javaMailSender.send(message);
//    }