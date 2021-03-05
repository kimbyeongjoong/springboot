package com.libms.service;

import com.libms.mapper.UserMapper;
import com.libms.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserMapper mapper;

    public List<User_infoVo> selectUser(){
        return mapper.selectUser();
    }

    public List<User_boardVo> selectUserBoard(BoardParameterVo boardParameterVo){

        // 페이징
        int page = 0; // 현재 보여주고 있는 페이지
        int limit = 10; // 몇개까지 보여줄 건지 정하는 변수
        String page_temp = boardParameterVo.getPage_temp();
        int list_count = boardParameterVo.getList_count();

        if(page_temp != null)
            page = Integer.parseInt(page_temp);
        else
            page = 1;   // 최초 기본 1페이지 세팅

        int offset = (page - 1) * limit;	// (1 - 1) * 10 = 0
        int limit2 = offset + limit - 1;	//	0 + 10 - 1 = 9

        // 최대 페이지 수
        int maxpage = (int)((double)list_count / limit + 0.95); // 20/10 -> 2+0.95 -> (int)2.95 -> 2
        // 처음 페이지
        int startpage = ((int)((double)page / 10 + 0.9) - 1) * 10 + 1; // 1/10 -> 0.1+0.9 -> 1-1 -> 0*10 -> 0+1 = 1
        // 마지막 페이지
        int endpage = maxpage; // 1 ~ 10까지는 maxpage가 endpage가 됨
        if(endpage > startpage + 10 - 1)
            endpage = startpage + 10 - 1;
        ArrayList<Integer> pages = new ArrayList<>();
        for(int i=startpage; i<=endpage; i++){
            pages.add(i);
        }
        boardParameterVo.setLimit(limit2);
        boardParameterVo.setOffset(offset);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        session.setAttribute("startpage", startpage);
        session.setAttribute("endpage", endpage);
        session.setAttribute("maxpage", maxpage);
        session.setAttribute("pages", pages);

        System.out.println("limit = " + limit);
        System.out.println("limit2 = " + limit2);
        System.out.println("offset = " + offset);
        System.out.println("page_temp = " + page_temp);
        System.out.println("page = " + page);
        System.out.println("list_count = " + list_count);
        System.out.println("startpage = " + startpage);
        System.out.println("endpage = " + endpage);
        System.out.println("maxpage = " + maxpage);

        return mapper.selectUserBoard(boardParameterVo);
    }

    public void userBoardWrite(User_boardVo user_boardVo){
        mapper.userBoardWrite(user_boardVo);
    }
    public void userBoardModifyDo(User_boardVo user_boardVo){
        mapper.userBoardModifyDo(user_boardVo);
    }
    public User_boardVo boardView(int board_num){
        return mapper.boardView(board_num);
    }
    public List<Board_categoryVo> categorySelect(){
        return mapper.categorySelect();
    };
    public void upHits(int board_num){
        mapper.upHits(board_num);
    }
    public void userBoardDelete(int board_num){
        mapper.userBoardDelete(board_num);
    }

    // 로그인
    public User_infoVo loginAuthenticate(LoginForm loginForm){
        // test 코드 참고

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(loginForm.getSalt().getBytes());
            md.update(loginForm.getPassword().getBytes());
            loginForm.setPassword(String.format("%064x", new BigInteger(1, md.digest())));
        }catch (Exception e){
            logger.error("login error = " + e.getMessage());
            e.printStackTrace();
        }

        logger.info("input id = " + loginForm.getUser_id());
        logger.info("input pw = " + loginForm.getPassword());

        return mapper.authenticate(loginForm);
    }

    // salt 값 DB에서 가져오기
    public String getSalt(LoginForm loginForm){
        return mapper.getSalt(loginForm);
    }

    public String writerFindFromBoard(int board_num){
        return mapper.writerFindFromBoard(board_num);
    }
    public void equalsOriginNo(int board_num){
        mapper.equalsOriginNo(board_num);
    }
    public Integer findBoardNum(){
        return mapper.findBoardNum();
    }
    public void writeReply(User_boardVo user_boardVo){
        mapper.writeReply(user_boardVo);
    }
    public void groupUpdate(int origin_no){
        mapper.groupUpdate(origin_no);
    }
    public int listCount(){
        return mapper.listCount();
    };
    public List<User_commentVo> userCommentView(int board_num){
        return mapper.userCommentView(board_num);
    }
    public void userCommentWrite(User_commentVo user_commentVo){
        mapper.userCommentWrite(user_commentVo);
    }
    public void userCommentWrite2(User_commentVo user_commentVo){
        mapper.userCommentWrite2(user_commentVo);
    }
    public void commentDelete(int comment_num){
        mapper.commentDelete(comment_num);
    }
    public void equalsCommentOriginNo(int comment_num){
        mapper.equalsCommentOriginNo(comment_num);
    }
    public Integer findCommentNum(){
        return mapper.findCommentNum();
    }
    public void commentGroupUpdate(int comment_origin_no){
        mapper.commentGroupUpdate(comment_origin_no);
    }
}
