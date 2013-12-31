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