<#include "/common/template-head.ftl">
	<div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li><a href="/events.html">事件列表</a></li>
		  <li class="active">事件详情</li>
		</ol>
		<legend>${event.title}</legend>
		<div class="jumbotron">
			<#include "/event/event-meta.ftl">
		</div>
		<legend>新闻列表</legend>
		<div>
			<#list event.newsList as news>
				<div>
					<div>
						<h4>
							<a href="/news/${news.id}.html">${news.title}</a>
						</h4>
						<span>新闻日期: ${news.date}<span>
						<span>新闻来源: ${news.fromSite}</span>
					</div>
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