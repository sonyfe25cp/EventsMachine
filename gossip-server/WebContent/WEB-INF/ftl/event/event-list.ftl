<#include "/common/template-head.ftl">
<div class="container">
	<#include "/head.ftl">
	<ol class="breadcrumb">
	  <li><a href="/events.html">新闻系统</a></li>
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
			<#include "/event/event-list-partial.ftl">
		</div>
		<div class="col-md-3">
			<#include "/event/event-list-right-partial.ftl">
		</div>
	</div>
</div>
<#include "/common/template-bottom.ftl">