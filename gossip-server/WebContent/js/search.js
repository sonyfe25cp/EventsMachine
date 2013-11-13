$(document).ready(function() {
	$("#search_btn1").click(function(){
		var queryWords = $("#queryWords1").val();
		//如果查询词为空，则什么也不做
		if(queryWords == null || queryWords.trim() == ""){
			alert("查询词为空");
			return;
		}
		//若查询词不为空，则返回查询结果
		var pageNo = 1;//开始时结果页默认为1
		search11(pageNo, queryWords);
		search21(pageNo, queryWords);
	});//$("#search_btn").click
	
	$("#search_btn2").click(function(){
		var queryWords = $("#queryWords2").val();
		//如果查询词为空，则什么也不做
		if(queryWords == null || queryWords.trim() == ""){
			alert("查询词为空");
			return;
		}
		//若查询词不为空，则返回查询结果
		var pageNo = 1;//开始时结果页默认为1
		search11(pageNo, queryWords);
		search21(pageNo, queryWords);
	});//$("#search_btn").click
	
});//document

function search11(pageNo, queryWords){
	$.ajax({
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/Gossip-server/q?pageNo="+pageNo+"&queryWords="+queryWords,
	    //data:{pageNo: pageNo, queryWords: queryWords},
	    dataType: "json",
	    anysc: false,
	    success: function(result) {
	        //显示查询结果
	        listResults1(result);
	    }//ajax success
	  });//$.ajax
}

function search12(pageNo, queryWords){
	$.ajax({
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/Gossip-server/q?pageNo="+pageNo+"&queryWords="+queryWords,
	    //data:{pageNo: pageNo, queryWords: queryWords},
	    dataType: "json",
	    anysc: false,
	    success: function(result) {
	        //显示查询结果
	        listResults1(result);
	    }//ajax success
	  });//$.ajax
}

function search21(pageNo, queryWords){
	$.ajax({
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/Gossip-server/q?pageNo="+pageNo+"&queryWords="+queryWords,
	    //data:{pageNo: pageNo, queryWords: queryWords},
	    dataType: "json",
	    anysc: false,
	    success: function(result) {
	        //显示查询结果
	        listResults2(result);
	    }//ajax success
	  });//$.ajax
}

function search22(pageNo, queryWords){
	$.ajax({
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/Gossip-server/q?pageNo="+pageNo+"&queryWords="+queryWords,
	    //data:{pageNo: pageNo, queryWords: queryWords},
	    dataType: "json",
	    anysc: false,
	    success: function(result) {
	        //显示查询结果
	        listResults2(result);
	    }//ajax success
	  });//$.ajax
}

function listResults1(result){
	var eventsList = result.events;
	$('#primary1 .list').empty();
	for(var i = 0; i< eventsList.length; i++){
		var event = eventsList[i];
		var eventDom = $('<li id="' + event.id + '" class="article"><div class="article-content"><h2></h2> <br></div><div class="info"><span class="pull-right"></span></div></li>');
		eventDom.find("h2").html('<a href="/Gossip-server/event?eventId=' + event.id  + '">' + event.title + '</a>');
		$('<p><a href="/Gossip-server/event?eventId=' + event.id+'">' + event.desc + '</a></p>').insertAfter(eventDom.find(".article-content br"));
	    var info = "&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
	    var keyword=splitKeywords(event.keywords);
	    keyword=keyword.split(",");
	    //alert(keyword.length);
	    var keyWordsLen = keyword.length;
	    for(var j=0;j<keyWordsLen;j++){
	    	//alert("j= " + j);
	    	info += "&nbsp;"+"&nbsp;"+'<a class="label">' + keyword[j] + '</a>';  
	    }  
	    info = info + '<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;' + dateConvert(event.started_at) + '</span>';
	    eventDom.find(".info").html(info);
	    eventDom.appendTo("#primary1 .list");
	}
	//页码和翻页功能
	pagination1(result);
	
}//listResult

function listResults2(result){
	var eventsList = result.events;
	$('#primary2 .list').empty();
	for(var i = 0; i< eventsList.length; i++){
		var event = eventsList[i];
		var eventDom = $('<li id="' + event.id + '" class="article"><div class="article-content"><h2></h2> <br></div><div class="info"><span class="pull-right"></span></div></li>');
		eventDom.find("h2").html('<a href="/Gossip-server/event?eventId=' + event.id  + '">' + event.title + '</a>');
		$('<p><a href="/Gossip-server/event?eventId=' + event.id+'">' + event.desc + '</a></p>').insertAfter(eventDom.find(".article-content br"));
	    var info = "&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
	    var keyword=splitKeywords(event.keywords);
	    keyword=keyword.split(",");
	    //alert(keyword.length);
	    var keyWordsLen = keyword.length;
	    for(var j=0;j<keyWordsLen;j++){
	    	//alert("j= " + j);
	    	info += "&nbsp;"+"&nbsp;"+'<a class="label">' + keyword[j] + '</a>';  
	    }  
	    info = info + '<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;' + dateConvert(event.started_at) + '</span>';
	    eventDom.find(".info").html(info);
	    eventDom.appendTo("#primary2 .list");
	}
	//页码和翻页功能
	pagination2(result);
	
}//listResult



function pagination1(result){
	$('#page1 ul').empty();
	var queryWords = result.queryWords;
	var pageNo = result.pageNo;
	var pageCount = result.totalPage;
	var beginNo = result.pageBegin;
	var endNo = result.pageEnd;
	var html = "";
	
	//首页和上一页的设置
	if(pageNo == 1){
		html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
		html += '<li id="pre_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
	}
	else{
		html = '<li id="first_page"><a href="#" onclick=search12("1' + '","' + queryWords + '")>首页</a></li>';
        html += '<li id="pre_page"><a href="#" onclick=search12("' + (pageNo-1) + '","' + queryWords + '")>'+'«</a></li>';
	}
	//中间页的设置
	for(var i = beginNo; i<= endNo; i++){
		if(i == pageNo)
			html += '<li id="page'+i+'" class="disabled"><a href="javascript:void(0)">' + i + '</a></li>';
		else 
			html += '<li id="page'+i+'"><a href="#" onclick=search12("' + i + '","' + queryWords + '")>'+ i +'</a></li>';
	}
	//末页和下一页的设置
	if(pageNo == pageCount){
		html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">'+'»</a></li>';
        html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">'+'末页</a></li>';
	}
	else{
		html += '<li id="next_page" class=""><a href="#" onclick=search12("' + (pageNo+1) + '","' + queryWords + '")>'+'»</a></li>';
        html += '<li id="final_page" class=""><a href="#" onclick=search12("' + pageCount + '","' + queryWords + '")>'+'末页</a></li>';
	}
	$('#page1 ul').append(html);
}

function pagination2(result){
	$('#page2 ul').empty();
	var queryWords = result.queryWords;
	var pageNo = result.pageNo;
	var pageCount = result.totalPage;
	var beginNo = result.pageBegin;
	var endNo = result.pageEnd;
	var html = "";
	
	//首页和上一页的设置
	if(pageNo == 1){
		html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
		html += '<li id="pre_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
	}
	else{
		html = '<li id="first_page"><a href="#" onclick=search22("1' + '","' + queryWords + '")>首页</a></li>';
        html += '<li id="pre_page"><a href="#" onclick=search22("' + (pageNo-1) + '","' + queryWords + '")>'+'«</a></li>';
	}
	//中间页的设置
	for(var i = beginNo; i<= endNo; i++){
		if(i == pageNo)
			html += '<li id="page'+i+'" class="disabled"><a href="javascript:void(0)">' + i + '</a></li>';
		else 
			html += '<li id="page'+i+'"><a href="#" onclick=search22("' + i + '","' + queryWords + '")>'+ i +'</a></li>';
	}
	//末页和下一页的设置
	if(pageNo == pageCount){
		html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">'+'»</a></li>';
        html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">'+'末页</a></li>';
	}
	else{
		html += '<li id="next_page" class=""><a href="#" onclick=search22("' + (pageNo+1) + '","' + queryWords + '")>'+'»</a></li>';
        html += '<li id="final_page" class=""><a href="#" onclick=search22("' + pageCount + '","' + queryWords + '")>'+'末页</a></li>';
	}
	$('#page2 ul').append(html);
}


//返回的keyWords是形如{奶粉:2.0;机构:2.0;已向:1.0;检出:1.0;果无:1.0;}的字符串，因此需要转换
function splitKeywords(keyWords){
	var key=keyWords.split(";");
	var allKeywords="";
	for(var i=0;i<key.length;i++){
		var keyWord=key[i].split(":");
		if(keyWord[0]==null||keyWord[0].length<=1||keyWord[0]=="null")
			continue;
		else{
			if(allKeywords=="")
				allKeywords+=keyWord[0];
			else
				allKeywords=allKeywords+","+keyWord[0];
		}
	}

	return allKeywords;
	
}

//时间转换，从long型转化为形如“2012年11月11日”的格式
function dateConvert(longTime){
	var date=new Date(longTime);
	var year = date.getFullYear();
	var month = date.getMonth()+1; //js从0开始取 
	var date1 = date.getDate(); 
	var hour = date.getHours(); 
	var minutes = date.getMinutes(); 
	var second = date.getSeconds();
    var timeString=year+"-"+month+"-"+date1;
    return timeString;
}