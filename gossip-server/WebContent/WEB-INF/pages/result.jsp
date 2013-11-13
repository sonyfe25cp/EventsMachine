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
	  	  	<a class="brand" href="#"><img src="images/logo3.gif" alt="新闻系统"></a>
	  	  	<ul class="nav">
	  	  	  <li class="active"><a href="">返回主页</a></li>
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
		<div id="container-inner search-container" style="padding: 0px 0px 40px 20px;">
			<div class="row search-head">				
				<div class="span12 ">
					<form action="">
						<input type="text" name="wd" id="kw" maxlength="100" style="width:474px; margin-top:5px;margin-bottom: 0" autocomplete="off" >
						<input type="submit" value="搜索一下" id="su" class="btn" style="margin-top: 6px;">
					</form>
				</div>
				
			</div>
			<div class="row result-wrapper">
				<div class="span8 result event-result">
					<h2>相关事件</h2>
					<ul id="event-list">
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜索体验：给<em>搜索结果分类</em>-搜索引擎周边</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">Google 正在测试一种新的SERPs 形式——将第一页的<em>搜索结果分类</em>。  Philipp 的一位读者告诉他，在Google 里搜索“buffalo bill silence”时，网页 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="www.100ec.cn/detail--6046578.html" target="_blank" class="l">搜狗推出<em>分类</em>搜索对<em>搜索结果</em>页做出改进搜狗搜索引擎关键词中国 ...</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.100ec.cn/detail--6046578.html</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">作者：中国电子商务研究中心编辑, 来源：中国电子商务研究中心, 发布时间：2012年07月09日09:24 , 内容<em>分类</em>：<em>搜索</em>动态/1230 搜狗<em>搜索</em>引擎关键词.<br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜狗推出“<em>分类搜索</em>” 日均节省用户10亿秒- 人民聊吧</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">近日，搜狗搜索对<em>搜索结果</em>页面进行重大改进，推出了<em>分类</em>搜索模式，这是继识图搜索，全搜索之外搜狗搜索又一技术创新。通过该技术，搜狗搜索 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">下架测试 - 威锋Cydia源– 所有资源/<em>分类</em>/<em>搜索结果</em></a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">iPhone在Cydia上输入：http://apt.weiphone.com加入源，进入“我的收藏”可以有个人专属源喔~. 支持类型: iPad+iPhone iPad iPhone; <em>分类</em>: 所有<em>分类</em> 软件APP 主题 ...<b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜索体验：给<em>搜索结果分类</em>-搜索引擎周边</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">Google 正在测试一种新的SERPs 形式——将第一页的<em>搜索结果分类</em>。  Philipp 的一位读者告诉他，在Google 里搜索“buffalo bill silence”时，网页 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
					</ul>

					<div class="pagination pagination-centered">
		             <ul>
		                <li class="disabled"><a href="#">«</a></li>
		                <li class="active"><a href="#">1</a></li>
		                <li><a href="#">2</a></li>
		                <li><a href="#">3</a></li>
		                <li><a href="#">4</a></li>
		                <li><a href="#">5</a></li>
		                <li><a href="#">»</a></li>
		             </ul>
            		</div>
				</div>
				<div class="result-space"></div>
				<div class="span8 result event-result">
					<h2>相关新闻</h2>
					<ul id="news-list">
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜索体验：给<em>搜索结果分类</em>-搜索引擎周边</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">Google 正在测试一种新的SERPs 形式——将第一页的<em>搜索结果分类</em>。  Philipp 的一位读者告诉他，在Google 里搜索“buffalo bill silence”时，网页 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="www.100ec.cn/detail--6046578.html" target="_blank" class="l">搜狗推出<em>分类</em>搜索对<em>搜索结果</em>页做出改进搜狗搜索引擎关键词中国 ...</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.100ec.cn/detail--6046578.html</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">作者：中国电子商务研究中心编辑, 来源：中国电子商务研究中心, 发布时间：2012年07月09日09:24 , 内容<em>分类</em>：<em>搜索</em>动态/1230 搜狗<em>搜索</em>引擎关键词.<br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜狗推出“<em>分类搜索</em>” 日均节省用户10亿秒- 人民聊吧</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">近日，搜狗搜索对<em>搜索结果</em>页面进行重大改进，推出了<em>分类</em>搜索模式，这是继识图搜索，全搜索之外搜狗搜索又一技术创新。通过该技术，搜狗搜索 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">下架测试 - 威锋Cydia源– 所有资源/<em>分类</em>/<em>搜索结果</em></a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">iPhone在Cydia上输入：http://apt.weiphone.com加入源，进入“我的收藏”可以有个人专属源喔~. 支持类型: iPad+iPhone iPad iPhone; <em>分类</em>: 所有<em>分类</em> 软件APP 主题 ...<b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜索体验：给<em>搜索结果分类</em>-搜索引擎周边</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">Google 正在测试一种新的SERPs 形式——将第一页的<em>搜索结果分类</em>。  Philipp 的一位读者告诉他，在Google 里搜索“buffalo bill silence”时，网页 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
					</ul>
					<div class="pagination pagination-centered">
		             <ul>
		                <li class="disabled"><a href="#">«</a></li>
		                <li class="active"><a href="#">1</a></li>
		                <li><a href="#">2</a></li>
		                <li><a href="#">3</a></li>
		                <li><a href="#">4</a></li>
		                <li><a href="#">5</a></li>
		                <li><a href="#">»</a></li>
		             </ul>
            		</div>

				</div>
				<div class="result-space-last"></div>
				<div class="span8 result event-result">
					<h2>人名特征词</h2>
					<ul id="keywords-list">
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜索体验：给<em>搜索结果分类</em>-搜索引擎周边</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">Google 正在测试一种新的SERPs 形式——将第一页的<em>搜索结果分类</em>。  Philipp 的一位读者告诉他，在Google 里搜索“buffalo bill silence”时，网页 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="www.100ec.cn/detail--6046578.html" target="_blank" class="l">搜狗推出<em>分类</em>搜索对<em>搜索结果</em>页做出改进搜狗搜索引擎关键词中国 ...</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.100ec.cn/detail--6046578.html</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">作者：中国电子商务研究中心编辑, 来源：中国电子商务研究中心, 发布时间：2012年07月09日09:24 , 内容<em>分类</em>：<em>搜索</em>动态/1230 搜狗<em>搜索</em>引擎关键词.<br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜狗推出“<em>分类搜索</em>” 日均节省用户10亿秒- 人民聊吧</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">近日，搜狗搜索对<em>搜索结果</em>页面进行重大改进，推出了<em>分类</em>搜索模式，这是继识图搜索，全搜索之外搜狗搜索又一技术创新。通过该技术，搜狗搜索 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">下架测试 - 威锋Cydia源– 所有资源/<em>分类</em>/<em>搜索结果</em></a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">iPhone在Cydia上输入：http://apt.weiphone.com加入源，进入“我的收藏”可以有个人专属源喔~. 支持类型: iPad+iPhone iPad iPhone; <em>分类</em>: 所有<em>分类</em> 软件APP 主题 ...<b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
						<li class="result-item">
							<div class="result-container">
								<h3 class="result-title"><a href="http://www.baidu.com" target="_blank" class="l">搜索体验：给<em>搜索结果分类</em>-搜索引擎周边</a></h3>
								<div class="result-detail">
									<div class="result-info">
										<cite><span class="bc">www.eryi.org</span></cite>
										<span class="f">2007年4月17日</span>
									</div>
									<div class="result-text">
										<span class="st">Google 正在测试一种新的SERPs 形式——将第一页的<em>搜索结果分类</em>。  Philipp 的一位读者告诉他，在Google 里搜索“buffalo bill silence”时，网页 <b>...</b><br></span>
									</div>
								</div>
							</div>
						</li>
					</ul>
					<div class="pagination pagination-centered">
		             <ul>
		                <li class="disabled"><a href="#">«</a></li>
		                <li class="active"><a href="#">1</a></li>
		                <li><a href="#">2</a></li>
		                <li><a href="#">3</a></li>
		                <li><a href="#">4</a></li>
		                <li><a href="#">5</a></li>
		                <li><a href="#">»</a></li>
		             </ul>
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