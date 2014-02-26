<#list events as event>
	<div class="jumbotron">
		<h3>
			<a href="/event/${event.id}.html">${event.title}</a>
		</h3>
		<#include "/event/event-meta.ftl">
	</div>
</#list>
<ul class="pagination">
	<#if (pageNo - 1 > -1)>
  		<li><a href="/events.html?sort=${sort}&pageNo=${pageNo-1}">前一页</a></li>
  	</#if>
  	<li><a href="#">当前是第${pageNo}页</a></li>
  <li><a href="/events.html?sort=${sort}&pageNo=${pageNo+1}">下一页</a></li>
</ul>