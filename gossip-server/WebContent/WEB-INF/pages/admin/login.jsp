<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
	<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>

	<script type="text/javascript" src="js/adminlogin.js"></script>
	<script type="text/javascript" src="js/signup.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	<script type="text/javascript" src="js/managerlogin.js"></script>
	<script type="text/javascript" src=""></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<div id="menu">
	<div class="navbar navbar-static-top navbar-inverse">
	  	<div class="navbar-inner">
	  	  <div class="container">
	  	  	<a class="brand" href="#"><img src="images/logo.png" alt="新闻系统"></a>
	  	  	<div class="nav-collapse">
	  	  		<ul class="nav">
	  	  		  <li class="active"><a href="./index.html">首页</a></li>
	  	  		</ul>	  	  		

	  	  	</div>

	  	  </div>
	  	</div>
	</div>
</div>
	<div id="container" class="container">
		<div id="head">
			<div id="logo">
			</div>	

		</div>

<div id="container-inner" style="padding: 20px 0 40px;">

			<div class="row-fluid">
				<div id="primary" class="span9">
					<h2 class="sub">管理员登录</h2>
					
					<div id="logininfo" class="pull-right">
	
</div>
<div id="logininfo" class="pull-right">
	
</div>
<div id="loginModal" >
  <div class="modal-header">
    <h3>请输入用户名和密码</h3>
  </div>
  <div class="modal-body">
    <div class="row-fluid">
    	<form action="/admin/login" method="post" class="span6" style="border-right: 1px dashed #D1D1D1;" id="loginform">
    		<label for="username">用户名：</label>
    		<input type="text" name="email" id="loginusername" onBlur="loginNameCheck()">
    		<label for="password">密码：</label>
    		<input type="password" name="password" id="loginpassword" onBlur="loginPasswordCheck()">
		    <p/>
		    <input class="btn btn-primary" type="submit" value="登录"></input>
    	</form>
    	<div class="span6">
    		<!-- <p>
    			<a href="#">忘记密码？</a>
    		</p>
    		<p>还没有帐号？<a href="#"   onClick="toSignUp()">点击这里注册</a></p> -->
    		<div class="" style="padding:10px;" id="logintishi">
		    	
		    </div>
    	</div>
    </div>
  </div>
  <div class="" align="center">	    
    <!--    <button type="button" class="btn" data-dismiss="modal">取消</button> -->
  </div>
</div>
				
	</div>
		</div>
		<div id="footer">
			<div class="copyright">
				<h3>版权声明</h3>
				<p>版权所有 © 2012 <a href="#">****M</a>. 保留所有权.</p>
			</div>
		</div>

	
</div>

</body>
</html>