<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Weather Info</title>
<script src="/js/annyang.js"></script>
<script src="/js/jquery-3.5.0.min.js"></script>
<script>
	//annyang라이브러리 실행
	annyang.start({
		autoRestart : true,
		continuous : true
	})
	
	//음성인식 값 받아오기위한 객체 생성
	var recognition = annyang.getSpeechRecognizer();
	
	//최종 음성인식 결과값 저장 변수
	var final_transcript = "";
	
	//말하는 동안에 인식되는 값 가져오기(x)
	recognition.interimResults = false;
	
	//음성 인식결과 가져오기
	recognition.onresult = function(event){
		var interim_transcript = "";
		final_transcript = "";
		for(var i = event.resultIndex; i<event.results.length ; i++){
			if(event.results[i].isFinal) {
				final_transcript += event.results[i][0].transcript;
			}
			$("#view_msg").html(final_transcript);
			$("#send_msg").val(final_transcript);
		}
		$.ajax({
			url : "/weather/info.do",
			type : "post",
			dataType : "JSON",
			data : $("form").serialize(),
			success : function(json){
				var msgResult = "";
				for(var i = 0; i<json.length;i++){
					msgResult += ("날짜 : "+ json[i].day_info+"<br>");
					msgResult += ("오전 강수확률 : "+ json[i].morning_rain+"%<br>");
					msgResult += ("오후 강수확률 : "+ json[i].afternoon_rain+"%<br>");
					msgResult += ("최저온도 : "+ json[i].low_temp+"<br>");
					msgResult += ("최고온도 : "+ json[i].high_temp+"<br>");
					msgResult += ("<hr>");
				}
				$("#weather_info_list").html(msgResult);
			}
		});
	};
</script>
</head>
<body>
	<h1>내가 방금 말한 음성명령</h1>
	<hr>
	<div id="view_msg"></div>
	<br>
	<h1>날씨 정보 결과</h1>
	<hr>
	<div id="weather_info_list"></div>
	
	<form name="form" method="post">
		<input type="hidden" name="send_msg" id="send_msg">
	</form>
</body>
</html>