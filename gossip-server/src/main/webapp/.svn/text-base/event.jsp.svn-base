<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事件详情</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="all">
	<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
	<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="js/bootstrap.js"></script>
	<script type="text/javascript" src="js/eventdisplay1.js"></script>
	<script type="text/javascript" src="js/signup.js"></script>
	<script type="text/javascript" src="js/jquery.cookie.js"></script>
	<script type="text/javascript" src="js/login.js"></script>
	<script type="text/javascript" src="js/clicklog.js"></script>
	<script type="text/javascript" src="js/comment.js"></script>
</head>
<body>
<input type="hidden" id="eventid" value="${eventId}"/>
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
	  	  		  <li class="active"><a href="/gossip-server/">返回首页</a></li>
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
    		<label for="email">email:</label><input type="email" id="email" onBlur="EmailCheck()">
    	</form>
    </div>
    <div class="span6" style="padding:10px;" id="signuptishi">
		    	
		</div>
	</div>
	<div class="modal-footer" >	    
       <a   id="signupbutton" class="btn btn-primary" disabled="disabled" onClick="Form_Submit()" >注册</a>

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
							<div class="event-title" style="clear:both;">
								<div class="span4"><img src="http://i2.sinaimg.cn/dy/2012/0927/U6074P1DT20120927081744.jpg" alt=""></div>
								<div class="span8 event-brief">
									<h2>中日钓鱼岛争端</h2>
									<p>日本“购岛”动作一览钓鱼岛又称钓鱼台列岛，周围海域面积约为17万平方公里，相当于5个台湾岛面积
										<ul>
											<li>4月16日：石原慎太郎提出东京都将于年内购买钓鱼岛</li>
											<li>4月17日：石原称购岛协议基本达成</li>
											<li>4月28日：东京都发起购岛募捐活动</li>
											<li>7月7日：日本称欲将钓鱼岛“国有”</li>
											<li>7月10日：日相称最快9月底实现"国有"</li>
											<li>7月24日：日本启动“国有化”程序</li>
										</ul>
									</p>
								</div>
							</div>
						</div>					
					</div>
				</div>
			</div>
			<div class="row-fluid" style="margin-bottom: 30px;">
				<div class="row-wrapper">
					<h3 class="sub">事件新闻</h3>
					<div class="span1" id="timelinebar" style="min-height: 400px;"></div>
					<div class="span11" id="timelinecontent" style="min-height: 400px;margin-left: 0;">
						<div class="news">
							<span class="news-time">2012-10-16</span>
							<div class="news-list">
								<i></i>
								<ul>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.jsp" title="">西哈努克灵柩启程回国 新华门及外交部降半旗</a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.jsp" title="">视频：安倍晋三今日将参拜靖国神社</a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.jsp" title="">新华门及外交部降半旗</a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.jsp" title="">西哈努克灵柩启程回国 </a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
								</ul>
					
							</div>
						</div>
						<!-- <div class="news">
							<span class="news-time">2012-10-15</span>
							<div class="news-list">
								<i></i>
								<ul>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.html" title="">西哈努克灵柩启程回国 新华门及外交部降半旗</a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.html" title="">视频：安倍晋三今日将参拜靖国神社</a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
									<li class="article">
										<div class="article-content">
											<h2><a href="./news.html" title="">新华门及外交部降半旗</a></h2>
											<p>西哈努克灵柩启程回国 新华门及外交部降半旗	</p>
										</div>
									</li>
								</ul>
							</div>
						</div> -->
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
						<a href="#" class="span3">相关事件1 </a>
						<a href="#" class="span3">相关事件2 </a>
						<a href="#" class="span3">相关事件3 </a>
						<a href="#" class="span3">相关事件4</a>
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