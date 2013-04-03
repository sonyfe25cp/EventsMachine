<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css"
	media="all">
<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/admin.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/login.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事件列表</title>

<script>
	function hideAll() {
		for (i = 0; i < odiv.length; i++) {
			odiv[i].style.display = "none";
		}
	}

	function showObj(num) {

		if (odiv[num].style.display == "none") {
			hideAll();
			odiv[num].style.display = "inline";
		} else {
			odiv[num].style.display = "none";
		}

	}
</script>
<style>
/*    tr{ background-color:#666666} ;*/
table {
	background: #FF9900;
	position: relative;
	left: 150px;
	top: 110px;
	font-weight: bold;
	font-family: "utf-8";
}

.a {
	color: #FFFFFF;
	font-size: 18px;
}

.a:hover {
	color: #FFFFFF;
	background-color: #0000FF;
	text-decoration: none;
}

.a:link {
	text-decoration: none;
}

.a:visited {
	text-decoration: none;
}

.a:active {
	text-decoration: none;
}
</style>
</head>
<body>
	<div id="menu">
		<div class="navbar navbar-static-top navbar-inverse">

			<div class="navbar-inner">
				<div class="container">
					<a class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse"> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
					</a> <a class="brand" href="#"><img src="images/logo.png"
						alt="新闻系统"></a>
					<div class="nav-collapse">
						<ul class="nav">
							<li class="active"><a href="./index.html">首页</a></li>
						</ul>
					</div>
				</div>
			</div>

		</div>
	</div>
	<!--  
	<table
		style="width: 60px; height: 20px; text-align: center; line-height: 20px; font-size: 12px; float: left;">
		<tr>
			<td><a href="javascript:void(0)" onClick="showObj(0)">事件管理</a><br>
				<div id="odiv" style="display: none" onClick="">
					事件管理

				</div></td>
		</tr>
		<tr>
			<td><a href="javascript:void(0)" onClick="showObj(1)">新闻管理</a><br>
				<div id="odiv" style="display: none">
					新闻管理

				</div></td>
		</tr>
		<!-- <tr >
    <td>
      <a href="javascript:void(0)" onClick="showObj(2)">菜单三</a><br>
      <div id="odiv" style="display:none">导航一<br>导航二<br>导航三</div>
    </td></tr>
<tr>
<td><a href="javascript:void(0)" onClick="showObj(3)">菜单四</a><br>
<div id="odiv" style="display:none">导航一<br>导航二<br>导航三</div>
</td>
</tr> 
	</table>
	-->
	<div id="container" class="container">
		<div id="head">
			<div id="logo"></div>
			<ul class="breadcrumb pull-left">

			</ul>
			<!-- <form method="get" id="searchform" action="#">
				<input type="text" value="Search..." name="s" id="s" onfocus="if (this.value == 'Search...') {this.value = '';}" onblur="if (this.value == '') {this.value = 'Search...';}">
				<input type="submit" id="searchsubmit" value="Search">
			</form> -->
		</div>

		<div id="container-inner" style="padding: 20px 0 40px;">
			<div class="row-fluid">
				<div id="primary" class="span9">
					<h2 class="sub">事件管理</h2>
					<ul class="list">
						
					</ul>
				</div>
			</div>
		</div>
		<div id="page" class="pagination pagination-centered">
			<ul>

			</ul>
		</div>

		<div id="footer">
			<div class="copyright">
				<h3>版权声明</h3>
				<p>
					版权所有 © 2012 <a href="#">****M</a>. 保留所有权.
				</p>
			</div>
		</div>
	</div>

</body>
</html>