//1

var current;

$(document).ready(function() {
    current = 0;
    toPage(current);
});//document
//跳转页面
function toPage(pageNo){
	if(pageNo < 0){
		return;
	}
	$('#primary .list').empty();
	current = pageNo;
	$.ajax({
	    type: "GET",
	    contentType: "application/json; charset=utf-8",
	    url:"/events?pageNo=" + pageNo,
	    dataType: "json",
	    anysc: false,
	    success: function(result) {
	    	display(result);
	    }
	  });
}//toPage

function display(result){
	$('#primary .list').empty();
	var html = "";
	$.each(result, function(i, event){
		var event_html ='<li id='+event["id"]+' class="article">' +
							'<div class="article-content">' +
								'<h2><a href="/event?eventId='+event["id"]+'">'+event["title"]+'</a></h2>'+
								'<br>'+
								'<p>'+
									'<a href="/event?eventId='+event["id"]+'">'+event['desc']+'</a>'+
								'</p>' +
							'</div>'+
							'<div class="info">'+
								'<a class="label">北京</a>'+
								'<span class="pull-right">'+dateConvert(event["createAt"])+'</span>'+
							'</div>'+
						'</li>';
		html += event_html;
	});
	$('#primary').html(html);
	
	//页码和翻页功能
//	pagination(result);
}//display

//页码和翻页功能实现
//function pagination(result){
//	$('#page ul').empty();
//	var pageNo = result.pageNo;
//	var totalPage = result.totalPage;
//	var beginNo = result.pageBegin;
//	var endNo = result.pageEnd;
//	var html = "";
//	//首页和上一页的设置
//	if(pageNo == 1){
//		html = '<li class="disabled"><a href="javascript:void(0)">首页</a></li>';
//		html += '<li id="pre_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
//	}
//	else{
//		html = '<li id="first_page"><a href="#" onclick="toPage(1)">首页</a></li>';
//        html += '<li id="pre_page"><a href="#" onclick="toPage(' + (pageNo - 1) +')">«</a></li>';
//	}
//	//中间页的设置
//	for(var i = beginNo; i<= endNo; i++){
//		if(i == pageNo)
//			html += '<li id="page' + i + '" class="disabled"><a href="javascript:void(0)">' + i + '</a></li>';
//		else 
//			html += '<li id="page' + i + '"><a href="#" onclick="toPage(' + i + ')">' + i + '</a></li>';
//	}
//	//末页和下一页的设置
//	if(pageNo == totalPage){
//		html += '<li id="next_page" class="disabled"><a href="javascript:void(0)">' + '»</a></li>';
//        html += '<li id="final_page" class="disabled"><a href="javascript:void(0)">' + '末页</a></li>';
//	}
//	else{
//		html += '<li id="next_page" class=""><a href="#" onclick="toPage(' + (pageNo+1) + ')">»</a></li>';
//        html += '<li id="final_page" class=""><a href="#" onclick="toPage(' + totalPage + ')">末页</a></li>';
//	}
//	$('#page ul').append(html);
//}//pagination

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
