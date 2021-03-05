package com.libms.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginForm {

    private String user_id;
    private String password;
    private String salt;
    private String loginError;
}
