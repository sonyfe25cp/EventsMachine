<p>${event.desc}</p>
<small>
	<#if event.keyWords??>
		<strong>关键词:</strong> ${event.keyWords}
	</#if>
</small>
</p>
<small>
	<#if event.startedLocation??>
		<strong>发生地点:</strong> ${event.startedLocation}
	&nbsp;
	</#if>
	<strong>发生时间:</strong> ${event.createAt?date}
	&nbsp;
	<#if event.updateAt??>
		<strong>更新时间:</strong> ${event.updateAt?date}
	&nbsp;
	</#if>
	<#if event.importantPeople??>
		<strong>重要人物:</strong> ${event.importantPeople}
	</#if>
</small>