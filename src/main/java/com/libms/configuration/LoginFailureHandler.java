package com.libms.configuration;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    // 참조 https://to-dy.tistory.com/92
    // 참조 https://simsimjae.tistory.com/42

    private String user_id;
    private String password;

    public LoginFailureHandler(String user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String user_id_value = request.getParameter("user_id");
        String password_value = request.getParameter("password");
        String errorMessage = null;

        if(exception instanceof BadCredentialsException){
            errorMessage = "아이디 또는 비밀번호가 틀렸습니다.";
        }
        if(exception instanceof DisabledException){
            errorMessage = "계정이 비활성화 상태입니다.";
        }

        System.out.println("user_id_value = " + user_id_value);
        System.out.println("password_value = " + password_value);
        System.out.println("errorMessage = " + errorMessage);
        System.out.println("user_id = " + user_id);
        System.out.println("password = " + password);

        // setAttribute 로 변수 전달할 필요가 없을 것 같다. view 단에서 값을 전달받을 수는 있으나
        // 어차피 forward 라서 입력한 아이디 문자열이 그대로 존재하기 때문에,
        // errorMessage 전달 말고는 의미가 없을 것 같다.
        request.setAttribute(user_id, user_id_value);
        request.setAttribute(password, password_value);
        request.setAttribute("errorMessage", errorMessage);
        // HttpServletRequest 의 getRequestDispatcher 메서드를 이용해서 보여줄 화면으로 forward 해준다.
        // forward를 해줘야만 jstl(서버 값 출력)을 이용해서 값들을 가져올 수 있다.
        // 여기서 보여줄 화면은 로그인 실패 시 이동할 페이지(defaultFailureUrl) 이라고 생각하면 된다.
        request.getRequestDispatcher("/login?error=true").forward(request, response);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
