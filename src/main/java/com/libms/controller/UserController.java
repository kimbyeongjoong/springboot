package com.libms.controller;

import com.google.gson.JsonObject;
import com.libms.service.UserService;
import com.libms.util.ConfirmLogin;
import com.libms.vo.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("user/*")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;

    @Autowired
    ConfirmLogin confirmLogin;

    @GetMapping("user_board")
    public String board_view(
            @ModelAttribute BoardParameterVo boardParameterVo,
            HttpServletRequest request,
            Model model){

        int list_count = userService.listCount();
//        String page_temp = request.getParameter("page");
//        String search_word = request.getParameter("search_word");
//        BoardParameterVo boardParameterVo = new BoardParameterVo();
//        boardParameterVo.setPage_temp(page_temp);
//        boardParameterVo.setSearch_word(search_word);
        boardParameterVo.setList_count(list_count);
        List<User_boardVo> user_board_list = userService.selectUserBoard(boardParameterVo);
        HttpSession session = request.getSession();
        int startpage = (int) session.getAttribute("startpage");
        int endpage = (int) session.getAttribute("endpage");
        int maxpage = (int) session.getAttribute("maxpage");
        ArrayList<Integer> pages = (ArrayList<Integer>) session.getAttribute("pages");
        session.removeAttribute("startpage");
        session.removeAttribute("endpage");
        session.removeAttribute("maxpage");
        session.removeAttribute("pages");
        model.addAttribute("user_board_list", user_board_list);
        model.addAttribute("startpage", startpage);
        model.addAttribute("endpage", endpage);
        model.addAttribute("maxpage", maxpage);
        model.addAttribute("pages", pages);

        return "user/user_board";
    }

    @GetMapping("user_board_writing")
    public String board_write(Model model){

        List<Board_categoryVo> category = userService.categorySelect();
        model.addAttribute("category", category);

        return "user/user_board_writing";
    }

    @PostMapping("user_board_write")
    public String user_board_write(
            @ModelAttribute User_boardVo board_info,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLogin(request, response, board_info.getWriter());
        if(!result)
            return "";

        // 글 작성 쿼리
//        board_info.setUser_ip(request.getRemoteAddr());
        board_info.setUser_ip("192.168.0.4");
        userService.userBoardWrite(board_info);
        // null 이 들어오는 경우가 있기 때문에 Integer 사용
        Integer max_board = userService.findBoardNum();
        // 글이 하나도 없을 경우 board_num = 1, 있을 경우 board_num = max_board
        int board_num = max_board != null ? max_board : 1;
        userService.equalsOriginNo(board_num);
        logger.info("get board = " + board_info);

        return "redirect:user_board";
    }

    @PostMapping(value = "uploadSummernoteImageFile", produces = "application/json")
    @ResponseBody
    public JsonObject uploadSummernoteImageFile(@RequestParam("file") MultipartFile multipartFile) {

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\summernote_image\\";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        logger.info("fileRoot = " + fileRoot);
        logger.info("originalFileName = " + originalFileName);
        logger.info("extension = " + extension);
        logger.info("savedFileName = " + savedFileName);
        logger.info("targetFile = " + targetFile);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/summernote_image/" + savedFileName);
            jsonObject.addProperty("responseCode", "success");
            logger.info("fileStream = " + fileStream);
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
    }


    @GetMapping("user_board_view")
    public String user_board_view(
            Model model,
            HttpServletRequest request){

        // 본문 출력
        int board_num = Integer.parseInt(request.getParameter("board_num"));
        User_boardVo board = userService.boardView(board_num);
        userService.upHits(board_num);

        // 댓글 출력
        List<User_commentVo> comment = userService.userCommentView(board_num);

        model.addAttribute("board", board);
        model.addAttribute("comment_list", comment);

        return "user/user_board_view";
    }

    @GetMapping("user_board_modify")
    public String user_board_modify(
            Model model,
            HttpServletRequest request){

        int board_num = Integer.parseInt(request.getParameter("board_num"));
        User_boardVo board = userService.boardView(board_num);
        List<Board_categoryVo> category = userService.categorySelect();
        model.addAttribute("category", category);
        model.addAttribute("board", board);

        return "user/user_board_modify";
    }

    @PostMapping("user_board_modifyDo")
    public String user_board_modifyDo(
            @ModelAttribute User_boardVo board_info,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLogin(request, response, board_info.getWriter());
        if(!result)
            return "";

        // 글 수정 쿼리
        board_info.setUser_ip(request.getRemoteAddr());
        userService.userBoardModifyDo(board_info);
        logger.info("get modify title = " + request.getParameter("title"));

        return "redirect:user_board";
    }

    @GetMapping("user_board_delete")
    public String user_board_delete(
            @RequestParam("board_num")String board_num,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // 유저 세션에서 가져오는 정보 관리
        int board_number = Integer.parseInt(board_num);
        String writer = userService.writerFindFromBoard(board_number); // 게시물의 유저 아이디

        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLogin(request, response, writer);
        if(!result)
            return "";

        userService.userBoardDelete(board_number);
        logger.info("board delete number = " + board_number);

        return "redirect:user_board";
    }

    @GetMapping("user_board_reply")
    public String writeReplyPage(
            @RequestParam("board_num")String board_number,
            @RequestParam("title")String title,
            Model model){
        int board_num = Integer.parseInt(board_number);
        User_boardVo board = userService.boardView(board_num);
        model.addAttribute("board", board);
        return "user/user_board_reply";
    }

    @PostMapping("write_reply_do")
    public String writeReplyDo(
            @ModelAttribute User_boardVo board_info,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLogin(request, response, board_info.getWriter());
        if(!result)
            return "";

        // 쿼리 실행 & 로그
        board_info.setUser_ip(request.getRemoteAddr());
        userService.groupUpdate(board_info.getOrigin_no());
        userService.writeReply(board_info);
        logger.info("get reply board = " + board_info);
        return "redirect:user_board";
    }

    @PostMapping("userCommentWrite")
    @ResponseBody
    public String userCommentWrite(
            @ModelAttribute User_commentVo user_commentVo,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLoginExpired(request, response);
        if(!result)
            return "";
        // 본문에 댓글 달기
//      board_info.setUser_ip(request.getRemoteAddr());
        user_commentVo.setUser_ip("192.168.0.4");
        userService.userCommentWrite(user_commentVo);
        // null 이 들어오는 경우가 있기 때문에 Integer 사용
        Integer max_comment = userService.findCommentNum();
        // 글이 하나도 없을 경우 board_num = 1, 있을 경우 board_num = max_board
        int comment_num = max_comment != null ? max_comment : 1;
        userService.equalsCommentOriginNo(comment_num);
        logger.info("user_commentVo = " + user_commentVo);

        return "success";
    }

    @PostMapping("userCommentWrite2")
    @ResponseBody
    public String userCommentWrite2(
            @ModelAttribute User_commentVo user_commentVo,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLoginExpired(request, response);
        if(!result)
            return "";
        // 대댓글 달기
//      user_commentVo.setUser_ip(request.getRemoteAddr());
        user_commentVo.setUser_ip("192.168.0.4");
        userService.commentGroupUpdate(user_commentVo.getComment_origin_no());
        userService.userCommentWrite2(user_commentVo);
        logger.info("user_commentVo = " + user_commentVo);

        return "success";
    }

    @PostMapping("commentDelete")
    @ResponseBody
    public String commentDelete(
            @ModelAttribute User_commentVo user_commentVo,
            HttpServletRequest request,
            HttpServletResponse response)throws IOException{

        // 로그인 상태가 올바른지 확인
        boolean result = confirmLogin.ConfirmLogin(request, response, user_commentVo.getWriter());
        if(!result)
            return "error";

        userService.commentDelete(user_commentVo.getComment_num());
        logger.info("comment delete number = " + user_commentVo.getComment_num());
        logger.info("comment delete writer = " + user_commentVo.getWriter());
        return "success";
    }

}
