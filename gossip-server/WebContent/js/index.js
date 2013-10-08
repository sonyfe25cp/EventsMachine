$(document).ready(function() {
    var currentPageNum=1; 
    $.ajax({
    type: "GET",
    contentType: "application/json; charset=utf-8",
    url:"/Gossip-server/events?pageNo=" + currentPageNum,
    dataType: "json",
    anysc: false,
    success: function(result) {
    	display(result);
    }
  });
	 
//	 /*下面是获取hotPeople、hotPlaces、hotKeywords*/
//	 $('#sidebar').empty();
//	 $.get('/Gossip-server/hot-words', function(data) {
//	    	var hotKeywords=data.hotKeywords;
//	    	if(hotKeywords==null||hotKeywords.length<1)
//	    		return;
//		      var html = '<div class="sidemodule">';
//		      html += '<h3 class="sub">热门关键词</h3>';
//
//		      for(var i = 0; i < hotKeywords.length; i++) {
//		        html += '           <a href="#" class="label">' + hotKeywords[i] + '</a>                                                                                                                                                                                           ';
//		      }
//		      html+= '</div>';
//		      $('#sidebar').append(html);
//	    });//hotKeywords
//	 
//	 
//	 $.get('/Gossip-server/hot-people', function(data) {
//	    	var hotPeople=data.hotPeople;
//	    	if(hotPeople==null||hotPeople.length<1)
//	    		return;
//	    	 //$('#sidebar div:eq(1)').empty();
//		      var html = '<div class="sidemodule">';
//		      html += '<h3 class="sub">热门人名</h3>';
//
//		      for(var i = 0; i < hotPeople.length; i++) {
//		        html += '           <a href="#" class="label label-inverse">' + hotPeople[i] + '</a>                                                                                                                                                                                           ';
//		      }
//		      html+= '</div>';
//		      $('#sidebar').append(html);
//	    });//hotPeople
//	 
//	 
//	 $.get('/Gossip-server/hot-places', function(data) {
//	    	var hotPlaces=data.hotPlaces;
//	    	if(hotPlaces==null||hotPlaces.length<1)
//	    		return;
//	    	 //$('#sidebar div:eq(2)').empty();
//		      var html = '<div class="sidemodule">';
//		      html += '<h3 class="sub">热门地名</h3>';
//
//		      for(var i = 0; i < hotPlaces.length; i++) {
//		        html += '           <a href="#" class="label label-info">' + hotPlaces[i] + '</a>                                                                                                                                                                                           ';
//		      }
//		      html+= '</div>';
//		      $('#sidebar').append(html);
//	    });//hotPlaces
	 
});//document

function display(result){
	$('#primary .list').empty();
	var events = result.events;
	for(var i = 0; i< events.length; i++){
		var event = events[i];
		var eventDom = $('<li id="' + event.id + '" class="article"><div class="article-content"><h2></h2> <br></div><div class="info"><span class="pull-right"></span></div></li>');
		
		/** /Gossip-server/event? 请求在页面跳转action： pageSwitchAction里面  **/
		eventDom.find("h2").html('<a href="/Gossip-server/event?eventId=' + event.id  + '">' + event.title + '</a>');
		$('<p><a href="/Gossip-server/event?eventId=' + event.id+'">' + event.desc + '</a></p>').insertAfter(eventDom.find(".article-content br"));
	    var info = "&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
	    var keyword=splitKeywords(event.keywords);
	    keyword=keyword.split(",");
	    for(var j = 0; j < keyword.length; j++){
	    	info += "&nbsp;"+"&nbsp;"+'<a class="label">' + keyword[j] + '</a>';  
	    }  
	    info = info + '<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;' + dateConvert(event.started_at) + '</span>';
	    eventDom.find(".info").html(info);
	    eventDom.appendTo("#primary .list");
	}
	//页码和翻页功能
	pagination(result);
}//display

//页码和翻页功能实现
function pagination(result){
	$('#page ul').empty();
	var pageNo = result.pageNo;
	var totalPage = result.totalPage;
	var beginNo = result.pageBegin;
	var endNo = result.pageEnd;
	var html = "";
	//首页和上一页的设置
	if(pageNo == 1){
		html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
		html += '<li id="pre_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
	}
	else{
		html = '<li id="first_page"><a href="#" onclick="toPage(1)">首页</a></li>';
        html += '<li id="pre_page"><a href="#" onclick="toPage(' + (pageNo - 1) +')">«</a></li>';
	}
	//中间页的设置
	for(var i = beginNo; i<= endNo; i++){
		if(i == pageNo)
			html += '<li id="page' + i + '" class="disabled"><a href="javascript:void(0)">' + i + '</a></li>';
		else 
			html += '<li id="page' + i + '"><a href="#" onclick="toPage(' + i + ')">' + i + '</a></li>';
	}
	//末页和下一页的设置
	if(pageNo == totalPage){
		html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">' + '»</a></li>';
        html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">' + '末页</a></li>';
	}
	else{
		html += '<li id="next_page" class=""><a href="#" onclick="toPage(' + (pageNo+1) + ')">»</a></li>';
        html += '<li id="final_page" class=""><a href="#" onclick="toPage(' + totalPage + ')">末页</a></li>';
	}
	$('#page ul').append(html);
}//pagination

//跳转页面
function toPage(pageNo){
	$('#primary .list').empty();
	$.ajax({
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/Gossip-server/events?pageNo=" + pageNo,
	    dataType: "json",
	    anysc: false,
	    success: function(result) {
	    	display(result);
	    }
	  });
}//toPage


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
