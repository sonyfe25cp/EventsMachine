<#include "/common/template-head.ftl">
	<div class="container">
		<#include "/head.ftl">
		<ol class="breadcrumb">
		  <li><a href="/">新闻系统</a></li>
		  <li class="active">新闻列表</li>
		</ol>
		<#list newsList as news>
			<div class="jumbotron">
				<h3>
					<a href="/news/${news.id}.html">${news.title}</a>
				</h3>
				<p>
				    <#if (news.body?length > 200)>
						${news.body?substring(0,200)} .......
					<#else>
							${news.body}
					</#if>
				</p>
			</div>
		</#list>
	</div>
<#include "/common/template-bottom.ftl">