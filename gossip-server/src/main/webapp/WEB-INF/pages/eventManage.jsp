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
<script type="text/javascript" src="js/eventManage.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<script type="text/javascript" src="js/login.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事件修改</title>
</head>
<body>
<input type="hidden" id="eventid" value="${eventId}" />
<input type="hidden" id="status" value="${status}" />
<div id="menu">
	<div class="navbar navbar-static-top navbar-inverse">

	  	<div class="navbar-inner">
	  	  <div class="container">
	  	  	<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		      </a>
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
<div id="logininfo" class="pull-right">
	
</div>
	<div id="container" class="container">
		<div id="head">
			<ul class="breadcrumb pull-left">
	<!-- 			  <li><a href="./manager.html">事件列表</a> </li> -->
				  <li><a href="./manager.html">事件列表</a> <span class="divider">&gt;</span></li>
				  <li class="active">事件修改</li>
			</ul>

		</div>
		<div id="container-inner">
			<div class="row-fluid" style="margin-bottom: 30px;">
				<div class="row-wrapper">
					<div class="span12 event-title-container">
						<div class="event-title-inner">
							<div class="event-title">
								
								<div  class="span8 event-brief">
									<h3 class="sub">事件修改</h3>
									<form id="uploadImg" action="/gossip-server/imageUpload" enctype="multipart/form-data" method="POST">
									    <label>上传图片:</label>
									    <input type="hidden" name="eventId" value="${eventId}" />
									    <div style="width:512px;height:35px;">
									    <div id="eventImage">
									     <input type="file" name="file" id="uploadFile"/>   
                                         <input id="submitImg" type="submit" value="提交图片"/>
                                        </div>
                                         <div id="imageStatus">
                                             <label id="imgstatusDis" style="width:150px;height:25px;"></label> 
                                         </div>
									    </div>
									     
									</form>
										    
    		                                <label>事件标题:</label><input id="eventTitle" type="text" class="event-revise-name" value=""  style="width:500px;height:25px;border:#9a9a9a 2px solid"/>
    		                                <label>关键词:</label><input id="eventKeywords" type="text"  value="" style="width:500px;height:25px;border:#9a9a9a 2px solid">
    		                                <label>事件发生地点:</label><input id="eventLocation" type="text"  value="" style="width:500px;height:25px;border:#9a9a9a 2px solid">
    		                                <label>事件内容:</label><textarea id="eventContent"  rows="10" style="width:500px;border:#9a9a9a 2px solid;"></textarea>
    		                                <label>事件新闻添加:</label><input id="addnews" type="text"  value="" style="width:500px;height:25px;border:#9a9a9a 2px solid">
                                          <button  id="submitChange" class="btn btn-primary" onClick="">提交修改</button>
    	                            
								</div>
							</div>
						</div>					
					</div>
				</div>
			</div>
			<div id="newsDelete" style="margin-bottom: 30px;">
				<div class="row-wrapper">
					<h3 class="sub" style="font-size:15px; font-weight:bold">事件新闻修改</h3>
					<div id="newslist">
						
						
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
	</div>
</body>
</html>