<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新闻详情</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
	<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript" src="js/newsdisplay.js"></script>
	<script type="text/javascript" src="js/signup.js"></script>
	<script type="text/javascript" src="js/login.js"></script>
	<script type="text/javascript" src="js/clicklog.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	<script type="text/javascript" src="js/comment.js"></script>
	<script type="text/javascript"></script>
</head>
<body>
<input type="hidden" id="newsid" value="${newsId}"/>
<input type="hidden" id="currentUname" value='<%=session.getAttribute("currentUser")%> '/>
<div id="menu">
		<div class="navbar navbar-static-top navbar-inverse">

		  	<div class="navbar-inner">
		  	  <div class="container">
		  	  	<a class="brand" href="#"><img src="images/logo3.gif" alt="新闻系统"></a>
		  	  	<ul class="nav">
		  	  	  <li class="active"><a href="/">返回首页</a></li>
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
    		<div class="span6" style="padding:10px;" id="logintishi">
		    	
		        </div>
    	</div>
    </div>
  </div>
  <div class="modal-footer">	    
    <a  id="loginbutton" class="btn btn-primary" disabled="disabled" onClick="loginSubmit()">登录</a>
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
    		<label for="email">email:</label><input type="email" id="email">
    	</form>
	    <div class="span6" style="padding:10px;" id="signuptishi">
		    	
		</div>

    </div>
	</div>
	<div class="modal-footer" >	    
       <button type="button" id="signupbutton" class="btn btn-primary" 
				onClick="Form_Submit()">注册</button>

       <button type="button" class="btn" data-dismiss="modal">取消</button>
     </div>
 
</div>
	<div id="container" class="container">
		<div id="head">
			<div id="logo">
				
			</div>	
			<ul class="breadcrumb pull-left">
				  <li><a href="/">首页</a> <span class="divider">&gt;</span></li>
				  <li class="active">新闻详情</li>
			</ul>

		</div>
		
		<div id="container-inner">
			<div class="news-title">
				
			</div>
			<div class="news-content">
				
			</div>
             <div id="comment">
	            <p style="font-size:22px;color:#3366FF;">评论区：</P>
                <table id="table1"></table>
	            <p style="font-size:15px;">用户名:
				<a id="commentusername"></a></br></p>
				<textarea type="text" id="textarea1"style="width:400px;height:100px;border:#9a9a9a 3px solid;boder-top-color:#9a9a9a;border-right-color:#cdcdcd;border-bottom-color:#cdcdcd;border-left-color:#9a9a9a;"></textarea>
				<input class="btn btn-primary" type="button" value="提交"  onclick="commentSubmit()"/>
             </div>    
			<div class="row-fluid">
				<div class="row-wrapper">
					<h3 class="sub">相关新闻</h3>
					<div class="relate-news">
						<a href="#" class="span3"> </a>

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