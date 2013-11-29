<#include "/common/template-head.ftl">
	<div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li><a href="/events.html">事件列表</a></li>
		  <li class="active">事件</li>
		</ol>
		<legend>${event.title}</legend>
		<div class="jumbotron">
			<p>${event.desc}</p>
			<small>
				<#if event.keyWords??>
					${event.keyWords}
				</#if>
			</small>
		</div>
		<legend>新闻列表</legend>
		<div>
			<#list event.newsList as news>
				<div>
					<h4>
						<a href="/news/${news.id}.html">${news.title}</a>
					</h4>
					<p>
						<#if (news.body?length > 300)>
							${news.body?substring(0,300)} .......
						<#else>
							${news.body}
						</#if>
					</p>
				</div>
			</#list>
		</div>
	</div>
<#include "/common/template-bottom.ftl">