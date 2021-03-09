package com.libms.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardParameterVo {

    private int limit;
    private int offset;
    private String search_word;
    private String page;
    private int list_count;
}
