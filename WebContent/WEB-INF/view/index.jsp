<%@page import="poly.dto.DataDTO"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<DataDTO> rList = (List<DataDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/js/jquery-3.5.0.min.js"></script>
<script src="https://www.amcharts.com/lib/4/core.js"></script>
<script src="https://www.amcharts.com/lib/4/charts.js"></script>
<script src="https://www.amcharts.com/lib/4/plugins/wordCloud.js"></script>
<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>
<script type="text/javascript">
var rList = [
<%for (int i = 0; i < rList.size(); i++) {%>
	{word:'<%=rList.get(i).getWord()%>', count:<%=rList.get(i).getCount()%>},
<%}%>
];
am4core.ready(function() {

// Themes begin
am4core.useTheme(am4themes_animated);
// Themes end


var chart = am4core.create("chartdiv", am4plugins_wordCloud.WordCloud);
var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());

series.randomness = 0.1;
series.rotationThreshold = 0.5;
series.labels.template.tooltipText = "{word}: {value}";
series.fontFamily = "Courier New";
series.data = rList
series.dataFields.word = "word";
series.dataFields.value = "count";
series.minFontSize=am4core.percent(10);
series.maxFontSize = am4core.percent(30);

}); // end am4core.ready()
</script>
<link rel="stylesheet" href="/css/jqcloud.min.css">
<title>Insert title here</title>
</head>
<body>
	<div id="chartdiv"></div>
</body>
</html>