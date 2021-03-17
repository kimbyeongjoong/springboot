// 다른 라이브러리, 다른 버전의 jQuery와 충돌 방지하기
let $j = jQuery.noConflict();
// ajax 통신할 때 헤더에 csrf 적용하여 보내기
let header = $j("meta[name='_csrf_header']").attr("content");
let token = $j("meta[name='_csrf']").attr("content");

$j(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
});

// 댓글 indent 값으로 css구분하기
let comment_indent = document.querySelectorAll('.commentOneBox');
// 반복문으로 indent(댓글 깊이)를 각각 구분한다.
for(let i = 0; i < comment_indent.length; i++){
    // 댓글의 data속성을 참조해서 변수에 담는다.
    let result = comment_indent[i].getAttribute('data-indent');
    // indent가 2이상인 것은 전부 대댓글이므로 comment_reply 클래스를 추가(add)한다.
    if(result > 1)
        comment_indent[i].classList.add("comment_reply");
}

//댓글등록
function comment_write(board_num, writer){
    $j('#comment_board_num').val(board_num);
    $j('#comment_writer').val(writer);
    let param = $j('#commentForm').serialize();
    $j.ajax({
            type : 'post',
            url : './userCommentWrite',
            dataType : 'text',
            data : param,
            success : function(data){
                if(data != 'success')
                    alert('세션이 만료되었습니다.');
                alert('success');
                console.log('ajax success = ', data);
                location.reload();
            },
            error:function(request, status, error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                if(error != null)
                    location.reload();
            }
    });

}

// 대댓글 창 열기
let comment_num_temp;
function nested_comment_open(comment_num){
    $j('#commentWriteDiv' + comment_num_temp).css("display","none"); // 먼저 열린 창이 있다면 닫기
    $j('#commentWriteDiv').hide(); // 일반 댓글 작성 창 닫기
    $j('#commentWriteDiv' + comment_num).css("display","block"); // 대댓글 작성 창 열기
    comment_num_temp = comment_num; // 대댓글 창 저장
}

//대댓글등록
function nested_comment_write(board_num, comment_num, comment_origin_no, writer){
    $j('#comment_board_num' + comment_num).val(board_num);
    $j('#comment_num' + comment_num).val(comment_num);
    $j('#comment_origin_no' + comment_num).val(comment_origin_no);
    $j('#comment_writer' + comment_num).val(writer);
    console.log("content = " + document.getElementById('comment_content' + comment_num).value);
    let param = new Object();
    param = $j('#commentForm' + comment_num).serialize();
    $j.ajax({
            type : 'post',
            url : './userCommentWrite2',
            dataType : 'text',
            data : param,
            success : function(data){
                location.reload();
            },
            error:function(request, status, error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                if(error != null)
                    location.reload();
            }
    });
}

// 대댓글 창 없애기
function nested_comment_cansel(comment_num){
    $j('#commentWriteDiv').show();  // 일반 댓글 작성 창 열기
    $j('#commentWriteDiv' + comment_num).css("display","none"); // 대댓글 창 닫기
}

//댓글삭제
function comment_delete(comment_num, writer){
    let result = confirm(comment_num + '번 게시물을 삭제하시겠습니까? 작성자 : ' + writer);
    if(!result){
        alert('삭제 취소');
        return false;
    }
    $j.ajax({
            type : 'post',
            url : './commentDelete',
            dataType : 'text',
            data : {
                comment_num : comment_num,
                writer : writer
            },
            success : function(data){
                location.reload();
            },
            error:function(request, status, error){
                alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
                if(error != null)
                    location.reload();
            }
    });
}