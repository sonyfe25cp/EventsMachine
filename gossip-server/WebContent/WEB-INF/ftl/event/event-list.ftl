<#include "/common/template-head.ftl">
<div class="container">
	<#include "/head.ftl">
	<ol class="breadcrumb">
	  <li><a href="/">新闻系统</a></li>
	  <li class="active">事件列表</li>
	</ol>
	<div class="row">
		<div class="col-md-9">
			<div>
				<a href="/events.html?sort=importance">
					<#if (!sort??) || sort == 'importance'>
						<span class="label label-success">
					<#else>
						<span class="label label-default">
					</#if>
						重要程度排序
					</span>
				</a>
				&nbsp;
				<a href="/events.html?sort=time">
					<#if sort?? && sort == 'time'>
						<span class="label label-success">
					<#else>
						<span class="label label-default">
					</#if>
						时间降序
					</span></a>
				<p/>
			</div>
			<#list events as event>
				<div class="jumbotron">
					<h3>
						<a href="/event/${event.id}.html">${event.title}</a>
					</h3>
					<#include "/event/event-meta.ftl">
				</div>
			</#list>
			<ul class="pagination">
			  <li><a href="#">&laquo;</a></li>
			  <li><a href="#">1</a></li>
			  <li><a href="#">2</a></li>
			  <li><a href="#">3</a></li>
			  <li><a href="#">&raquo;</a></li>
			</ul>
		</div>
		<div class="col-md-3">
			<div>
				<h3>今日热门词语</h3>
			</div>
		</div>
	</div>
</div>
	
	
<#include "/common/template-bottom.ftl">