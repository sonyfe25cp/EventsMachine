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
	<script type="text/javascript" src="js/search.js"></script>

</head>
<body>

<div id="menu">
	<div class="navbar navbar-static-top navbar-inverse">

	  	<div class="navbar-inner">
	  	  <div class="container">
	  	  	<a class="brand" href="#"><img src="images/logo.png" alt="新闻系统"></a>
	  	  	<ul class="nav">
	  	  	  <li class="active"><a href="/">Home</a></li>
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

<div id = "search-body" style="width:100%;float:left;">
   <div id = "search-left" style="width:46%;min-height:600px;float:left;">
       <div id = "search1" class="row-fluid" >			
			<input type="text" name="wd" id="queryWords1" maxlength="100" style="width:350px;">
			<span class="btn_wr" style="margin-right: 10px;">
				<input type="button" value="搜索一下" id="search_btn1" class="button button-rounded">
			</span>		
		</div>
		<div id = "left-result" class = "row-fluid">
			 <div id = "primary1" class="span11">
			       <ul class="list" id="eventList">
			       
			       </ul>
			 </div>
		</div>
		<div id="page1" class="pagination pagination-centered1">
		   <ul>

		   </ul>
		</div>
   </div><!-- search-left -->
   <div id = "search-right" style="width:46%;min-height:600px;float:left">
       <div id = "search2" class="row-fluid" >			
			<input type="text" name="wd" id="queryWords2" maxlength="100" style="width:350px;">
			<span class="btn_wr" style="margin-right: 10px;">
				<input type="button" value="搜索一下" id="search_btn2" class="button button-rounded">
			</span>		
	   </div>
	   <div id = "right-result" class = "row-fluid">
			 <div id = "primary2" class="span11">
			       <ul class="list" id="eventList">
			       
			       </ul>
			 </div>
		</div>
		<div id="page2" class="pagination pagination-centered1">
		   <ul>

		   </ul>
		</div>
   </div><!-- search-right -->
   <div style="clear:both"></div>
</div><!-- search-body -->	

</body>
</html>