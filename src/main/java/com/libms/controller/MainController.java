package com.libms.controller;

import com.libms.vo.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping("/")
    public String index() throws Exception{
        return "index";
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm")LoginForm loginForm, Model model){
        return "login";
    }

    @GetMapping("/authenticated_info")
    public String authenticated_info(HttpSession session){
        session.setAttribute("sessionTest", "Y");
        return "authenticated_info";
    }

    @GetMapping("/user_join")
    public String user_join_view(){
        return "user_join";
    }

    @GetMapping("/mail_send")
    public String mail_send(){
        return "mail_send";
    }

    // test
    @RequestMapping("/navtop")
    public String navtop(){
        return "common/navtop";
    }

    @RequestMapping("/sidenavbar")
    public String sidenavbar(){
        return "common/sidenavbar";
    }

}
