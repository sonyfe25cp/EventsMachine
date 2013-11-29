<#include "/common/template-head.ftl">
	<div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li class="active">事件列表</li>
		</ol>
		<#list events as event>
			<div class="jumbotron">
				<h3>
					<a href="/event/${event.id}.html">${event.title}</a>
				</h3>
				<p>${event.desc}</p>
				<small>
				<#if event.keyWords??>
					${event.keyWords}
				</#if>
				</small>
			</div>
		</#list>
	</div>
<#include "/common/template-bottom.ftl">