package com.libms.util;

import com.libms.configuration.CustomUserDetails;
import com.libms.service.CustomUserDetailsService;
import com.libms.service.UserService;
import com.libms.vo.User_infoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ConfirmLogin {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    // 로그인 되어있지 않을 경우
    public boolean ConfirmLoginExpired(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal).getUsername();
//        } else {
//            username = principal.toString();
//        }
//
//        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        System.out.println("principal = " + principal.getClass());
        System.out.println("username = " + username);
//        System.out.println("CustomUserDetails = " + userDetails.getUser_id() + " and role = " + userDetails.getRole());

        try{
            if (principal == null) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print("<script>");
                out.print("alert('잘못된 접근입니다.');");
                out.print("location.href = '../login';");
                out.print("</script>");
                out.flush();
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("<script>");
            out.print("alert('올바르지 않은 접근입니다.');");
            out.print("location.href = '../';");
            out.print("</script>");
            out.flush();
            return false;
        }
        return true;
    }

    // 아이디가 일치하지 않을 경우
    public boolean ConfirmLogin(
            HttpServletRequest request,
            HttpServletResponse response,
            String writer) throws IOException {
        String username = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);

        System.out.println("principal = " + principal.getClass());
        System.out.println("writer = " + writer);
        System.out.println("username = " + username);
        System.out.println("CustomUserDetails = " + userDetails.getUser_id() + " and role = " + userDetails.getRole());

//        HttpSession session = request.getSession();
//        System.out.println("session = " + session);
        // CustomUserDetails 를 이용하여 작성자의 유효성을 체크 한다.
        // 전에는 세션으로 비교하여 체크하였지만 spring security 에서는 CustomUserDetails 로 권한정보를 조회하고
        // principal 객체의 유저네임 값과 파라미터로 온 값을 비교하여 체크한다.
        try{
            if(userDetails.getRole().equals("ROLE_ADMIN"))
                return true;
            if (!username.equals(writer)) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print("<script>");
                out.print("alert('아이디를 확인해주세요.\\n올바르지 않은 접근입니다.');");
                out.print("location.href = '../';");
                out.print("</script>");
                out.flush();
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("<script>");
            out.print("alert('올바르지 않은 접근입니다.');");
            out.print("location.href = '../';");
            out.print("</script>");
            out.flush();
            return false;
        }
        return true;
    }


//    // 로그인 되어있지 않을 경우
//    public boolean ConfirmLoginExpired(
//            HttpServletRequest request,
//            HttpServletResponse response) throws IOException {
//
//        HttpSession session = request.getSession();
//        System.out.println("session = " + session);
//        try{
//            User_infoVo info = (User_infoVo) session.getAttribute("member");
//            if (info == null) {
//                response.setContentType("text/html; charset=UTF-8");
//                PrintWriter out = response.getWriter();
//                out.print("<script>");
//                out.print("alert('세션이 만료되었습니다.');");
//                out.print("location.href = '../login';");
//                out.print("</script>");
//                out.flush();
//                return false;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//            response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.print("<script>");
//            out.print("alert('올바르지 않은 접근입니다.');");
//            out.print("location.href = '../';");
//            out.print("</script>");
//            out.flush();
//            return false;
//        }
//        return true;
//    }
//    public boolean ConfirmLogin(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            String writer) throws IOException {
//
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println("principal = " + principal);
//        System.out.println("writer = " + writer);
////        CustomUserDetails userDetails = (CustomUserDetails) loadUserByUsername(writer);
////        System.out.println("CustomUserDetails = " + userDetails.getUser_id() + " and role = " + userDetails.getRole());
//
////        HttpSession session = request.getSession();
////        System.out.println("session = " + session);
//        // CustomUserDetails 를 이용하여 작성자의 유효성을 체크 한다.
//        // 전에는 세션으로 비교하여 체크하였지만 spring security 에서는 CustomUserDetails
//        try{
////            if(userDetails.getRole().equals("ROLE_ADMIN"))
////                return true;
////            if (!(userDetails.getUser_id().equals(writer))) {
////                response.setContentType("text/html; charset=UTF-8");
////                PrintWriter out = response.getWriter();
////                out.print("<script>");
////                out.print("alert('아이디를 확인해주세요.\\n올바르지 않은 접근입니다.');");
////                out.print("location.href = '../';");
////                out.print("</script>");
////                out.flush();
////                return false;
////            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            response.setContentType("text/html; charset=UTF-8");
//            PrintWriter out = response.getWriter();
//            out.print("<script>");
//            out.print("alert('올바르지 않은 접근입니다.');");
//            out.print("location.href = '../';");
//            out.print("</script>");
//            out.flush();
//            return false;
//        }
//        return true;
//    }

}