"use strict";

var $j = jQuery.noConflict(); // // ajax 통신할 때에 헤더에 csrf 적용하여 보내기

var header = $j("meta[name='_csrf_header']").attr("content");
var token = $j("meta[name='_csrf']").attr("content");
$j(document).ajaxSend(function (e, xhr, options) {
  xhr.setRequestHeader(header, token);
});
$j('#summernote').summernote({
  height: 400,
  // 에디터 높이
  minHeight: null,
  // 최소 높이
  maxHeight: null,
  // 최대 높이
  focus: true,
  // 에디터 로딩후 포커스를 맞출지 여부
  lang: "ko-KR",
  // 한글 설정
  placeholder: '내용을 입력해주세요.',
  //placeholder 설정
  codeviewFilter: false,
  codeviewIframeFilter: true,
  callbacks: {
    //여기 부분이 이미지를 첨부하는 부분
    onImageUpload: function onImageUpload(files) {
      console.log('files = ' + files);
      uploadSummernoteImageFile(files[0], this);
    },
    onPaste: function onPaste(e) {
      var clipboardData = e.originalEvent.clipboardData;

      if (clipboardData && clipboardData.items && clipboardData.items.length) {
        var item = clipboardData.items[0];

        if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
          console.log('e = ' + e);
          e.preventDefault();
        }
      }
    }
  }
});
/**
* 이미지 파일 업로드
*/

function uploadSummernoteImageFile(file, editor) {
  console.log("file = " + file);
  console.log("editor = " + editor);
  var data = new FormData();
  data.append("file", file);
  console.log("data = " + data);
  $j.ajax({
    data: data,
    type: "POST",
    url: "uploadSummernoteImageFile",
    contentType: false,
    processData: false,
    success: function success(data) {
      //항상 업로드된 파일의 url이 있어야 한다.
      console.log("data.url = " + data.url);
      $j(editor).summernote('insertImage', data.url);
    },
    error: function error(request, status, _error) {
      alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + _error);
      if (_error != null) location.reload();
    }
  });
} // let writer = sessionStorage.getItem("member");
// console.log("writer = ", writer);
// console.log("writer? = ", writer.user_id);
// 기동은 하는 기능
// let oldVal = document.getElementById("writer");
// console.log( "ready!" );
// // // 세션 아이디
// // //예전 jQuery라면 on이 아니라 bind나 live 
// $j("#writer").change(function() {
//     var currentVal = $j(this).val();
//     if(currentVal == oldVal) {
//         return;
//     }
//     console.log("oldVal = ", oldVal);
//     console.log("currentVal = ", currentVal);
//     oldVal = currentVal;
//     alert("changed!");
// });