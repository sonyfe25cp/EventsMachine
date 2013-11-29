<#include "/common/template-head.ftl">
<div class="container">
	<div class="container-fluid">
		<div class="row-fluid">
		    <div class="span3">
				<#include "/common/left-nav.ftl">
			</div>
			<div class="span9">
				<table class="table">
					<tr>
						<th>ID</th>
						<th>Title</th>
						<th>Date</th>
						<th>Location</th>
						<th>People</th>
						<th>Keywords</th>
						<th>Options</th>
					</tr>
					<#list events as event>
					<tr>
						<td>${event.id}</td>
						<td>${event.title}</td>
						<td></td>
						<td></td>
						<td></td>
						<td>
							<a href="/admin/events/show/${event.id}">Show</a>
							<a href="/admin/events/edit/${event.id}">Edit</a>
						</td>
					</tr>
					</#list>
				</table>
			</div>
		</div>
	</div>
	<#include "/common/template-bottom.ftl">
</div>