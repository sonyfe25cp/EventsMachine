$(document).ready(function() {
	//获取了id
	var currentPageNum=0;
	thisURL = document.URL;
	var eveid = thisURL.split("=");
	var eventid=$("#eventid").val();
	url="/Gossip-server/events/"+eventid;
	
	$.ajax({
		type: "GET",
		contentType: "application/json; charset=utf-8",
		url:url,
		dataType: "json",
		anysc: true,
		//data: datt,
		success: function(result) {
			display(result);
		}//success
	});//ajax
	
	
	//相关事件的显示
//	var url1="/Gossip-server/related-events?id="+eveid[1]+"&pageNo=1&limit=10";
//	$.ajax({
//		type: "GET",
//		contentType: "application/json; charset=utf-8",
//		url:url1,
//		dataType: "json",
//		anysc: false,
//		//data: data,
//		success: function(result) {
//			var datad = result.events;
//			$('#relate').empty();
//			var html = "";
//			for(var ir = 0; ir < datad.length; ir++) {
//				html += '<a href="/Gossip-server/events?id=' + datad[ir].id + '" class="span3">' + datad[ir].title + ' </a>';
//			}
//
//			$('#relate').append(html);
//
//		}//sucess
//	});//ajax
	var timelineHeight = $("#timelinecontent").height();
	$("#timelinebar").height(timelineHeight);
});//document	

function display(eventInfo){
	$('#container-inner .event-title').empty();
	var html;
	if(eventInfo['img']==""||eventInfo['img']==null)
		html = '<div class="span4"><img src="' + 'eventImg/none.jpg' + '" alt=""></div>';
	else
		html = '<div class="span4"><img src="' + eventInfo['img'] + '" alt=""></div>';
	
	html += '<div class="span8 event-brief">';
	html += '<h2>' + eventInfo['title'] + '</h2>';
	html += '<p>' + eventInfo['desc'] + '</p>';
	html += '</div>';
	$('#container-inner .event-title').append(html);
	//显示事件新闻
	var newsIdList = eventInfo['news'];
	$.ajax({
		type: "GET",
		contentType: "application/json; charset=utf-8",
		url:"/Gossip-server/news/getEventNews?newsIdList="+newsIdList,
		dataType: "json",
		anysc: true,
		success: function(result) {
			newsJson = result;
			displayEventNews(1, newsIdList, result);
			}//success
	});//ajax
}//display

function displayEventNews(pageNo, newsIdList, result){
	var eventNews = result.news;
	//alert(pageNo);
	var total = eventNews.length;
	var numPerPage = 10;
	var start = (pageNo - 1)*numPerPage;
	var end0 = pageNo*numPerPage;
	var end = total > end0 ? (end0 - 1) : (total - 1);
	$('#timelinecontent').empty();
	for(var i = start; i <= end; i++){
		if(i == start||(eventNews[i].date!=eventNews[i-1].date)){
			 generateTimeLine(eventNews[i].date);
			 insertNews(eventNews[i]);
		}
		else
			insertNews(eventNews[i]);
	}
	pagination(pageNo, newsIdList, result);
}

//当新闻的时间与前一个不同时，必须生成新的时间线
function generateTimeLine(newsDate){
	var html = '						<div class="news" >';
	html += '							<span class="news-time">' + newsDate + '</span>';
	html += '							<div class="news-list">';
	html += '								<i>';
	html += '								</i>';
	html += '								<ul  id="' + newsDate + '">';
	html+='							</ul></div></div>';
	$('#timelinecontent').append(html);
}

//把新闻添加到对应的时间线之下
function insertNews(news){
	var newsDate = news.date;
	var htmlc = '<li class="article">';
	htmlc += '<div class="article-content">';
	htmlc += '<h2><a href="/Gossip-server/newsContent?newsId=' + news.id + '" title="">' + news.title + '</a></h2>';
	htmlc += '<p><a href="/Gossip-server/newsContent?newsId='+news.id+'">' + news.body.substring(0,300)+" ... " + '</a></p></div>';//datb.desc -> datb.body  by chenjie
	htmlc += '<span class="s">来源:' + news.url + '</span>';
	htmlc += '<span class="s">作者:' + news.author + '</span>';
	htmlc += '</li>';
	$('#' + newsDate).append(htmlc);
}

function pagination(pageNo, newsIdList, result){
	$('#page ul').empty();
	var eventNews = result.news;
	var totalNews = eventNews.length;
	var totalPages = Math.ceil(totalNews/10);
	var pageBegin = (pageNo-5)>0?pageNo-5:1;
	var pageEnd = (pageNo+5)>totalPages?totalPages:pageNo+5;
	//alert("pageNo: " + pageNo + " totalNews: " + totalNews +"  totalPages: " + totalPages + " pageBegin: " +pageBegin +" pageEnd: " +pageEnd);
	var html = "";
	if(pageNo==1){
        html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
        html += '<li id="last_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
    }
    else{
    	html = '<li><a href="#" onclick=toPage(' + 1 +',"' + newsIdList +'")>首页</a></li>';
        html += '<li id="last_page"><a href="#" onclick=toPage('+(pageNo-1) +',"' + newsIdList +'")>'+'«</a></li>';
    }
    
    for(var i = pageBegin; i <= pageEnd; i++) {
    	if(i==pageNo)
    		html += '<li id="page'+i+'" class="disabled"><a href="javascript:void(0)">' + i + '</a></li>';
    	else
    		html += '<li id="page'+i+'"><a href="#" onclick=toPage(' + i +',"' + newsIdList + '")>' + i + '</a></li>';
    	
    }
    if(pageNo==totalPages){
    	html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">'+'»</a></li>';
        html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">'+'末页</a></li>';
    }
    else{
    	html += '<li id="next_page" class=""><a href="#" onclick=toPage('+(pageNo + 1) +',"' + newsIdList +'")>'+'»</a></li>';
        html += '<li id="final_page" class=""><a href="#" onclick=toPage('+totalPages +',"' + newsIdList +'")>'+'末页</a></li>';
    }
    
    $('#page ul').append(html);
}

function toPage(pageNo, newsIdList){
	//alert(newsIdList);
	$('#timelinecontent').empty();
	$.ajax({
		type: "GET",
		contentType: "application/json; charset=utf-8",
		url:"/Gossip-server/news/getEventNews?newsIdList="+newsIdList,
		dataType: "json",
		anysc: true,
		success: function(result) {
			//alert("1212: " + newsList.length);
			newsJson = result;
			displayEventNews(pageNo, newsIdList, result);
			}//success
	});//ajax
}//toPage

//时间转换
function timeConvert(time){
	var timeString="";
	if(time==null||time=="")
		return time;
	if(time.length>8){
		timeString=time.substring(0,4)+"-"+time.substring(4,6)+"-"+time.substring(6,8);
	}
	return timeString;
}



//页码点击跳转



