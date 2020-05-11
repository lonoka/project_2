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
		getRankForSinger();
	});
	
	function getRankForSinger(){
		$.ajax({
			url : "/melon/getRankForSinger.do",
			type : "post",
			dataType : "JSON",
			contentType : "application/json; charset=UTF-8",
			success : function(json){
				var melon_rank = "";
				for(var i = 0; i<json.length; i++){
					melon_rank += (json[i].rank+"위 | ");
					melon_rank += (json[i].singer+" | ")
					melon_rank += (json[i].song_cnt+"곡 <br>");
				}
				console.log(json);
				$("#rank_for_singer").html(melon_rank);
			}
		})
		
	}
</script>
</head>
<body>
	<h1>가수별 노래 랭크 순위</h1>
	<hr>
	<div id="rank_for_singer"></div>
	<br>
	<hr>
</body>
</html>