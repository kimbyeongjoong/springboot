package com.libms.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JoinConfirmVo {

    private String email;
    private String authKey;

    private String address;
    private String title;
    private String message;
}
