<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
	<script type="text/javascript" src="js/jquery-1.8.2.js"></script>

	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript" src="js/index1.js"></script>
	<script type="text/javascript" src="js/signup.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	<script type="text/javascript" src="js/login.js"></script>
	<script type="text/javascript" src="js/clicklog.js"></script>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线新闻系统</title>
</head>
<body>

<div id="menu">
	<div class="navbar navbar-static-top navbar-inverse">

	  	<div class="navbar-inner">
	  	  <div class="container">
	  	  	<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		      </a>
	  	  	<a class="brand" href="#"><img src="images/logo3.gif" alt="新闻系统"></a>
	  	  	<div class="nav-collapse">
	  	  		<ul class="nav">
	  	  		</ul>
	  	  		
	  	  		<div class="pull-right" id="user-button">                        
	  	  		                <div id="login-popover-container" data-original-title="">
	  	  		                    <div class="btn-group">
	  	  		                        <a class="btn btn-success" href="#loginModal" data-toggle="modal" id="login-link" onClick="loginclear()">
	  	  		                            登录
	  	  		                        </a>
	  	  		                        <a class="btn btn-success" href="#signUpModal" data-toggle="modal" id="login-link" onClick="signupclear()">
	  	  		                            注册
	  	  		                        </a>
	  	  		                    </div>
	  	  		                </div>                
	  	  		     		</div>
	  	  	</div>

	  	  </div>
	  	</div>

	</div>
</div>
<div id="logininfo" class="pull-right">
	
</div>
<div id="loginModal" class="modal hide fade">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>请输入用户名和密码</h3>
  </div>
  <div class="modal-body">
    <div class="row-fluid">
    	<form action="#" class="span6" style="border-right: 1px dashed #D1D1D1;" id="loginform">
    		<label for="username">用户名：</label> <input type="text" name="loginusername" id="loginusername" onBlur="loginNameCheck()">
    		<label for="password">密码：</label><input type="password" name="loginpassword" id="loginpassword" onBlur="loginPasswordCheck()">
    	</form>
    	<div class="span6">
    		<p>
    			<a href="#">忘记密码？</a>
    		</p>
    		<p>还没有帐号？<a href="#"   onClick="toSignUp()">点击这里注册</a></p>
    		<div class="" style="padding:10px;" id="logintishi">
		    	
		        </div>
    	</div>
    </div>
  </div>
  <div class="modal-footer">	    
    <button  id="loginbutton" class="btn btn-primary " disabled="disabled" onClick="loginSubmit()">登录</button>
    <button type="button" class="btn" data-dismiss="modal">取消</button>
  </div>
</div>
<div id="signUpModal" class="modal hide fade">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>请输入注册信息</h3>
  </div>
  <div class="modal-body">
    <div class="row-fluid">
    	<form action="#" class="span6" style="border-right: 1px dashed #D1D1D1;" id="signupform">
    		<label for="username">用户名：</label> <input type="text" id="username" onBlur="UserNameCheck()">
    		<label for="password">密码：</label><input type="password" id="password" onBlur="PasswordCheck()">
    		<label for="repassword">重复密码：</label><input type="password" id="repassword" onBlur="RepasswordCheck()">
    		<label for="email">email:</label><input type="email" id="email" onBlur="EmailCheck()">
    	</form>
	    <div class="span6" style="padding:10px;" id="signuptishi">
		    	
		</div>

    </div>
	</div>
	<div class="modal-footer" >	    
       <button id="signupbutton" class="btn btn-primary" disabled="disabled" onClick="Form_Submit()" >注册</button>

       <button type="button" class="btn" data-dismiss="modal">取消</button>
     </div>
 
</div>

	<div id="container" class="container">
		<div id="head">
			<div id="logo">
			</div>	
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
					<h2 class="sub">最新事件</h2>
					<ul class="list" id="eventList">
						<li id="e-1" num="1" class="article">
							<div class="article-content">
								<h2><a href="./event.jsp" title="1元芳，你怎么看">1元芳，你怎么看</a></h2>
								<br><p>1元芳，你怎么看。大人，此时必有蹊跷！	</p>
							</div>
							<div class="info">
								特征词：
								<a class="label">特征词1</a>
								<a class="label">元芳</a>
								<span class="pull-right">时间：2012-10-15</span>
							</div>
						</li>
						
					</ul>
					
				</div>
				<div id="sidebar" class="span3">
					<div class="sidemodule">
						<h3 class="sub">热门关键词</h3>
						<a href="#" class="label">特征词1</a>
						<a href="#" class="label">元芳</a>
						<a href="#" class="label">特征词占位符</a>
						<a href="#" class="label">元芳</a>
						<a href="#" class="label">特征词1</a>
						<a href="#" class="label">元占位符芳</a>
						<a href="#" class="label">特征词1</a>
						<a href="#" class="label">元芳</a>
						<a href="#" class="label">特征占位符词1</a>
						<a href="#" class="label">元芳</a>
						<a href="#" class="label">特征词1</a>
						<a href="#" class="label">元占位符芳</a>
					</div>

					<div class="sidemodule">
						<h3 class="sub">热门人名</h3>
						<a href="#" class="label label-inverse">特征词1</a>
						<a href="#" class="label label-inverse">元芳占位符</a>
						<a href="#" class="label label-inverse">特征词1</a>
						<a href="#" class="label label-inverse">元芳</a>
						<a href="#" class="label label-inverse">特征词1</a>
						<a href="#" class="label label-inverse">元芳</a>
						<a href="#" class="label label-inverse">特征占位符词1</a>
						<a href="#" class="label label-inverse">元芳</a>
						<a href="#" class="label label-inverse">特征词1</a>
						<a href="#" class="label label-inverse">元芳</a>
						<a href="#" class="label label-inverse">特占位符征词1</a>
						<a href="#" class="label label-inverse">元芳</a>
					</div>

					<div class="sidemodule">
						<h3 class="sub">热门地名</h3>
						<a href="#" class="label label-info">特征占位词1</a>
						<a href="#" class="label label-info">元芳</a>
						<a href="#" class="label label-info">特征词1</a>
						<a href="#" class="label label-info">元占位芳</a>
						<a href="#" class="label label-info">特征词1</a>
						<a href="#" class="label label-info">元占位芳</a>
						<a href="#" class="label label-info">特征词1</a>
						<a href="#" class="label label-info">元芳</a>
						<a href="#" class="label label-info">特占位征词1</a>
						<a href="#" class="label label-info">元芳</a>
						<a href="#" class="label label-info">特征词1</a>
						<a href="#" class="label label-info">元占位芳</a>
					</div>
				</div>
			</div>
			<div id="page" class="pagination pagination-centered">
		             <ul>
		                
		             </ul>
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