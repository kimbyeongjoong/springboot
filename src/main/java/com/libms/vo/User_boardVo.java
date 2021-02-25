package com.libms.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class User_boardVo {

    private int board_num;
    private String title;
    private String content;
    private String writer;
    private int hits;
    private LocalDateTime reg_datetime;
    private LocalDateTime update_datetime;
    private int origin_no;
    private int group_ord;
    private String user_ip;
    private String category;

}
