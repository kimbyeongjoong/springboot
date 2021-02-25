package com.libms.util;

import com.libms.vo.User_infoVo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class ConfirmLogin {

    // 로그인 되어있지 않을 경우
    public boolean ConfirmLogin0(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();
        try{
            User_infoVo info = (User_infoVo) session.getAttribute("member");
            if (info == null) {
                System.out.println("info.getUser_id() = " + info.getUser_id());
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.print("<script>");
                out.print("alert('로그인 해주세요.\\n또는 올바르지 않은 접근입니다.');");
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

        HttpSession session = request.getSession();
        try{
            User_infoVo info = (User_infoVo) session.getAttribute("member");
            if(info.getRole().equals("ROLE_ADMIN"))
                return true;
            if (!(info.getUser_id().equals(writer))) {
                System.out.println("info.getUser_id() = " + info.getUser_id());
                System.out.println("writer = " + writer);
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
}