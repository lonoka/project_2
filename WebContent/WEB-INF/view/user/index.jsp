<%@page import="poly.dto.DataDTO"%>
<%@page import="java.util.List"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	List<DataDTO> rList = (List<DataDTO>) request.getAttribute("rList");
	List<DataDTO> wList = (List<DataDTO>) request.getAttribute("wList");
	List<DataDTO> tList = (List<DataDTO>) request.getAttribute("tList");
	String userId = (String) session.getAttribute("userId");
	String userAuthor = (String) session.getAttribute("userAuthor");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">


<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,700"
	rel="stylesheet">
<link
	href="https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@400;700;800&display=swap"
	rel="stylesheet">
<link rel="stylesheet" href="/fonts/icomoon/style.css">
<link rel="stylesheet" href="/css/bootstrap.min.css">
<link rel="stylesheet" href="/css/jquery-ui.css">
<link rel="stylesheet" href="/css/owl.carousel.min.css">
<link rel="stylesheet" href="/css/owl.theme.default.min.css">
<link rel="stylesheet" href="/css/owl.theme.default.min.css">
<link rel="stylesheet" href="/css/jquery.fancybox.min.css">
<link rel="stylesheet" href="/css/bootstrap-datepicker.css">
<link rel="stylesheet" href="/fonts/flaticon/font/flaticon.css">
<link rel="stylesheet" href="/css/aos.css">
<link rel="stylesheet" href="/css/style.css">
<style type="text/css">
h2 a:hover {
	color: aliceblue !important;
}
</style>

<title>CA.</title>
</head>
<body data-spy="scroll" data-offset="300">

	<div id="overlayer"></div>
	<div class="loader">
		<div class="spinner-border text-primary" role="status">
			<span class="sr-only">Loading...</span>
		</div>
	</div>

	<div class="site-wrap">

		<div class="site-mobile-menu site-navbar-target">
			<div class="site-mobile-menu-header">
				<div class="site-mobile-menu-close mt-3">
					<span class="icon-close2 js-menu-toggle"></span>
				</div>
			</div>
			<div class="site-mobile-menu-body"></div>
		</div>


		<header class="site-navbar js-sticky-header site-navbar-target"
			role="banner">

			<div class="container">
				<div class="row align-items-center">

					<div class="col-6 col-xl-2">
						<h1 class="mb-0 site-logo">
							<a href="/index.do" class="h2 mb-0">CA<span
								class="text-primary">.</span>
							</a>
						</h1>
					</div>

					<div class="col-12 col-md-10 d-none d-xl-block">
						<nav class="site-navigation position-relative text-right"
							role="navigation">
							<%
								if (userId == null) {
							%>
							<%@include file="/WEB-INF/view/user/frame/TopbarLogout.jsp"%>
							<%
								} else {
							%>
							<%
								if (userAuthor.equals("1")) {
							%>
							<%@include file="/WEB-INF/view/user/frame/TopbarLoginAdmin.jsp"%>
							<%
								} else {
							%>
							<%@include file="/WEB-INF/view/user/frame/TopbarLoginUser.jsp"%>
							<%
								}
							%>
							<%
								}
							%>
						</nav>
					</div>


					<div class="col-6 d-inline-block d-xl-none ml-md-0 py-3"
						style="position: relative; top: 3px;">
						<a href="#" class="site-menu-toggle js-menu-toggle float-right"><span
							class="icon-menu h3"></span></a>
					</div>

				</div>
			</div>

		</header>
		<!-- Modal -->
		<%
			if (userId == null) {
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalLogout.jsp"%>
		<%
			} else {
		%>
		<%
			if (userAuthor.equals("1")) {
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalLoginAdmin.jsp"%>
		<%
			} else {
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalLoginUser.jsp"%>
		<%
			}
		%>
		<%
			}
		%>
		<%@include file="/WEB-INF/view/user/frame/ModalContact.jsp"%>


		<div class="site-blocks-cover overlay"
			style="background-image: url(/images/bg.jpg);" data-aos="fade"
			id="home-section">

			<div class="container">
				<div class="row align-items-center justify-content-center">


					<div class="col-md-12 mt-lg-5 text-center">
						<div id="slider" class="carousel slide" data-ride="carousel">
							<ol class="carousel-indicators">
								<li data-target="#slider" data-slide-to="0" class="active"></li>

							</ol>
							<div class="carousel-inner">
								<div class="carousel-item active">
									<h2 style="font-family: Nanum Gothic">
										<a href="javascript:void(0)" class="" data-toggle="modal"
											data-target="#comu_modal_1"
											style="color: aliceblue !important;"><%=rList.get(0).getCommu_name()%></a>
									</h2>
									<div id="wordcloud_1" class=""
										style="height: 600px; width: 100%"></div>
								</div>
							</div>
						</div>
						<br>
						<div class="col-md-8" style="margin: 0 auto">
							<div class="form-row">
								<div class="col-12 col-md-9 mb-2 mb-md-0">
									<input type="email" class="form-control form-control-lg"
										placeholder="검색어를 입력하세요">
								</div>
								<div class="col-12 col-md-3">
									<button type="submit" class="btn btn-block btn-lg btn-primary"
										style="padding-top: 8.5px; padding-bottom: 8.5px; color: dodgerblue;">검색</button>
								</div>
							</div>
						</div>
					</div>

				</div>


			</div>

		</div>
		<!-- 커뮤니티 모달 -->
		<div class="modal fade" id="comu_modal_1" tabindex="-1" role="dialog"
			aria-labelledby="contactLabel" aria-hidden="true">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="contactLabel"><%=rList.get(0).getCommu_name()%></h4>
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>

					</div>
					<div class="modal-body">
						<div class="row">
							<div id="time_chart_1" class="col-md-6"
								style="border: 1px solid black; height: 200px;"></div>
							<div class="col-md-6">긍정 부정 차트</div>
						</div>
						게시글 작성자
						<div id="writer_chart_1" class="col-md-12"
							style="margin: auto; height: 300px"></div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						<button type="button"
							onclick="open_comu('<%=rList.get(0).getCommu_name()%>')"
							class="btn btn-primary" style="color: dodgerblue;">커뮤니티
							열기</button>
					</div>
				</div>
			</div>
		</div>

	</div>
	<!-- .site-wrap -->
	<script src="/js/jquery-3.5.0.min.js"></script>
	<script src="/js/jquery-ui.js"></script>
	<script src="/js/popper.min.js"></script>
	<script src="/js/bootstrap.min.js"></script>
	<script src="/js/owl.carousel.min.js"></script>
	<script src="/js/jquery.countdown.min.js"></script>
	<script src="/js/jquery.easing.1.3.js"></script>
	<script src="/js/aos.js"></script>
	<script src="/js/jquery.fancybox.min.js"></script>
	<script src="/js/jquery.sticky.js"></script>
	<script src="/js/isotope.pkgd.min.js"></script>
	<script src="/js/main.js"></script>

	<script src="https://www.amcharts.com/lib/4/core.js"></script>
	<script src="https://www.amcharts.com/lib/4/charts.js"></script>
	<script src="https://www.amcharts.com/lib/4/plugins/wordCloud.js"></script>
	<script src="https://www.amcharts.com/lib/4/themes/animated.js"></script>

	<!-- 워드클라우드 스크립트 -->
	<script type="text/javascript">
	var rList = [
		<%for (int i = 0; i < rList.size(); i++) {%>
			{word:'<%=rList.get(i).getWord()%>', count:<%=Integer.toString(rList.get(i).getCount())%>},
		<%}%>
	];
	am4core.ready(function() {
	// Themes begin
	am4core.useTheme(am4themes_animated);
	// Themes end
	var chart = am4core.create("wordcloud_1", am4plugins_wordCloud.WordCloud);
	var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());
	series.randomness = 0.1;
	series.labels.template.tooltipText = "{word}: {value}";
	series.fontFamily = "Courier New";
	series.data = rList
	series.dataFields.word = "word";
	series.dataFields.value = "count";

	}); // end am4core.ready()
	</script>
	<!-- 작성자 차트 -->
	<script type="text/javascript">
	<%if (wList.size() > 100) {%>
		var wList = [
		     		<%for (int i = 0; i < 100; i++) {%>
		     			{writer:'<%=wList.get(i).getWord()%>', count:<%=wList.get(i).getCount()%>},
		     		<%}%>
		     		];
	<%} else {%>
	var wList = [
		<%for (int i = 0; i < wList.size(); i++) {%>
			{writer:'<%=wList.get(i).getWord()%>', count:<%=Integer.toString(wList.get(i).getCount())%>},
		<%}%>
		];
	<%}%>
	
	console.log(wList);
	am4core.ready(function() {
		// Themes begin
		am4core.useTheme(am4themes_animated);
		// Themes end
		var chart = am4core.create("writer_chart_1", am4plugins_wordCloud.WordCloud);
		var series = chart.series.push(new am4plugins_wordCloud.WordCloudSeries());
		series.randomness = 0.8;
		series.minFontSize = 15;
		series.maxFontSize = 80;
		series.labels.template.tooltipText = "{word}: {value}";
		series.fontFamily = "Courier New";
		series.data = wList
		series.dataFields.word = "writer";
		series.dataFields.value = "count";

		});
	</script>
	<!-- 시간대별 차트 -->
	<script type="text/javascript">
	var tList = [
		<%for (int i = 0; i < tList.size(); i++) {%>
			{section:'<%=tList.get(i).getWord()%>', count:<%=Integer.toString(tList.get(i).getCount())%>},
		<%}%>
		];
	</script>
	<script>
    $('.carousel').carousel({
  interval: 10000
})  
</script>
	<script>
	$('body').on('click', '.topbar_link', function(e) {
		var $this = $(this);
		e.preventDefault();

		if ( $('body').hasClass('offcanvas-menu') ) {
			$('body').removeClass('offcanvas-menu');
			$this.removeClass('active');
		}
	}) 
	function open_comu(e){
		var str = e;
		switch (e) {
		case "컴퓨터 본체 갤러리":
			window.open('https://gall.dcinside.com/board/lists/?id=pridepc_new3','_blank');
			break;

		default:
			break;
		}
	}
</script>


</body>
</html>