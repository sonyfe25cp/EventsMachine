$(document).ready(function() {
	 
    var currentPageNum=0;
    var data = {
    classCode: "0001"
     }; // 这里要直接使用JOSN对象    
    $.ajax({
    type: "POST",
    contentType: "application/json; charset=utf-8",
    //url: "json_example/events.json",
    url:"/Gossip-server/events?pageNo=1",
    dataType: "json",
    anysc: false,
    data: data,
    success: function(result) {
        var numRows = result.total;
        var numPerPage = 10;
        var numPage = Math.ceil(numRows / numPerPage);
        //$('#page ul').empty();
        
         var html = '                    <li><a href="javascript:void(0)">首页</a></li>';
         html += '                    <li id="last_page"><a href="javascript:void(0)">'+'«</a></li>';
        
        for(var i = result.pageBegin-1; i <= result.pageEnd; i++) {
        	if(i==0)
        		html += '                   <li id="page'+(i+1)+'"><a href="javascript:void(0)" >' + (i + 1) + '</a></li>';
        	else
        		html += '                   <li id="page'+(i+1)+'"><a href="#" onclick="toPage('+(i+1)+')">' + (i + 1) + '</a></li>';
          
        }
        html += '                    <li id="next_page" class=""><a href="#" onclick=toPage(2)>»</a></li>';
        html += '                    <li id="final_page" class=""><a href="#" onclick="toPage('+numPage+')">'+'末页</a></li>';
        //var da=1;
        $('#page ul').append(html);
        var $pager = $('<div id="page" class="pagination pagination-centered"></div>');
        ppp(1, numPerPage, result);
        $('#page li').eq(0).addClass('disabled');
        $('#page li').eq(1).addClass('disabled');
        $('#page li').eq(2).addClass('disabled');

       
    }
  });
    

});//document

function toPage(pageNo){
	display(pageNo);
}
function toEvent(eventId){
	var imageStatus="";
	//alert(eventId);
	window.location.href="/Gossip-server/eventManage?eventId="+eventId+"&status="+"0";

}

var ppp = function(page, numPerPage, result) {
	 //alert(result.total);
	 var tata = result.events;
	 //alert(tata.length);
    var t = ((page - 1) * numPerPage);
    var r = page * numPerPage;
    $('#primary .list').empty();
    for(;(r < result.total) ? t < r : t < result.total; t++) { // alert(tata[t].title);
   	   var eventId=t%10;
   	   var tt = tata[eventId];
   	   //alert(tt.id+"---"+tt.desc);
   	   //alert(splitKeywords(tt.keywords));
	       var eventDom = $('<li id="' + tt.id + '" class="article"><div class="article-content"><h2></h2> <br></div><button id="eventModifyButton" class="pull-right" onclick="toEvent(this.value)" value="'+tt.id+'" onclick="toEvent(this.value)">事件修改</button>'+'<div class="info"></div></li>');
	       eventDom.find("h2").html('<span>' + tt.title + '</span>');
	       $('<p>' + tt.desc + '</p>').insertAfter(eventDom.find(".article-content br"));
	       var info = "&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
	       var keyword=splitKeywords(tt.keywords);
	       keyword=keyword.split(",");
	      for(i=0;i<keyword.length;i++){
	    	  info += "&nbsp;"+"&nbsp;"+'<a class="label">' + keyword[i] + '</a>';  
	      }  
	       info = info + '<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;' + dateConvert(tt.started_at) + '</span>';
	       eventDom.find(".info").html(info);
	       eventDom.appendTo("#primary .list");
    
    }

}

display=function(currentPage){
	  $('#page ul li').removeClass('disabled');

	  currentPageNum=currentPage;
	 
	  $.ajax({
	              type: "GET",
	              contentType: "application/json; charset=utf-8",
	              url:"/Gossip-server/events?pageNo="+currentPageNum,
	              //url: "json_example/events.json",
	              dataType: "json",
	              anysc: false,
	              //data: data,
	              success: function(result) {
	                var tata = result.events;
	                var numRows = result.total;
	                var numPerPage = 10;
	                var numPage = Math.ceil(numRows / numPerPage);
	                var page = currentPage;
	                var end=result.pageEnd;
	                var begin=result.pageBegin;
	                ppp(page, numPerPage, result);
	                $('#page ul').empty();
	                var html;
	                if(page==1){
	                    html = '                     <li class="disabled"><a href="javascript:void(0)">首页</a></li>';
	 	                html += '                    <li id="last_page" class="disabled"><a href="javascript:void(0)">'+'«</a></li>';
	                }
	                else{
	                	html = '                     <li><a href="/Gossip-server/">首页</a></li>';
		                html += '                    <li id="last_page"><a href="#" onclick="toPage('+(page-1)+')">'+'«</a></li>';
	                }
	                
	                for(var i = result.pageBegin-1; i <= result.pageEnd; i++) {
	                	if(i==page-1)
	                		html += '   <li id="page'+(i+1)+'" class="disabled"><a href="javascript:void(0)">' + (i + 1) + '</a></li>';
	                	else
	                		html += '   <li id="page'+(i+1)+'"><a href="#" onclick="toPage('+(i+1)+')">' + (i + 1) + '</a></li>';
	                	
	                }
	                if(page==numPage){
	                	html += '                    <li id="next_page" class="disabled"><a href="javascript:void(0)">'+'»</a></li>';
		                html += '                    <li id="final_page" class="disabled"><a href="javascript:void(0)">'+'末页</a></li>';
	                }
	                else{
	                	html += '                    <li id="next_page" class=""><a href="#" onclick="toPage('+(page+1)+')">'+'»</a></li>';
		                html += '                    <li id="final_page" class=""><a href="#" onclick="toPage('+numPage+')">'+'末页</a></li>';
	                }
	                
	                //var da=1;
	                $('#page ul').append(html);
	                
//	                if(numPage<=11||page<=6){
//	                	 $('#page li').eq(page+1).addClass('disabled');
//	                }
//	                else if(page+4>=numPage){
//	                	$('#page li').eq(page-begin+2).addClass('disabled');
//	                }
//	                else{
//	                	$('#page li').eq(7).addClass('disabled');
//	                }
//
//	                if(page==1){
//	                  $('#page li').eq(0).addClass('disabled');
//	                  
//	                  $('#page li').eq(1).addClass('disabled');
//	                }
//	                 if(page==numPage){
//	                  $('#next_page').addClass('disabled');
//	                  $('#final_page').addClass('disabled');
//	                } 


	              }

	            });
	}//display


//返回的keyWords是形如{奶粉:2.0;机构:2.0;已向:1.0;检出:1.0;果无:1.0;}的字符串，因此需要转换
function splitKeywords1(keyWords){
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
