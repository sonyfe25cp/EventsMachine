<#include "/common/template-head.ftl">
    <div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li class="active">新闻搜索</li>
		</ol>
		<div class ="jumbotron" style="text-align:center">
		   <form action = "/news-q">
		     <div style="text-align:center">
		       <span style="display: inline-block;vertical-align: top;">
		          <input type = "text" name = "queryWords" id = "kw" maxlength = "100" autocomplete = "off" style="font-size:18px;height:35px;width:397px;margin-top:2px;">
		       </span>
		       <span style="display: inline-block;z-index: 0;vertical-align: top;">
		          <input type = "submit" value = "搜索新闻" id = "su" style="height:35px;font-size:18px;background-color:#DDD;margin-bottom:2px;">
		       <span >
		     </div>
		   </form>
		</div>
	</div>