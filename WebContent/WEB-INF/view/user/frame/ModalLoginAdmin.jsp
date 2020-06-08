<%@page import="poly.util.EncryptUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- mypage -->
<div class="modal fade" id="mypage_modal" tabindex="-1" role="dialog"
	aria-labelledby="mypageLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="mypageLabel">Mypage</h4>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form name="f" action="updateUserInfo.do"
				onsubmit="return doModifyUserCkeck(this)" method="post">
				<div class="modal-body">
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							name="user_id" placeholder="ID" value="<%=uDTO.getUser_id()%>"
							readonly required="required">
					</div>
					<div class="form-group">
						<input type="text" class="form-control form-control-user"
							name="user_name" placeholder="이름"
							value="<%=uDTO.getUser_name()%>" required="required">
					</div>
					<div class="form-group row">
						<div class="col-sm-6 mb-3 mb-sm-0">
							<input type="password" class="form-control form-control-user"
								name="password" maxlength="16" placeholder="비밀번호"
								style="ime-mode: disabled;" required="required">
						</div>
						<div class="col-sm-6">
							<input type="password" class="form-control form-control-user"
								name="RepeatPassword" placeholder="비밀번호 재입력"
								style="ime-mode: disabled;" required="required">
						</div>
					</div>
					<div class="form-group">
						<input type="email" class="form-control form-control-user"
							name="user_mail" placeholder="Email"
							value="<%=EncryptUtil.decAES128CBC(uDTO.getUser_mail())%>" readonly required="required">
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="submit" class="btn btn-primary"
							style="color: dodgerblue;">회원정보 수정</button>
					</div>

				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">
	function doModifyUserCkeck(f) {
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
		if (f.user_date.value == "") {
			alert("생년월일을 입력하세요.");
			f.user_date.focus();
			return false;
		}
		if (f.password.value != f.RepeatPassword.value) {
			alert("비밀번호가 같지 않습니다.");
			f.RepeatPassword.focus();
			return false;
		}
	}
</script>