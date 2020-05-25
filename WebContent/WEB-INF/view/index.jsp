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


<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:300,400,700"
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

<title>Insert title here</title>
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
							<a href="index.html" class="h2 mb-0">CA<span
								class="text-primary">.</span>
							</a>
						</h1>
					</div>

					<div class="col-12 col-md-10 d-none d-xl-block">
						<nav class="site-navigation position-relative text-right"
							role="navigation">

							<ul
								class="site-menu main-menu js-clone-nav mr-auto d-none d-lg-block">
								<li><a href="" class="nav-link">Login</a></li>

								<li><a href="#blog-section" class="nav-link">Sign in</a></li>
								<li><a href="#contact-section" class="nav-link">Contact</a></li>
							</ul>
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



		<div class="site-blocks-cover overlay"
			style="background-image: url(/images/bg.jpg);" data-aos="fade"
			id="home-section">

			<div class="container">
				<div class="row align-items-center justify-content-center">


					<div class="col-md-12 mt-lg-5 text-center">
						<div id="slider" class="carousel slide" data-ride="carousel">
							<ol class="carousel-indicators">
								<li data-target="#slider" data-slide-to="0" class="active"></li>
								<li data-target="#slider" data-slide-to="1"></li>
							</ol>
							<div class="carousel-inner">
								<div class="carousel-item active">
									<h2><%=rList.get(0).getCommu_name()%></h2>
									<div id="chartdiv" class="" style="height: 600px; width: 100%"></div>
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
										style="padding-top: 8.5px; padding-bottom: 8.5px">검색</button>
								</div>
							</div>
						</div>
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

series.labels.template.tooltipText = "{word}: {value}";
series.fontFamily = "Courier New";
series.data = rList
series.dataFields.word = "word";
series.dataFields.value = "count";

}); // end am4core.ready()
</script>



</body>
</html>