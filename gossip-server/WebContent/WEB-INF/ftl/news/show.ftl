<#include "/common/template-head.ftl">
	<div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li><a href="/events.html">事件列表</a></li>
		  <li class="active">新闻内容</li>
		</ol>
		<legend>${news.title}</legend>
		原文链接: <a href=${news.url} target="_blank">${news.url}</a>
		<div class="jumbotron">
			<p>${news.body}</p>
			<small>
				<#if news.keyWords??>
					${news.keyWords}
				</#if>
			</small>
		</div>
	</div>
<#include "/common/template-bottom.ftl">