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
				<span class="label label-success">
					重要程度排序
				</span>
				&nbsp;
				<span class="label label-default">时间降序</span>
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