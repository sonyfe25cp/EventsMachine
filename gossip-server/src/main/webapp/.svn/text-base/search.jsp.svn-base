<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>搜索</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
	<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>

</head>
<body>

<div id="menu">
	<div class="navbar navbar-static-top navbar-inverse">

	  	<div class="navbar-inner">
	  	  <div class="container">
	  	  	<a class="brand" href="#"><img src="images/logo.png" alt="新闻系统"></a>
	  	  	<ul class="nav">
	  	  	  <li class="active"><a href="/gossip-server/">Home</a></li>
	  	  	  <li><a href="#">Link</a></li>
	  	  	  <li><a href="#">Link</a></li>
	  	  	</ul>
	  	  	<div class="pull-right" id="user-button">                        
                    <div id="login-popover-container" data-original-title="">
                        <div class="btn-group">
                            <a class="btn btn-success" href="#loginModal" data-toggle="modal" id="login-link">
                                登录
                            </a>
                            <a class="btn btn-success" href="/login/" data-toggle="modal" id="login-link">
                                注册
                            </a>
                        </div>
                    </div>
                
     		</div>
	  	  </div>
	  	</div>

	</div>
</div>
<div id="loginModal" class="modal hide fade">
  <div class="modal-header">
    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    <h3>请输入用户名和密码</h3>
  </div>
  <div class="modal-body">
    <div class="row-fluid">
    	<form action="#" class="span6" style="border-right: 1px dashed #D1D1D1;">
    		<label for="username">用户名：</label> <input type="text" name="username">
    		<label for="password">密码：</label><input type="password" name="password">
    	</form>
    	<div class="span6">
    		<p>
    			<a href="#">忘记密码？</a>
    		</p>
    		<p>还没有帐号？<a href="#">点击这里注册</a></p>
    	</div>
    </div>
  </div>
  <div class="modal-footer">	    
    <a href="#" class="btn btn-primary">登录</a>
    <button type="button" class="btn" data-dismiss="modal">取消</button>
  </div>
</div>

	<div id="container" class="container">
		<div id="container-inner" style="padding: 120px 0 40px;">
			<div class="row-fluid" style="min-height: 400px; margin:0 auto;width:500px;">	
				<p style="text-align: center"> 
					<img src="http://www.baidu.com/img/baidu_sylogo1.gif" width="270" height="129">
				</p>			
				<form name="f" action="#" onsubmit="javascript:F.call('ps/ssuggestion','pssubmit');">
					<input type="text" name="wd" id="kw" maxlength="100" style="width:474px;" autocomplete="off">
					<div class="text-center" style="margin-top: 10px;">
						<span class="btn_wr" style="margin-right: 10px;">
							<input type="submit" value="搜索一下" id="su" class="btn">
						</span>		
						<span class="btn_wr">
							<input type="submit" value="手气不错" id="su" class="btn">
						</span>
					</div>
				</form>
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