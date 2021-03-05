package com.libms.controller;

import com.libms.service.UserService;
import com.libms.vo.LoginForm;
import com.libms.vo.User_infoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @RequestMapping("/")
    public String index() throws Exception{
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(@ModelAttribute("loginForm")LoginForm loginForm, Model model){
        return "login";
    }

    @GetMapping("/authenticated_info")
    public String authenticated_info(HttpSession session){
        return "authenticated_info";
    }

    @GetMapping("user_info/user_info")
    public String user_info(Model model){
        List<User_infoVo> user_info_list = userService.selectUser();
        model.addAttribute("user_info_list", user_info_list);

        return "user_info/user_info";
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
