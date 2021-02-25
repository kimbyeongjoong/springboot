package com.libms.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/*")
public class AdminController {

    @GetMapping("admin_index")
    public String admin_index(){
        return "admin/admin_index";
    }
}
