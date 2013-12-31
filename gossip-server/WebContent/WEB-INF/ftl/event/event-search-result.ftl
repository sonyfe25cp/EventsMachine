<#include "/common/template-head.ftl">
<div class="container">
	<#include "/head.ftl">
	<ol class="breadcrumb">
	  <li><a href="/">新闻系统</a></li>
	  <li class="active">搜索结果</li>
	</ol>
	<div class="row">
		<div class="col-md-9">
			<div>
				<legend>搜索条件:</legend>
			</div>
			<#include "/event/event-list-partial.ftl">
		</div>
		<div class="col-md-3">
			<#include "/event/event-list-right-partial.ftl">
		</div>
	</div>
</div>
<#include "/common/template-bottom.ftl">