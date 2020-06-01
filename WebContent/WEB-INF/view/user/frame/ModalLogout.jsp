<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="modal fade" id="login_modal" tabindex="-1" role="dialog"
	aria-labelledby="loginLabel" aria-hidden="true">
	<div class="modal-dialog">
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
						<a class="small" href="javascript:void(0)">회원가입</a>
					</div>
					<div class="text-center">
						<a class="small" href="javascript:void(0)">아이디/비밀번호 찾기</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="Sign_modal" tabindex="-1" role="dialog"
	aria-labelledby="signLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="signLabel">Sign up</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>

			</div>
			<div class="modal-body">...</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary">Save changes</button>
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