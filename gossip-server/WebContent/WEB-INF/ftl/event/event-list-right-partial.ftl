<script type="text/javascript" src="/js/jquery-ui-1.10.3.custom.min.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<script>
	$(document).ready(function(){
		$("#eventCalendar").datepicker({
			dateFormat: "yy-mm-dd",
			onSelect: function(){
				//alert();
				//windows.href="/events.html?date=";
				//alert($("#eventCalendar").datepicker("getDate"));
			}
		});
	});
</script>
<div>
	<legend>事件日历</legend>
	<div id="eventCalendar"></div>
</div>
<legend></legend>
<div>
	<legend>关键字搜索</legend>
	<form action="/q">
		<input class="input span3" name="keyword"></input>
		<input class="btn btn-primary" type="submit" value="搜索"></input>
	</form>
</div>
<legend></legend>