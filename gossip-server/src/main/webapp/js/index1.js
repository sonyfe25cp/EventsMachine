 $(document).ready(function() {
	 $.ajax({
		    type: "GET",
		    contentType: "application/json; charset=utf-8",
		    //url: "json_example/events.json",
		    url:"/gossip-server/events?pageNo=1",
		    dataType: "json",
		    anysc: false,
		    //data: data,
		    success: function(result) {
		    	//alert(result.total+" "+result.events.length+" "+result.events[0].id+" "+result.events[0].desc);
		    	 var numRows = result.total;
		         var numPerPage = 10;
		         var numPage = Math.ceil(numRows / numPerPage);
		         var html = '                    <li class="disabled"><a href="#">«</a></li>';
		         for(var i = 0; i < numPage; i++) {
		           html += '                   <li><a>' + (i + 1) + '</a></li>';
		         }//for
		         $('#page ul').append(html);
		         var $pager = $('<div id="page" class="pagination pagination-centered"></div>');
		         ppp(1, numPerPage, result);
		         $('#page li').eq(1).addClass('active');
		         for(var page = 1; page < numPage + 1; page++) {
		        	 $('#page li').eq(page).bind('click', {
		                 'newPage': page
		               }, function(event) {
		            	   var currentPage = event.data['newPage'];
		            	   //alert(currentPage);
		            	   $.ajax({
		            		   type: "GET",
		                       contentType: "application/json; charset=utf-8",
		                       url:"/gossip-server/events?pageNo="+currentPage,
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
		                           //alert(page);
		                           ppp(page, numPerPage, result);
		                           $('#page ul li').removeClass('active');
		                           $('#page li').eq(page).addClass('active');
		                       }//sucess
		            	   });//ajax
		               }//function(event)
		               );//bind
		         }//for
		    }//success
	 });//ajax
	 
	 /*下面是获取hotPeople、hotPlaces、hotKeywords*/
	 $('#sidebar').empty();
	 $.get('/gossip-server/hot-words', function(data) {
	    	var hotKeywords=data.hotKeywords;
	    	if(hotKeywords==null||hotKeywords.length<1)
	    		return;
		      var html = '<div class="sidemodule">';
		      html += '<h3 class="sub">热门关键词</h3>';

		      for(var i = 0; i < hotKeywords.length; i++) {
		        html += '           <a href="#" class="label">' + hotKeywords[i] + '</a>                                                                                                                                                                                           ';
		      }
		      html+= '</div>';
		      $('#sidebar').append(html);
	    });//hotKeywords
	 
	 
	 $.get('/gossip-server/hot-people', function(data) {
	    	var hotPeople=data.hotPeople;
	    	if(hotPeople==null||hotPeople.length<1)
	    		return;
	    	 //$('#sidebar div:eq(1)').empty();
		      var html = '<div class="sidemodule">';
		      html += '<h3 class="sub">热门人名</h3>';

		      for(var i = 0; i < hotPeople.length; i++) {
		        html += '           <a href="#" class="label label-inverse">' + hotPeople[i] + '</a>                                                                                                                                                                                           ';
		      }
		      html+= '</div>';
		      $('#sidebar').append(html);
	    });//hotPeople
	 
	 
	 $.get('/gossip-server/hot-places', function(data) {
	    	var hotPlaces=data.hotPlaces;
	    	if(hotPlaces==null||hotPlaces.length<1)
	    		return;
	    	 //$('#sidebar div:eq(2)').empty();
		      var html = '<div class="sidemodule">';
		      html += '<h3 class="sub">热门地名</h3>';

		      for(var i = 0; i < hotPlaces.length; i++) {
		        html += '           <a href="#" class="label label-info">' + hotPlaces[i] + '</a>                                                                                                                                                                                           ';
		      }
		      html+= '</div>';
		      $('#sidebar').append(html);
	    });//hotPlaces
	 
	 
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
		       var eventDom = $('<li id="' + tt.id + '" class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2> <br></div><div class="info"><span class="pull-right"></span></div></li>');
		       eventDom.find("h2").html('<a href="/gossip-server/event?eventId=' + tt.id  + '">' + tt.title + '</a>');
		       $('<p><a href="/gossip-server/event?eventId='+tt.id+'">' + tt.desc + '</a></p>').insertAfter(eventDom.find(".article-content br"));
		       var info = "&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
		       var keyword=splitKeywords(tt.keywords);
		       keyword=keyword.split(",");
//		       $.each(splitKeywords(tt.keywords), function(index, ele) {
//		         info += '<a class="label label-info">' + ele + '</a>';
//		       });
		      for(i=0;i<keyword.length;i++){
		    	  info += "&nbsp;"+"&nbsp;"+'<a class="label">' + keyword[i] + '</a>';  
		      }
		       
		       info = info + '<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;' + dateConvert(tt.started_at) + '</span>';
		       eventDom.find(".info").html(info);
		       eventDom.appendTo("#primary .list");
	     
	     }
     
   }
	 
	 		   
 });//document
 
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
 
