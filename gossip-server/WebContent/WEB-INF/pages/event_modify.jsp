<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事件详情</title>
<link rel="stylesheet" type="text/css" href="css/admin/bootstrap.css"
	media="all">
<link rel="stylesheet" type="text/css" href="css/admin/style.css"
	media="all">
<script type="text/javascript" src="js/admin/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/admin/bootstrap.js"></script>
<script type="text/javascript" src="js/admin/eventdisplay1.js"></script>
<script type="text/javascript" src="js/admin/ajaxfileupload.js"></script>
<script type="text/javascript" src="js/admin/signup.js"></script>
<script type="text/javascript" src="js/admin/jquery.cookie.js"></script>
<script type="text/javascript" src="js/admin/login.js"></script>
<script type="text/javascript" src="js/admin/clicklog.js"></script>
<script type="text/javascript" src="js/admin/comment.js"></script>
</head>
<body>
	<input type="hidden" id="eventid" value="${eventId}" />
	<div id="menu">
		<div class="navbar navbar-static-top navbar-inverse">

			<div class="navbar-inner">
				<div class="container">
					<a class="btn btn-navbar" data-toggle="collapse"
						data-target=".nav-collapse"> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
					</a> <a class="brand" href="#"><img src="images/logo3.gif"
						alt="新闻系统"></a>
					<div class="nav-collapse">
						<ul class="nav">
							<li class="active"><a href="/gossip-server/">返回首页</a></li>
						</ul>

						<div class="pull-right" id="user-button">
							<div id="login-popover-container" data-original-title="">
								<div class="btn-group">
									<a class="btn btn-success" href="#loginModal"
										data-toggle="modal" id="login-link" onClick="loginclear()">
										登录 </a> <a class="btn btn-success" href="#signUpModal"
										data-toggle="modal" id="login-link" onClick="signupclear()">
										注册 </a>
								</div>
							</div>
						</div>
					</div>

				</div>
			</div>

		</div>
	</div>
	<div id="logininfo" class="pull-right"></div>
	<div id="loginModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>请输入用户名和密码</h3>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<form action="#" class="span6"
					style="border-right: 1px dashed #D1D1D1;" id="loginform">
					<label for="username">用户名：</label> <input type="text"
						name="loginusername" id="loginusername" onBlur="loginNameCheck()">
					<label for="password">密码：</label><input type="password"
						name="loginpassword" id="loginpassword"
						onBlur="loginPasswordCheck()">
				</form>
				<div class="span6">
					<p>
						<a href="#">忘记密码？</a>
					</p>
					<p>
						还没有帐号？<a href="#" onClick="toSignUp()">点击这里注册</a>
					</p>
					<div class="span6" style="padding: 10px;" id="logintishi"></div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<a id="loginbutton" class="btn btn-primary" disabled="disabled"
				onClick="loginSubmit()">登录</a>
			<button type="button" class="btn" data-dismiss="modal">取消</button>
		</div>
	</div>
	<div id="signUpModal" class="modal hide fade">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal"
				aria-hidden="true">&times;</button>
			<h3>请输入注册信息</h3>
		</div>
		<div class="modal-body">
			<div class="row-fluid">
				<form action="#" class="span6"
					style="border-right: 1px dashed #D1D1D1;" id="signupform">
					<label for="username">用户名：</label> <input type="text" id="username"
						onBlur="UserNameCheck()"> <label for="password">密码：</label><input
						type="password" id="password" onBlur="PasswordCheck()"> <label
						for="repassword">重复密码：</label><input type="password"
						id="repassword" onBlur="RepasswordCheck()"> <label
						for="email">email:</label><input type="email" id="email">
				</form>
			</div>
			<div class="span6" style="padding: 10px;" id="signuptishi"></div>
		</div>
		<div class="modal-footer">
			<button type="button" id="signupbutton" class="btn btn-primary" 
				onClick="Form_Submit()">注册</button>

			<button type="button" class="btn" data-dismiss="modal">取消</button>
		</div>

	</div>
	<div id="container" class="container">
		<div id="head">
			<ul class="breadcrumb pull-left">
				<li><a href="/gossip-server/">首页</a> <span class="divider">&gt;</span></li>
				<li class="active">事件详情</li>
			</ul>
		</div>

		<div id="container-inner">
			<div class="row-fluid" style="margin-bottom: 30px;">
				<div class="row-wrapper">
					<div class="span12 event-title-container">
						<div class="event-title-inner">
							<div class="event-title" style="clear: both;">
							  
							</div>
						</div>
					</div>
				</div>
				<div id="addnews" style="clear:both;">
				   <div id="label" style="float:left"><label>添加新闻：  </label></div>
				   <div id="text" style="float:left"><input type="text" id="addednews"><input type="button" value="提交" id="submitInfo"></div>
				</div>
				
				<div class="row-fluid" style="margin-bottom: 30px;">
					<div class="row-wrapper">
						<h3 class="sub">事件新闻</h3>
						<div class="span1" id="timelinebar" style="min-height: 400px;"></div>
						<div class="span11" id="timelinecontent"
							style="min-height: 400px; margin-left: 0;">
							<div class="news">
								<span class="news-time"></span>
								<div class="news-list">
									<i></i>
									<ul>

									</ul>

								</div>
							</div>

						</div>
					</div>
				</div>
				<div id="page" class="pagination pagination-centered">
					<ul>

					</ul>
				</div>
				<div class="row-fluid">
					<div class="row-wrapper">
						<h3 class="sub">相关事件</h3>
						<div id="relate" class="relate-events">
							<a href="#" class="span3"> </a>

						</div>
					</div>
				</div>

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