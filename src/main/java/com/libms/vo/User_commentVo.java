package com.libms.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User_commentVo {

    private int comment_num;
    private int board_num;
    private String content;
    private String writer;
    private LocalDateTime reg_datetime;
    private LocalDateTime update_datetime;
    private int comment_origin_no;
    private int group_ord;
    private int origin_no;
    private int indent;
    private String user_ip;
}
