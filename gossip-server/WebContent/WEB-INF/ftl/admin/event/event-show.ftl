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
						<td>ID</td>
						<td>${event.id}</td>
					</tr>
					<tr>
						<td>Img</td>
						<td>
							<#if event.img??>
								<img src=${event.img}/>
							</#if>
						</td>
					</tr>
					<tr>
						<td>Title</td>
						<td>${event.title}</td>
					</tr>
					<tr>
						<td>Summary</td>
						<td>${event.desc}</td>
					</tr>
					<tr>
						<td>startedLocation</td>
						<td>
							<#if event.startedLocation??>
								${event.startedLocation}
							</#if>
						</td>
					</tr>
				</table>
				<div class="news">
					<div class="group">
						<#list event.newsList as news>
							<div class="item">
								<div class="title">${news.title}</div>
								<!--<div class="content">${news.body}</div> -->
							</div>
						</#list>
					</div>
				</div>
			</div>
		</div>
	</div>
	<#include "/common/template-bottom.ftl">
</div>