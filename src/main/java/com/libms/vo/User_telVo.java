package com.libms.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User_telVo {

    public User_telVo(){
        this.sns_agree = "y";
    }

    private String user_tel;
    private String sns_agree;
}
