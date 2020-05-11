<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="/js/jquery-3.5.0.min.js"></script>
<script type="text/javascript">
	$(window).on("load",function(){
		getSongForSinger();
	});
	
	function getSongForSinger(){
		$.ajax({
			url : "/melon/getSongForSinger.do",
			type : "post",
			dataType : "JSON",
			contentType : "application/json; charset=UTF-8",
			success : function(json){
				var melon_rank = "";
				for(var i = 0; i<json.length; i++){
					melon_rank += (json[i].rank+"위 | ");
					melon_rank += (json[i].song+"<br>");
				}
				console.log(json);
				$("#song_for_singer").html(melon_rank);
			}
		})
		
	}
</script>
</head>
<body>
	<h1>멜론 top100에 랭크된 방탄소년단의 노래 제목 가져오기</h1>
	<hr>
	<div id="song_for_singer"></div>
	<br>
	<hr>
</body>
</html>