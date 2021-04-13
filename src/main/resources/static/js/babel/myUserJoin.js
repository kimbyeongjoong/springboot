"use strict";

// 다른 라이브러리, 다른 버전의 jQuery와 충돌 방지하기
var $j = jQuery.noConflict(); // // ajax 통신할 때 헤더에 csrf 적용하여 보내기

var header = $j("meta[name='_csrf_header']").attr("content");
var token = $j("meta[name='_csrf']").attr("content");
$j(document).ajaxSend(function (e, xhr, options) {
  xhr.setRequestHeader(header, token);
}); //필요한 엘리먼트들을 선택한다.

var openButton = document.getElementById("modalOpen");
var modal = document.querySelector(".modal");
var overlay = modal.querySelector(".md_overlay");
var closeButton = modal.querySelector("button"); //동작함수

var openModal = function openModal() {
  emailDuplicateCheck();
};

var closeModal = function closeModal() {
  modal.classList.add("hidden");
}; //클릭 이벤트


openButton.addEventListener("click", openModal);
closeButton.addEventListener("click", closeModal); //아이디 중복 체크

$j("#id").keyup(function () {
  var inputId = document.getElementById('id').value;
  $j.ajax({
    type: 'post',
    url: './idDuplicateCheck',
    dataType: 'text',
    data: {
      id: inputId
    },
    success: function success(data) {
      if (data === "success") {
        $j('#idDuplicateCheck').val("true");
        $j('.idDup').css('display', 'none');
      } else {
        $j('#idDuplicateCheck').val("false");
        $j('.idDup').css('display', 'block');
      }
    },
    error: function error(request, status, _error) {
      alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + _error);
    }
  });
}); //메일 중복 체크

function emailDuplicateCheck() {
  var emailAuth = new Promise(function (resolve, reject) {
    var email1 = document.getElementById('email1').value;
    var email2 = document.getElementById('email2').value;
    var email = email1 + "@" + email2;
    $j.ajax({
      type: 'post',
      url: './emailDuplicateCheck',
      dataType: 'text',
      data: {
        user_email: email
      },
      success: function success(data) {
        if (data === "success") {
          console.log(data);
          resolve(data);
        } else {
          alert('중복된 메일 입니다.');
        }
      },
      error: function error(request, status, _error2) {
        alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + _error2);
      }
    });
  });
  emailAuth.then(function (resolvedData) {
    console.log('resolvedData = ' + resolvedData);

    if (resolvedData === "success") {
      modal.classList.remove("hidden");
      if (!emailAuthenticateConfirm) emailAuthenticate();else alert('이미 인증키가 전송된 상태입니다.');
    }
  });
} //이메일 인증번호 보내기


var emailAuthenticateConfirm = false;

function emailAuthenticate() {
  var email1 = $j("#email1").val();
  var email2 = $j("#email2").val();
  var email = email1 + "@" + email2;
  $j.ajax({
    type: 'post',
    url: './sendAuthMail',
    dataType: 'json',
    data: {
      email: email
    },
    success: function success(data) {
      if (data != null) {
        emailAuthenticateConfirm = true;
        alert("전송이 완료되었습니다.");
      } else {
        alert("인증키 오류입니다.");
        location.reload();
      }
    },
    error: function error(request, status, _error3) {
      alert("code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + _error3);
    }
  });
} // //필요한 엘리먼트들을 선택한다.
// const openButton = document.getElementById("modalOpen");
// const modal = document.querySelector(".modal");
// const overlay = modal.querySelector(".md_overlay");
// const closeButton = modal.querySelector("button");
// //동작함수
// const openModal = () => {
//     modal.classList.remove("hidden");
// }
// const closeModal = () => {
//     modal.classList.add("hidden");
// }
// //클릭 이벤트
// openButton.addEventListener("click", openModal);
// closeButton.addEventListener("click", closeModal);
//우편번호 찾기 화면을 넣을 element


var element_layer = document.getElementById('layer');

function closeDaumPostcode() {
  // iframe을 넣은 element를 안보이게 한다.
  element_layer.style.display = 'none';
} //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.


function sample4_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function oncomplete(data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
      // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var roadAddr = data.roadAddress; // 도로명 주소 변수

      var extraRoadAddr = ''; // 참고 항목 변수
      // 법정동명이 있을 경우 추가한다. (법정리는 제외)
      // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.

      if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
        extraRoadAddr += data.bname;
      } // 건물명이 있고, 공동주택일 경우 추가한다.


      if (data.buildingName !== '' && data.apartment === 'Y') {
        extraRoadAddr += extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName;
      } // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.


      if (extraRoadAddr !== '') {
        extraRoadAddr = ' (' + extraRoadAddr + ')';
      } // 우편번호와 주소 정보를 해당 필드에 넣는다.


      document.getElementById('sample4_postcode').value = data.zonecode;
      document.getElementById("sample4_roadAddress").value = roadAddr;
      document.getElementById('sample4_detailAddress').focus(); // (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)

      element_layer.style.display = 'none'; //	이걸 지울경우 스타일 추가로 만든 border(borderWidth)가 안 사라진다
    },
    width: '100%',
    height: '100%'
  }).embed(element_layer); // iframe을 넣은 element를 보이게 한다.

  element_layer.style.display = 'block'; // iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.

  initLayerPosition();
}

function initLayerPosition() {
  var width = 300; //우편번호 서비스가 들어갈 element의 width

  var height = 480; //우편번호 서비스가 들어갈 element의 height

  var borderWidth = 5; //샘플에서 사용하는 border의 두께
  // 위에서 선언한 값들을 실제 element에 넣는다.

  element_layer.style.width = width + 'px';
  element_layer.style.height = height + 'px';
  element_layer.style.border = borderWidth + 'px solid'; // 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.

  element_layer.style.left = ((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth + 'px';
  element_layer.style.top = ((window.innerHeight || document.documentElement.clientHeight) - height) / 2 - borderWidth + 'px';
}