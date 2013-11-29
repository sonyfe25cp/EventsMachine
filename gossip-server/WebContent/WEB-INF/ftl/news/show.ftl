<#include "/common/template-head.ftl">
	<div class="container">
		<#include "/head.ftl">
		<legend>${news.title}</legend>
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