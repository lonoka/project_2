<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- 로그인 -->
<div class="modal fade" id="login_modal" tabindex="-1" role="dialog"
	aria-labelledby="loginLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="loginLabel">
					<span class="h2 mb-0">CA<span class="text-primary">.</span>
					</span>Login
				</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<div class="modal-body">
				<form class="user" action="Loginbtn.do" method="post">
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							name="userId" placeholder="ID" required="required">
					</div>
					<div class="form-group">
						<input type="password" class="form-control form-control-user"
							name="userPassword" placeholder="Password"
							style="ime-mode: disabled;" required="required">
					</div>
					<div class="form-group">
						<div class="custom-control custom-checkbox small">
							<input type="checkbox" class="custom-control-input"
								id="customCheck" name="userCheck"> <label
								class="custom-control-label" for="customCheck">ID 기억하기</label>
						</div>
					</div>
					<button type="submit" class="btn btn-primary btn-user btn-block"
						style="color: dodgerblue;">Login</button>
				</form>
			</div>
			<div class="modal-footer">
				<div class="" style="margin: auto;">
					<div class="text-center">
						<a class="small" href="javascript:void(0)" data-dismiss="modal"
							data-toggle="modal" data-target="#sign_modal"
							data-backdrop="static">회원가입</a>
					</div>
					<div class="text-center">
						<a class="small" href="javascript:void(0)" data-dismiss="modal"
							data-toggle="modal" data-target="#find_modal"
							data-backdrop="static">아이디/비밀번호 찾기</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 회원가입 -->
<div class="modal fade" id="sign_modal" tabindex="-1" role="dialog"
	aria-labelledby="signLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="signLabel">Sign up</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close" onclick="reset_form()">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<form id="reg_form" name="f" class="user" action="/user/insertUserInfo.do"
				method="post" onsubmit="return doRegUserCkeck(this);">
				<div class="modal-body">
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							name="user_id" oninput="checkId()" placeholder="ID"
							required="required">
						<div id="wrongId"
							style="display: none; color: red; font-size: 12px;">5~16자의
							영문 소문자,숫자만 사용 가능합니다.</div>
						<div id="failId"
							style="display: none; color: red; font-size: 12px;">사용하실 수
							없는 아이디입니다.</div>
						<div id="successId"
							style="display: none; color: saddlebrown; font-size: 12px;">사용하실
							수 있는 아이디입니다.</div>
					</div>
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							name="user_name" placeholder="이름" required="required">
					</div>
					<div class="form-group row">
						<div class="col-sm-6 mb-3 mb-sm-0">
							<input type="password" class="form-control form-control-user"
								name="password" maxlength="16" oninput="checkPw()"
								placeholder="비밀번호" style="ime-mode: disabled;"
								required="required">
						</div>
						<div class="col-sm-6">
							<input type="password" class="form-control form-control-user"
								name="RepeatPassword" oninput="checkPw2()"
								placeholder="비밀번호 재입력" style="ime-mode: disabled;"
								required="required">
						</div>
						<div id="wrongPw"
							style="display: none; color: red; font-size: 12px; margin-left: 20px;">6~16자의
							비밀번호를 사용해주세요.</div>
						<div id="wrongPw2"
							style="display: none; color: red; font-size: 12px; margin-left: 20px;">비밀번호가
							일치하지 않습니다.</div>
					</div>
					<div class="form-group">
						<input type="email" class="form-control form-control-user"
							name="user_mail" oninput="checkMail()" placeholder="Email"
							required="required">
						<div id="wrongMail"
							style="display: none; color: red; font-size: 12px;">잘못된 이메일
							형식입니다.</div>
						<div id="successMail"
							style="display: none; color: saddlebrown; font-size: 12px;">사용
							가능한 Email입니다.</div>
						<div id="failMail"
							style="display: none; color: red; font-size: 12px;">이미 등록된
							Email입니다.</div>
					</div>
					<button id="submitbtn" type="submit"
						class="btn btn-primary btn-user btn-block">회원 가입</button>

				</div>
				<div class="modal-footer">
					<div class="" style="margin: auto;">
						<div class="text-center">
							<a class="small" href="javascript:void(0)" data-dismiss="modal"
								data-toggle="modal" data-target="#login_modal"
								data-backdrop="static">로그인</a>
						</div>
						<div class="text-center">
							<a class="small" href="javascript:void(0)" data-dismiss="modal"
								data-toggle="modal" data-target="#find_modal"
								data-backdrop="static">아이디/비밀번호 찾기</a>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<!-- 아이디찾기 -->
<div class="modal fade" id="find_modal" tabindex="-1" role="dialog"
	aria-labelledby="findLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="findLabel">Find</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<div class="modal-body">
				<div class="col-lg-12">
					<div class="text-center">
						<h1 class="h4 text-gray-900 mb-2">아이디/비밀번호 찾기</h1>
						<p class="mb-4">회원가입시 등록한 정보를 입력하여 아이디/비밀번호를 찾을 수 있습니다. 등록한
							정보가 기억나지 않는다면 고객문의를 이용해주세요.</p>
					</div>
					<form name="findID" method="post" class="user" action="/findID.do"
						onsubmit="return checkID(this)">
						<div class="form-group">
							<input type="text" class="form-control form-control-user"
								name="user_name" placeholder="이름" required="required">
						</div>
						<div class="form-group">
							<input type="email" class="form-control form-control-user"
								name="user_mail" placeholder="Email" required="required">
						</div>
						<button type="submit" class="btn btn-primary btn-user btn-block">아이디
							찾기</button>
					</form>
					<hr>
					<form name="findPW" method="post" class="user" action="/findPW.do"
						onsubmit="return checkPW(this)">
						<div class="form-group">
							<input type="text" class="form-control form-control-user"
								name="user_name" placeholder="이름" required="required">
						</div>
						<div class="form-group">
							<input type="text" class="form-control form-control-user"
								name="user_id" placeholder="아이디" required="required">
						</div>
						<div class="form-group">
							<input type="email" class="form-control form-control-user"
								name="user_mail" placeholder="Email" required="required">
						</div>
						<button type="submit" class="btn btn-primary btn-user btn-block">비밀번호
							찾기</button>
					</form>
				</div>
			</div>
			<div class="modal-footer">
				<div class="" style="margin: auto;">
					<div class="text-center">
						<a class="small" href="javascript:void(0)" data-dismiss="modal"
							data-toggle="modal" data-target="#login_modal"
							data-backdrop="static">로그인</a>
					</div>
					<div class="text-center">
						<a class="small" href="javascript:void(0)" data-dismiss="modal"
							data-toggle="modal" data-target="#sign_modal"
							data-backdrop="static">회원가입</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	window.onload = function() {
		var userInputId = getCookie("userInputId");//저장된 쿠기값 가져오기
		$('input[name=userId]').val(userInputId);

		if ($('input[name=userId]').val() != "") { // 그 전에 ID를 저장해서 처음 페이지 로딩
			// 아이디 저장하기 체크되어있을 시,
			$('input[name=userCheck]').attr("checked", true); // ID 저장하기를 체크 상태로 두기.
		}

		$('input[name=userCheck]').change(function() { // 체크박스에 변화가 발생시
			if ($('input[name=userCheck]').is(":checked")) { // ID 저장하기 체크했을 때,
				var userInputId = $('input[name=userId]').val();
				setCookie("userInputId", userInputId, 7); // 7일 동안 쿠키 보관
			} else { // ID 저장하기 체크 해제 시,
				deleteCookie("userInputId");
			}
		});

		// ID 저장하기를 체크한 상태에서 ID를 입력하는 경우, 이럴 때도 쿠키 저장.
		$('input[name=userId]').keyup(function() { // ID 입력 칸에 ID를 입력할 때,
			if ($('input[name=userCheck]').is(":checked")) { // ID 저장하기를 체크한 상태라면,
				var userInputId = $('input[name=userId]').val();
				setCookie("userInputId", userInputId, 7); // 7일 동안 쿠키 보관
			}
		});
	};

	function setCookie(cookieName, value, exdays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var cookieValue = escape(value)
				+ ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
		document.cookie = cookieName + "=" + cookieValue;
	}

	function deleteCookie(cookieName) {
		var expireDate = new Date();
		expireDate.setDate(expireDate.getDate() - 1);
		document.cookie = cookieName + "= " + "; expires="
				+ expireDate.toGMTString();
	}

	function getCookie(cookieName) {
		cookieName = cookieName + '=';
		var cookieData = document.cookie;
		var start = cookieData.indexOf(cookieName);
		var cookieValue = '';
		if (start != -1) {
			start += cookieName.length;
			var end = cookieData.indexOf(';', start);
			if (end == -1)
				end = cookieData.length;
			cookieValue = cookieData.substring(start, end);
		}
		return unescape(cookieValue);
	}
</script>
<script>
	var idCheck = 0;
	var pwCheck = 0;
	var emailCheck = 0;
	function reset_form(){
		$("#reg_form")[0].reset();
		$('#wrongId').hide();
		$('#failId').hide();
		$('#successId').hide();
		$('#wrongPw').hide();
		$('#wrongPw2').hide();
		$('#successMail').hide();
		$('#failMail').hide();
		$('#wrongMail').hide();
		
	}
	function checkId() {
		var inputed = f.user_id.value;
		var CheckForm = /^[a-z0-9]{5,16}$/;
		if (!CheckForm.test(inputed)) {
			$('#wrongId').show();
			$('#failId').hide();
			$('#successId').hide();
			idCheck = 0;
		} else {
			$.ajax({
				data : {
					user_id : inputed
				},
				url : "checkId.do",
				success : function(data) {
					if (inputed == "" && data == '0') {
						$('#wrongId').hide();
						$('#failId').show();
						$('#successId').hide();
						idCheck = 0;
					} else if (data == '0') {
						$('#wrongId').hide();
						$('#failId').hide();
						$('#successId').show();
						idCheck = 1;
					} else if (data == '1') {
						$('#wrongId').hide();
						$('#failId').show();
						$('#successId').hide();
						idCheck = 0;
					}
				}
			})
		}
	}
	function checkPw() {
		var inputed = f.password.value;
		if (inputed.length < 6) {
			$('#wrongPw').show();
			$('#wrongPw2').hide();
		} else {
			$('#wrongPw').hide();
			$('#wrongPw2').hide();
		}
	}
	function checkPw2() {
		var inputed = f.password.value;
		var inputed2 = f.RepeatPassword.value;
		if (inputed != inputed2) {
			$('#wrongPw2').show();
		} else {
			$('#wrongPw2').hide();
		}
	}
	function checkMail() {
		var inputed = f.user_mail.value;
		var CheckForm = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;

		if (!CheckForm.test(inputed)) {
			$('#successMail').hide();
			$('#failMail').hide();
			$('#wrongMail').show();
			emailCheck = 0;
		} else {
			$.ajax({
				data : {
					user_mail : inputed
				},
				url : "checkMail.do",
				success : function(data) {
					if (inputed == "" && data == '0') {
						$('#successMail').hide();
						$('#failMail').show();
						$('#wrongMail').hide();
						emailCheck = 0;
					} else if (data == '0') {
						$('#successMail').show();
						$('#failMail').hide();
						$('#wrongMail').hide();
						emailCheck = 1;
					} else if (data == '1') {
						$('#successMail').hide();
						$('#failMail').show();
						$('#wrongMail').hide();
						emailCheck = 0;
					}
				}
			})
		}
	}
	function doRegUserCkeck(f) {
		if (idCheck == 0) {
			alert("사용하실수 없는 아이디 입니다.");
			return false;
		}
		if (emailCheck == 0) {
			alert("사용하실수 없는 이메일 입니다.");
			return false;
		}
		if (f.password.value.length < 6) {
			alert("6글자 이상의 비밀번호를 사용해주세요.");
			return false;
		}
		if (f.user_id.value == "") {
			alert("아이디를 입력하세요.");
			f.user_id.focus();
			return false;
		}
		if (f.user_name.value == "") {
			alert("이름을 입력하세요.");
			f.user_name.focus();
			return false;
		}
		if (f.password.value == "") {
			alert("비밀번호를 입력하세요.");
			f.password.focus();
			return false;
		}
		if (f.RepeatPassword.value == "") {
			alert("비밀번호 재입력을 입력하세요.");
			f.RepeatPassword.focus();
			return false;
		}
		if (f.user_mail.value == "") {
			alert("Email을 입력하세요.");
			f.user_mail.focus();
			return false;
		}
		if (f.password.value != f.RepeatPassword.value) {
			alert("비밀번호가 같지 않습니다.");
			f.RepeatPassword.focus();
			return false;
		}
	}
</script>
<script>
	function checkID(findID) {
		if (findID.user_name.value == "") {
			alert("이름을 입력하세요.");
			findID.user_name.focus();
			return false;
		}
		if (findID.user_date.value == "") {
			alert("생년월일을 입력하세요.");
			findID.user_date.focus();
			return false;
		}
		if (findID.user_mail.value == "") {
			alert("Email을 입력하세요.");
			findID.user_mail.focus();
			return false;
		}
	}

	function checkPW(findPW) {
		if (findPW.user_name.value == "") {
			alert("이름을 입력하세요.");
			findPW.user_name.focus();
			return false;
		}
		if (findPW.user_id.value == "") {
			alert("아이디를 입력하세요.");
			findPW.user_id.focus();
			return false;
		}
		if (findPW.user_date.value == "") {
			alert("생년월일을 입력하세요.");
			findPW.user_date.focus();
			return false;
		}
		if (findPW.user_mail.value == "") {
			alert("Email을 입력하세요.");
			findPW.user_mail.focus();
			return false;
		}
	}
</script>