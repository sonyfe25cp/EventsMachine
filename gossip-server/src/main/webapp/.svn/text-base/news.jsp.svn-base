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
	    <div class="span6" style="padding:10px;" id="signuptishi">
		    	
		</div>

    </div>
	</div>
	<div class="modal-footer" >	    
       <a   id="signupbutton" class="btn btn-primary" disabled="disabled" onClick="Form_Submit()" >注册</a>

       <button type="button" class="btn" data-dismiss="modal">取消</button>
     </div>
 
</div>
	<div id="container" class="container">
		<div id="head">
			<div id="logo">
				
			</div>	
			<ul class="breadcrumb pull-left">
				  <li><a href="./index.jsp">首页</a> <span class="divider">&gt;</span></li>
				  <li class="active">新闻详情</li>
			</ul>

		</div>
		
		<div id="container-inner">
			<div class="news-title">
				<h2 style="text-align: center;">西哈努克灵柩启程回国 新华门及外交部降半旗</h2>
				<p style="text-align: center;"><span>2012年10月17日10:08</span> 
					<span class="news-source">新闻来源:<a href="http://news.qq.com/a/20121017/000750.htm">腾讯网</a></span>
				</p>
			</div>
			<div class="news-content">
				<p align="center"><img alt="西哈努克灵柩启程 新华门及外交部降半旗" src="http://img1.gtimg.com/news/pics/hv1/183/78/1171/76164348.jpg"></p>
				<p align="center"><span style="FONT-SIZE: 12px">10月17日，为哀悼柬埔寨前国王诺罗敦·西哈努克逝世，新华门、天安门广场降半旗致哀。【</span><a target="_blank" _fcksavedurl="http://news.qq.com/a/20121017/000683.htm#p=1" href="http://news.qq.com/a/20121017/000683.htm#p=1"><span style="FONT-SIZE: 12px">查看高清图集</span></a><span style="FONT-SIZE: 12px">】</span></p>
				<br>
				<p style="TEXT-INDENT: 2em">据央视记者现场报道，西哈努克灵柩目前已经离开北京医院，经长安街启程前往首都机场。央视记者表示，据了解，长安街、新华门及<!--keyword--><span class="infoMblog"><a class="a-tips-Article-QQ" href="http://t.qq.com/wj_xlt#pref=qqcom.keyword" rel="wj_xlt" reltitle="外交部" target="_blank"><!--/keyword-->外交部<!--keyword--></a></span><!--/keyword-->等地都已经降了半旗。</p>
				<p style="TEXT-INDENT: 2em"><strong>国务委员戴秉国将护送西哈努克灵柩返回柬埔寨</strong></p>
				<p style="TEXT-INDENT: 2em">中新网10月17日电 据外交部网站消息，外交部发言人洪磊宣布，国务委员戴秉国将于10月17日护送柬埔寨太皇西哈努克灵柩返回柬埔寨。</p>
				<p style="TEXT-INDENT: 2em">其后，西哈努克的灵柩将经过金边的俄罗斯大道和独立碑广场，最后运抵金边王宫。</p>
				<p style="TEXT-INDENT: 2em">金边当地的电视台预计，期间将有至少10万柬埔寨民众夹道迎接西哈努克的灵柩。</p>
				<p style="TEXT-INDENT: 2em">被誉为柬埔寨宪政之父、独立之父的西哈努克，他的逝世令柬埔寨民众悲痛万分。</p>
				<p style="TEXT-INDENT: 2em">当地时间16日下午6时许，中新社记者在金边王宫广场看到，广场上已挂起西哈努克的大幅画像。广场上聚集的几千名柬埔寨民众，面色沉重，手捧白色莲花，焚香祈祷西哈努克太皇遗体平安抵达金边。</p>
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
						<a href="#" class="span3">相关新闻1 </a>
						<a href="#" class="span3">相关新闻2 </a>
						<a href="#" class="span3">相关新闻3 </a>
						<a href="#" class="span3">相关新闻4</a>
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