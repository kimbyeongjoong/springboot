package com.libms.component;

import com.libms.service.UserService;
import com.libms.vo.LoginForm;
import com.libms.vo.User_infoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

//        String user_id = authentication.getName();
//        String password = (String) authentication.getCredentials();
        LoginForm loginForm = new LoginForm();

        // vo에 입력된 아이디와 비밀번호 할당
        loginForm.setUser_id(authentication.getName());
        loginForm.setPassword((String) authentication.getCredentials());

        // salt 값 DB에서 가져오기
        loginForm.setSalt(userService.getSalt(loginForm));

        // 로그인 시도 and 계정 정보 가져오기(role 정보를 가져오기 위함)
        User_infoVo user_infoVo = userService.loginAuthenticate(loginForm);

        if(user_infoVo == null)
            throw new BadCredentialsException("Login Error");

//        user_infoVo.setPassword(null);
//        loginForm.setPassword(null);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 실제로는 DB 등의 여러가지 자원에서 가져와서 권한 정보를 만들어야 한다.
//      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));   // "ROLE_USER" 하지말고 vo.getRole() 이런 식으로.
        authorities.add(new SimpleGrantedAuthority(user_infoVo.getRole()));

        logger.info("LoginForm = " + loginForm);
        logger.info("User_infoVo = " + user_infoVo);

        // 유저 session 생성
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        session.setAttribute("member", user_infoVo);

//      return new UsernamePasswordAuthenticationToken(user_infoVo, null, authorities);
        return new UsernamePasswordAuthenticationToken(user_infoVo, user_infoVo.getPassword(), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}



//    String user_id = authentication.getName();
//    String password = (String) authentication.getCredentials();
//    LoginForm loginForm = new LoginForm();
//
//// vo에 입력한 아이디와 비밀번호 할당
//        loginForm.setUser_id(user_id);
//                loginForm.setPassword(password);