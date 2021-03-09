package com.libms.component;

import com.libms.configuration.CustomUserDetails;
import com.libms.service.CustomUserDetailsService;
import com.libms.service.UserService;
import com.libms.vo.LoginForm;
import com.libms.vo.User_infoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    CustomUserDetailsService customUserDetailsService;

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
        if(loginForm.getSalt() == null)
            loginForm.setSalt("");

        // 로그인 시도 and 계정 정보 가져오기(role 정보를 가져오기 위함)
        User_infoVo user_infoVo = userService.loginAuthenticate(loginForm);

        // 아이디 및 비밀번호가 일치하지 않으면 쿼리 결과는 null 을 반환하게 된다.
        // AuthenticationCredentialNotFoundException : 시스템 문제로 내부 인증 관련 처리 요청을 할 수 없는 경우
        // BadCredentialsException : 비밀번호가 일치하지 않는 경우
        if(user_infoVo == null)
            throw new BadCredentialsException("Login Error");


        // UserDetails 에 DB에 있는 정보 집어넣기
        // UserDetails 를 구현한 CustomUserDetails 에서 만든 변수대로 mapper 에서 가져온 값을 집어넣는다.
        // CustomUserDetails 랑 CustomUserDetailsService 둘 다 중요하다.
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authentication.getName());
        System.out.println("CustomUserDetails = " + userDetails.getUsername() + " and role = " + userDetails.getAuthorities());

        /* checker */
        // UserDetails 에 있는 정보확인.
        // 출처 https://jeong-pro.tistory.com/205
        if(!userDetails.isAccountNonLocked()){
            throw new LockedException("User account is locked");
        }
        if(!userDetails.isEnabled()){
            throw new DisabledException("User is disabled");
        }
        if(!userDetails.isAccountNonExpired()){
            throw new AccountExpiredException("User account has expired");
        }

        // 유저 password 감추기
//        user_infoVo.setPassword(null);
//        loginForm.setPassword(null);

        // ↓ role 정보를 DB에서 가져와서 UsernamePasswordAuthenticationToken 의 3번째 매개변수에 넣는 용도였다.
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 실제로는 DB 등의 여러가지 자원에서 가져와서 권한 정보를 만들어야 한다.
//      authorities.add(new SimpleGrantedAuthority("ROLE_USER"));   // "ROLE_USER" 하지말고 vo.getRole() 이런 식으로.
//        authorities.add(new SimpleGrantedAuthority(user_infoVo.getRole()));

        logger.info("LoginForm = " + loginForm);
        logger.info("User_infoVo = " + user_infoVo);

        // 유저 session 생성
        // 나는 처음에, 여기서 세션을 만들었지만 LoginSuccessHandler 에서 세션을 만드는 것으로 개선하였다.
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        HttpSession session = request.getSession();
//        session.setAttribute("member", user_infoVo);

        // 확인용 sout
        System.out.println("userDetails getUsername = " + userDetails.getUsername());
        System.out.println("userDetails getAuthorities = " + userDetails.getAuthorities());
        System.out.println("authentication.getDetails() = " + authentication.getDetails());

        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        result.setDetails(authentication.getDetails());
//        return new UsernamePasswordAuthenticationToken(user_infoVo, null, authorities);
//        return new UsernamePasswordAuthenticationToken(user_infoVo, user_infoVo.getPassword(), authorities);
        return result;
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