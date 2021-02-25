package com.libms.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User_infoVo {

    private String user_id;
    private String password;
    private String user_name;
    private String user_tel;
    private String user_email;
    private String post_number;
    private String address1;
    private String address2;
    private String join_date;
    private String user_birth;
    private String salt;
    private String role;
    private String authKey;
}
