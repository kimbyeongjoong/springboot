package com.libms.mapper;

import com.libms.configuration.CustomUserDetails;
import com.libms.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    UserDetails selectUser(String user_id);
    List<User_boardVo> selectUserBoard(BoardParameterVo boardParameterVo);
    void userBoardWrite(User_boardVo user_boardVo);
    void userBoardModifyDo(User_boardVo user_boardVo);
    User_boardVo boardView(int board_num);
    List<Board_categoryVo> categorySelect();
    void upHits(int board_num);
    void userBoardDelete(int board_num);
    User_infoVo authenticate(LoginForm loginForm);
    String getSalt(LoginForm loginForm);
    String writerFindFromBoard(int board_num);
    Integer findBoardNum();
    void equalsOriginNo(int board_num);
    void groupUpdate(int origin_no);
    void writeReply(User_boardVo user_boardVo);
    int listCount();
    List<User_commentVo> userCommentView(int board_num);
    void userCommentWrite(User_commentVo user_commentVo);
    void userCommentWrite2(User_commentVo user_commentVo);
    void commentDelete(int comment_num);
    void equalsCommentOriginNo(int comment_num);
    Integer findCommentNum();
    void commentGroupUpdate(int comment_origin_no);
}
