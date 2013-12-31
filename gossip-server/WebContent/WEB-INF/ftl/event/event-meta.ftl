<p>${event.desc}</p>
<small>
	<#if event.keyWords??>
		<strong>关键词:</strong> ${event.keyWords}
	</#if>
</small>
</p>
<small>
	<#if event.startedLocation??>
		<strong>发生地点:</strong> <a href="/q?keyword=${event.startedLocation}">${event.startedLocation}</a>
	&nbsp;
	</#if>
	<strong>发生时间:</strong> <a href="/q?createAt=${event.createAt?date}">${event.createAt?date}</a>
	&nbsp;
	<#if event.updateAt??>
		<strong>更新时间:</strong> <a href="/q?updateAt=${event.updateAt?date}">${event.updateAt?date}</a>
	&nbsp;
	</#if>
	<#if event.importantPeople??>
		<strong>重要人物:</strong> ${event.importantPeople}
	</#if>
</small>