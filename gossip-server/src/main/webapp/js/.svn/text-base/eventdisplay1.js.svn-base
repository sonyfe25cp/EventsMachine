$(document).ready(function() {
	//获取了id
	var currentPageNum=0;
	thisURL = document.URL;
	var eveid = thisURL.split("=");
	var eventid=$("#eventid").val();
	url="/gossip-server/events/"+eventid;
	
	$.ajax({
		type: "POST",
		contentType: "application/json; charset=utf-8",
		url:url,
		dataType: "json",
		anysc: true,
		//data: datt,
		success: function(data) {
			$('#container-inner .event-title').empty();
			var html;
			if(data['img']==""||data['img']==null)
				html = '							<div class="span4"><img src="' + 'eventImg/none.jpg' + '" alt=""></div>';
			else
				html = '							<div class="span4"><img src="' + data['img'] + '" alt=""></div>';
			
			html += '<div class="span8 event-brief">';
			html += '<h2>' + data['title'] + '</h2>';
			html += '<p>' + data['desc'] + '</p>';
			html += '</div>';
			$('#container-inner .event-title').append(html);
			//显示事件新闻
			var newsids = data.news;
			$.ajax({
				type: "POST",
				contentType: "application/json; charset=utf-8",
				url:"/gossip-server/news/event-news?newsids="+newsids,
				dataType: "json",
				anysc: true,
				//data: datt,
				success: function(result) {
					array=result.news;
					var date = "";
					var numRows = array.length;
					var numPerPage = 10;
					var numPage = Math.ceil(numRows / numPerPage);
					
					var html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
					html+= '<li class="disabled"><a href="javascript:void(0)">«</a></li>';
					for(var ib = 0; ib < numPage; ib++) {
						if(ib==0)
							html += '<li><a href="javascript:void(0)" >' + (ib + 1) + '</a></li>';
						else
							html += '<li><a href="#" onclick="toPage('+(ib+1)+','+array+')">' + (ib + 1) + '</a></li>';
						
					}
					 html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">»</a></li>';
				     html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">末页</a></li>';
					$('#page ul').append(html);
					date =ppp(1, numPerPage, array, date);
					$('#page ul li').eq(2).addClass('disabled');

		}//success1
	});//ajax1
		}//success2
	});//ajax2
	
//	$.getJSON(url, function(data) {
//		$('#container-inner .event-title').empty();
//		var html;
//		if(data['img']==""||data['img']==null)
//			html = '							<div class="span4"><img src="' + 'eventImg/none.jpg' + '" alt=""></div>';
//		else
//			html = '							<div class="span4"><img src="' + data['img'] + '" alt=""></div>';
//		
//		html += '<div class="span8 event-brief">';
//		html += '<h2>' + data['title'] + '</h2>';
//		html += '<p>' + data['desc'] + '</p>';
//		html += '</div>';
//		$('#container-inner .event-title').append(html);
//		//显示事件新闻
//		var newsids = data.news;
//		$.ajax({
//			type: "POST",
//			contentType: "application/json; charset=utf-8",
//			url:"/gossip-server/news/event-news?newsids="+newsids,
//			dataType: "json",
//			anysc: true,
//			//data: datt,
//			success: function(result) {
//				array=result.news;
//				var date = "";
//				var numRows = array.length;
//				var numPerPage = 10;
//				var numPage = Math.ceil(numRows / numPerPage);
//				
//				var html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
//				html+= '<li class="disabled"><a href="javascript:void(0)">«</a></li>';
//				for(var ib = 0; ib < numPage; ib++) {
//					if(ib==0)
//						html += '<li><a href="javascript:void(0)" >' + (ib + 1) + '</a></li>';
//					else
//						html += '<li><a href="#" onclick="toPage('+(ib+1)+','+array+')">' + (ib + 1) + '</a></li>';
//					
//				}
//				 html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">»</a></li>';
//			     html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">末页</a></li>';
//				$('#page ul').append(html);
//				date =ppp(1, numPerPage, array, date);
//				$('#page ul li').eq(2).addClass('disabled');
////				for(var pagea = 1; pagea < numPage + 1; pagea++) {
////					$('#page li').eq(pagea+1).bind('click', {
////						'newPage': pagea
////					}, function(event) {
////						var currentPageNum = event.data['newPage'];
////						var pageb = currentPageNum;
////						date =ppp(pageb, numPerPage, array, date);
////						$('#page ul li').eq(pageb+1).addClass('disabled');
////					});//bind
////				}//for
//
//			}
//		});//ajax
//	});//getJSON
	
	//相关事件的显示
	var url1="/gossip-server/related-events?id="+eveid[1]+"&pageNo=1&limit=15";
	$.ajax({
		type: "GET",
		contentType: "application/json; charset=utf-8",
		//url: "json_example/revents.json",
		url:url1,
		dataType: "json",
		anysc: false,
		//data: data,
		success: function(result) {
			var datad = result.events;
			$('#relate').empty();
			var html = "";
			for(var ir = 0; ir < datad.length; ir++) {
				html += '												<a href="/gossip-server/events?id=' + datad[ir].id + '" class="span3">' + datad[ir].title + ' </a>';
			}

			$('#relate').append(html);

		}//sucess
	});//ajax
	var timelineHeight = $("#timelinecontent").height();
	$("#timelinebar").height(timelineHeight);
	
});//document

//页码点击跳转
function toPage(pageNo,newsArray){
	var allNewsNum = array.length;
	var numPerPage = 10;
	var totalPage = Math.ceil(allNewsNum / numPerPage);
	var pageBegin=pageNo-5>0?pageNo-5:1;
	var pageEnd;
	var date="";//默认每一页从新的时间开始排列
	if(totalPage<=11)
		pageEnd=pageNo;
	else if(pageNo<=6)
		pageEnd=10;
	else{
		pageEnd=pageNo+4;
		if(pageEnd>=totalPage){
			pageBegin=totalPage-11;//这里默认每页显示10个news，totalPage-limit-1
			pageEnd=totalPage-1;
		}
	}
	ppp(pageNo,numPerPage,newsArray,date);
	$('#page ul').empty();
	var html;
    if(pageNo==1){
        html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
         html += '<li id="last_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
    }
    else{
    	html = '<li><a href="/gossip-server/">首页</a></li>';
        html += '<li id="last_page"><a href="#" onclick="toPage('+(pageNo-1)+','+newsArray+')">'+'«</a></li>';
    }
    
    for(var i = pageBegin-1; i <= pageEnd; i++) {
    	if(i==pageNo-1)
    		html += '<li id="page'+(i+1)+'" class="disabled"><a href="javascript:void(0)">' + (i + 1) + '</a></li>';
    	else
    		html += '<li id="page'+(i+1)+'"><a href="#" onclick="toPage('+(i+1)+','+newsArray+')">' + (i + 1) + '</a></li>';
    	
    }
    if(pageNo==totalPage){
    	html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">'+'»</a></li>';
        html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">'+'末页</a></li>';
    }
    else{
    	html += '<li id="next_page" class=""><a href="#" onclick="toPage('+(page+1)+','+newsArray+')">'+'»</a></li>';
        html += '<li id="final_page" class=""><a href="#" onclick="toPage('+totalPage+','+newsArray+')">'+'末页</a></li>';
    }
    
    $('#page ul').append(html);
}//toPage

var evedate = function(date) {
	var html = '						<div class="news" >';
	html += '							<span class="news-time">' + date + '</span>';
	html += '							<div class="news-list">';
	html += '								<i>';
	html += '								</i>';
	html += '								<ul  id="' + date + '">';
	//html+='							</ul></div></div>';
	$('#timelinecontent').append(html);

};

var ppp = function(page, numPerPage, array, dateb) {
	var tata = array;
	var t = ((page - 1) * numPerPage);
	var r = page * numPerPage;
	$('#timelinecontent').empty();
	//	evedate(dateb);
	for(var currenta = t;(array.length < r) ? (currenta < array.length) : (currenta < r); currenta++) {
		var time1=array[currenta].publish_at;
		time2=dateb
		if(time1 == time2) {
			newdis(dateb, array[currenta]);
		} else {
			var htmlb = '</ul></div></div>';
			$('#timelinecontent .news').append(htmlb);
			dateb = array[currenta].publish_at;
			evedate(dateb);
			newdis(dateb, array[currenta]);
		}
	}

	$('#timelinecontent').append(htmlb);
	return dateb;
};

var newdis = function(dateb, datb) {
	var htmlc = '<li class="article">';
	htmlc += '<div class="article-content">';
	htmlc += '<h2><a href="/gossip-server/newsContent?newsId=' + datb.id + '" title="">' + datb.title + '</a></h2>';
	htmlc += '<p><a href="/gossip-server/newsContent?newsId='+datb.id+'">' + datb.desc + '</a></p></div>';
	htmlc += '<span class="s">来源:' + datb.source + '</span>';
	htmlc += '<span class="s">作者:' + datb.author + '</span>';
	htmlc += '</li>';
	$('#' + dateb).append(htmlc);
};



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

