function parse(){
		var data = arguments[0].events;		

		for (var i = 0; i < data.length; i++) {			
			// $("#primary .list li")ind("h2").get(0).innerHtml=getEvent(data[i],"title");
			var eventDom=$('<li class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2>	<br></div><div class="info"><span class="pull-right"></span></div>');
			eventDom.find("h2").html('<a href="./event.html" title="'+getEvent(data[i],"title")+'">'+getEvent(data[i],"title")+'</a>');
			$('<p>'+getEvent(data[i],"desc")+'</p>').insertAfter(eventDom.find(".article-content br"));	
			var info="特征词：";
			$.each(getEvent(data[i], "keywords"),function(index,ele){info+='<a class="label">'+ele+'</a> ';});
			info = info+'<span class="pull-right">时间：'+getEvent(data[i],"started_at")+'</span>';	
			eventDom.find(".info").html(info)
			eventDom.appendTo(".list");	
		};
}

function getEvent(obj,attr){
	return obj[attr];
}
		
	
	


$(function(){

	var script = document.createElement('script');
	script.src = 'http://10.1.1.94:8080/jsonserver/json?callback=parse';
	script.type = 'text/javascript';
	$("head").get(0).appendChild(script);
	// $.getJSON("http://10.1.1.94:8080/jsonserver/json?callback=parse",function(data){					
	// 		//$("#primary .list li").find("h2").get(0).innerHtml=getEventTitle(data,0);
	// 		//parse(data);
	// 	},"json").success(function() { alert("second success"); }).error(function() { alert("error"); }).complete(function() { alert("complete"); });
	
});