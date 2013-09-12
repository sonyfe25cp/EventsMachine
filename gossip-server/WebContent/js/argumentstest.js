/*events.json试用
$(function(){
	//abc({	"total":55,	"pageNo":5,	"limit":15,	"events" : [{				"id" : "e-1",				"title" : "解放军四总部领导调整：房峰辉任总参谋长",				"desc" : "解放军四总部领导调整：房峰辉任总参谋长解放军四总部领导调整：房峰辉任总参谋长解放军四总部领导调整：房峰辉任总参谋长",				"img":"/images/e-1.jpg",				"started_at" : "2012-10-1",				"started_location" : "北京",				"keywords" : ["解放军", "解放军1"],				"news" : ["n-1", "n-2"]			}, {				"id" : "e-2",				"title" : "十年·城边之一:打工者的早餐",				"desc" : "十年·城边之一:打工者的早餐十年·城边之一:打工者的早餐十年·城边之一:打工者的早餐",				"img":"/images/e-1.jpg",				"started_at" : "南京",				"started_location" : "",				"keywords" : ["十年·城边之一", "早餐"],				"news" : ["n-3", "n-4"]			}]});
	abc(http://10.1.1.94:8080/jsonserver/json)});
function abc(){
	
	var data=arguments[0].events;
	for (var i = 0; i<data.length; i++) {			
			$("#primary .list li").find("h2").get(0).innerHtml=getEvent(data[i],"title");
			var eventDom=$('<li class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2>	<br></div><div class="info"><span class="pull-right"></span></div>');
			eventDom.find("h2").html('<a href="./event.html" title="'+getEvent(data[i],"title")+'">'+getEvent(data[i],"title")+'</a>');
			$('<p>'+getEvent(data[i],"desc")+'</p>').insertAfter(eventDom.find(".article-content br"));	
			var info="&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
			$.each(getEvent(data[i], "keywords"),function(index,ele){info+='<a class="label">'+ele+'</a>';});
			info = info+'<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;'+getEvent(data[i],"started_at")+'</span>';	
			eventDom.find(".info").html(info)
			eventDom.appendTo(".list");	
		};
	}
	function getEvent(obj,attr){
	return obj[attr];
}*/
/*$.ajax({      
   type: "get",       
   url: "json_example/events.json",
   success:function(){
   	
   	alert(arguments.length);
   	}
   
});*/
/*
function getd(){
			alert("hello1");
			$.getJSON("http://10.1.1.94:8080/jsonserver/json",function(){					
					//$("#primary .list li").find("h2").get(0).innerHtml=getEventTitle(data,0);
					//parse(data);
					
					alert(2);
				},"json").success(function() { alert("second success"); })
		.error(function() { alert("error"); })
		.complete(function() { alert("complete"); });
			
		}
		
$(function(){
	var script = document.createElement('script');
	//script.src='/events?pageNo={1}&limit={15}';
script.src = 'http://10.1.1.94:8080/jsonserver/json?callback=getd';
	//script.src='/json_example/events.json?callback=getd'
	script.type = 'text/javascript';
	$("head").get(0).appendChild(script);
	// $.getJSON("http://10.1.1.94:8080/jsonserver/json?callback=parse",function(data){					
	// 		//$("#primary .list li").find("h2").get(0).innerHtml=getEventTitle(data,0);
	// 		//parse(data);
	// 	},"json").success(function() { alert("second success"); }).error(function() { alert("error"); }).complete(function() { alert("complete"); });
	
});*/
var data = { classCode: "0001"}; // 这里要直接使用JOSN对象 
$.ajax({ 
type: "GET", 
contentType: "application/json; charset=utf-8", 
url: "json_example/events.json", 
dataType: "json", 
anysc: false, 
data: data, 
success: function(result){var data=result.events;
	for (var i = 0; i<data.length; i++) {			
			//$("#primary .list li").find("h2").get(0).innerHtml=getEvent(data[i],"title");
			var id=getEvent(data[i],"id");
			var idt=id.substring(2);
			var eventDom=$('<tr><li id="'+id+'" num="'+idt+'" class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2>	<br></div><div class="info"><span class="pull-right"></span></div></tr>');
			eventDom.find("h2").html('<a href="./event.html" title="'+getEvent(data[i],"title")+'">'+getEvent(data[i],"title")+'</a>');
			$('<p>'+getEvent(data[i],"desc")+'</p>').insertAfter(eventDom.find(".article-content br"));	
			var info="&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
			$.each(getEvent(data[i], "keywords"),function(index,ele){info+='<a class="label">'+ele+'</a>';});
			info = info+'<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;'+getEvent(data[i],"started_at")+'</span>';	
			eventDom.find(".info").html(info)
			eventDom.appendTo("#primary .list");	
		};
		function getEvent(obj,attr){
	return obj[attr];
}
//分页显示调用
//pageshow(result);
dis(result);
}, 
error: function (XMLHttpRequest, textStatus, errorThrown) { 
alert(errorThrown + ':' + textStatus); // 错误处理 
} 
}); 
var dis=function(aa) {
            	 var numRows=aa.total+2;
            	 var currentPage=0;
            	 var numPerPage=10;
          	   
	      for(var i=0;i<aa.total+2;i++){
	      	if((i<currentPage*numPerPage)||(i>((currentPage+1)*numPerPage-1)))
             $('#primary .list li').eq(i).hide();
             
         }
     
         var numPages=Math.ceil(numRows/numPerPage);
         alert(numPages);
         
           var htt='';
            for(var j=2;j<(numPages+1);j++)
            {htt+='<li><a>'+ j +'</a></li>';
            	alert(j);
            	}  
            	htt+='<li><a>»</a></li>'
            	var eventDo='<div class="pagination pagination-centered"><ul><li class="disabled"><a href="#">«</a></li><li><a>1</a></li>'+ htt +'</ul></div>';    
               $('#container-inner').append(eventDo);
                $('#container-inner <li><a>1</a></li>').addClass("active");
               var $pager=$('#container-inner .pagination pagination-centered ul');
               for(var j=0;j<numPages;j++){
             $('#container-inner <li><a>'+j+'</a></li>').bind('click',{'newPage':j},function(event){
             	 currentPage=j;
             	 alert(3);
             	 for(var i=0;i<aa.total+2;i++){
	      	if((i<currentPage*numPerPage)||(i>((currentPage+1)*numPerPage-1)))
             $('#primary .list li').eq(i).hide();
             
         }
         $(this).addClass('active').siblings().removeClass('active');
            }
        
             	).appendTo($paper).addClass('clickable');
             }
           $('#container-inner <li><a>1</a></li>').addClass('active');
           // var $toggleBtn = $('div.showmore > a');
           /* $toggleBtn.click(function() {
                if ($category.is(":visible")) {
                    $category.hide();
                    $('.showmore a span').css("background", "yellow").text("显示全部");
                    $('ul li').removeClass("promoted");
                }
                else {
                    $category.show();
                    $category.show();
                    $('.showmore a span').css("background", "Green").text("显示部分");
                    $('ul li').filter(":contains('佳能')，：contains('尼康')，：contains('奥林巴斯')").addClass("promoted");
                }*/
                return false;
            }
/*
$(document).ready(function(){
function pageshow(){
	var total=arguments[0].total;
	var limit=10;
	var pagetotal=Math.ceil(total/numPerPage);
	/*$("#primary .artical").each(function(){
		var currentPage=0;
		var numPerPage=limit;
		var repaginate=function(){
			if($('#primary '))
			}
		})*/
	/*	
		var repaginate=function(){
		for(var i=0;i<total;i++){
			if((i<currentPage*numPerPage)||(i>((currentPage+1)*numPerPage-1)))
			{$('#primary li:eq[i]').hide();}
			else
				{$('#primary li:eq[i]').show();}
		}}
	}
});*/

var pageshow=function(tt){
	var numRows= tt.total;
	//var data=tt.events;
	//alert(numRows);
	var numPerPage=10;
//	$('.pagination pagination-centered').each(function(){
	var currentPage=0;
	//alert(numPerPage);
  var repaginate=function(currentPage){
  	var tt=numRows+2;
  	alert(tt);
		for(var i=0;i<tt;i++){
			if((i<currentPage*numPerPage)||(i>((currentPage+1)*numPerPage-1))){
			 $('#primary .list li').eq(i).hide();
				}
		//	alert((currentPage+1)*numPerPage-1);
			/*else
				{$('#primary .list li').eq(i).show();}
		*/}
		}
  	var numPages=Math.ceil(numRows/numPerPage);
  	//alert(numPages);
  	$('#container-inner .pagination pagination-centered ul').empty();
	var html='		                <ul><li class="disabled"><a href="#">«</a></li>';
  	html+='		                <li class="active"><a href="#">1</a></li>';
  	for(var j=2;j<numPages+1;j++){
  	 html+='		                <li><a href="#">'+j+'</a></li>';
    }
    html+='		                <li><a href="#">»</a></li></ul>';
     $('#container-inner .pagination pagination-centered').append(html);
     // alert(numPages);
     for(var page=0;page<numPages;page++){
  		$('.pagination pagination-centered li:nth-child(page+1)')
  	  	//.bind('click',{'newPage':page},function(event){
  	    .click(function(){
  	    	
  	     //currentPage=event.data['newPage'];
  			repaginate(page);
  		});
  			}
  			//.appendTo($(this)).addClass('clickable');
  		}




  /*var $pager=$('<li class="pager"></li>');
  	for(var page=0;page<numPages;page++){
  		$('<span class="page-number">'+(page+1)+'</span>')
  	  	.bind('click',{'newPage':page},function(event){
  	     currentPage=event.data['newPage'];
  			//currentPage=page;
  			repaginate();
  			$(this).addClass('active').siblings().removeClass('active');
  			})
  			.appendTo($pager).addClass('clickable');
  		}
  		$pager.find('span.page-number:first').addClass('active');
  		//$pager.appendTo('.pagination pagination-centered');
  		//$('div.copyright').append($paper)
  		$('.pagination pagination-centered').append($paper);
  		repaginate();
	*/

$(document).ready(function(){	
  $.get('json_example/hotwords', function(data) {
   /* var tt=data;
    var dd=new Array();
    var aa;
    var k=0;
    for(var j=0;j<tt.length;j++)
    {
    	if((tt[j]!="{")&&(tt[j]!='"'&&tt[j]!="}")&&tt[j]!=",")
    	{dd[k]+=tt[j];}
    	else
    		if(tt[j]==",")
    	{
       k++;    }
    	}
    	dd.toString();
    	for(var j=0;j<dd.length;j++)
    	alert(dd[j]);
    	*/
   	var tt=data;
    	var dd=new Array();
    	dd=tt.split('","');
    	var cc=dd[0].toString();
    	cc=cc.substring(2);
    	dd[0]=cc;
    	cc=dd[dd.length-1];
    	cc.toString();
    	cc=cc.substring(0,(cc.length-2));
    	dd[dd.length-1]=cc;
    //	for(var i=0;i<dd.length;i++)
    	//alert(dd[i]);	
    $('#sidebar').empty();
	    var html='<div class="sidemodule">';
	    html+='<h3 class="sub">热门关键词</h3>';
	
		 for(var i=0;i<dd.length;i++)
		  {
		  	html+='						<a href="#" class="label">'+dd[i]+'</a>                                                                                                                                                                                           ';
		  	}
		   html_='</div>';
			$('#sidebar').append(html);
		//	alert(3);
  });
});


$(document).ready(function(){	
  $.get('json_example/hotpeople', function(data) {
   	 var tt=data;
    	var dd=new Array();
    	dd=tt.split('","');
    	var cc=dd[0].toString();
    	cc=cc.substring(2);
    	dd[0]=cc;
    	cc=dd[dd.length-1];
    	cc.toString();
    	cc=cc.substring(0,(cc.length-2));
    	dd[dd.length-1]=cc;
    //	for(var i=0;i<dd.length;i++)
    	//alert(dd[i]);	
    $('#sidebar div:eq(1)').empty();
	    var html='<div class="sidemodule">';
	    html+='<h3 class="sub">热门人名</h3>';
	
		 for(var i=0;i<dd.length;i++)
		  {
		  	html+='						<a href="#" class="label">'+dd[i]+'</a>                                                                                                                                                                                           ';
		  	}
		   html_='</div>';
			$('#sidebar').append(html);
			//alert(3);
  });
});


$(document).ready(function(){	
  $.get('json_example/hotplaces', function(data) {
   	var tt=data;
    	var dd=new Array();
    	dd=tt.split('","');
    	var cc=dd[0].toString();
    	cc=cc.substring(2);
    	dd[0]=cc;
    	cc=dd[dd.length-1];
    	cc.toString();
    	cc=cc.substring(0,(cc.length-2));
    	dd[dd.length-1]=cc;
   // 	for(var i=0;i<dd.length;i++)
    //	alert(dd[i]);	
    $('#sidebar div:eq(2)').empty();
	    var html='<div class="sidemodule">';
	    html+='<h3 class="sub">热门地名</h3>';
	
		 for(var i=0;i<dd.length;i++)
		  {
		  	html+='						<a href="#" class="label">'+dd[i]+'</a>                                                                                                                                                                                           ';
		  	}
		   html_='</div>';
			$('#sidebar').append(html);
		//	alert(3);
  });
});

/*
function setPage(opt){ 
if(!opt.pageDivId || opt.allPageNum < opt.curpageNum || opt.allPageNum < opt.showPageNum){return false}; 
var allPageNum = opt.allPageNum; //总的页数 
var showPageNum = opt.showPageNum; //显示的页数 
var curpageNum = opt.curpageNum; // 当前的页数 
var pageDIvBox = document.getElementById(opt.pageDivId); 
//左边或右边显示页码的个数 
var lrNum = Math.floor(showPageNum/2); 
if(curpageNum>1){ 
var oA = document.createElement('a'); 
oA.href='#1'; 
oA.innerHTML = '首页' 
pageDIvBox.appendChild(oA); 
} 
if(curpageNum>1){ 
var oA = document.createElement('a'); 
oA.href='#'+(curpageNum-1); 
oA.innerHTML = '上一页' 
pageDIvBox.appendChild(oA); 
} 
if(curpageNum<showPageNum-2 || allPageNum == showPageNum){ 
for(var i=1;i<=showPageNum;i++){ 
var oA = document.createElement('a'); 
oA.href = '#'+i; 
if(curpageNum==i){ 
oA.innerHTML = i; 
}else{ 
oA.innerHTML = "[" + i + "]"; 
} 
//pageDIvBox.appendChild(oA); 
} 
}else{ 
//倒数第一页的处理 
if(allPageNum-curpageNum<lrNum && curpageNum == allPageNum-1){ 
for(var i=1;i<=showPageNum;i++){ 
console.log((curpageNum - showPageNum + i)); 
var oA = document.createElement('a'); 
oA.href = '#'+ (curpageNum - (showPageNum-1) + i); 
if(curpageNum == (curpageNum - (showPageNum-1) + i)){ 
oA.innerHTML = (curpageNum - (showPageNum-1) + i) 
}else{ 
oA.innerHTML = '['+(curpageNum - (showPageNum-1) + i)+']' 
} 
//pageDIvBox.appendChild(oA); 
} 
} 
//最后一页的处理 
else if(allPageNum-curpageNum<lrNum && curpageNum == allPageNum){ 
for(var i=1;i<=showPageNum;i++){ 
console.log((curpageNum - showPageNum + i)); 
var oA = document.createElement('a'); 
oA.href = '#'+ (curpageNum - showPageNum + i); 
if(curpageNum == (curpageNum - showPageNum + i)){ 
oA.innerHTML = (curpageNum - showPageNum + i) 
}else{ 
oA.innerHTML = '['+(curpageNum-showPageNum + i)+']' 
} 
//pageDIvBox.appendChild(oA); 
} 
}else{ 
for(var i=1;i<=showPageNum;i++){ 
var oA = document.createElement('a'); 
oA.href = '#'+ (curpageNum - (showPageNum-lrNum) + i); 
if(curpageNum == (curpageNum - (showPageNum-lrNum) + i)){ 
oA.innerHTML = (curpageNum - (showPageNum-lrNum) + i) 
}else{ 
oA.innerHTML = '['+(curpageNum - (showPageNum-lrNum) + i)+']' 
} 
//pageDIvBox.appendChild(oA); 
} 
} 
} 
if(curpageNum<allPageNum){ 
for(var i=1;i<=2;i++){ 
if(i==1){ 
var oA = document.createElement('a'); 
oA.href='#'+(parseInt(curpageNum)+1); 
oA.innerHTML = '下一页' 
}else{ 
var oA = document.createElement('a'); 
oA.href='#'+allPageNum; 
oA.innerHTML = '尾页' 
} 
//pageDIvBox.appendChild(oA); 
} 
} 
var oA = document.getElementsByTagName('a'); 
//给页码添加点击事件 
for(var i=0;i<oA.length;i++){ 
oA[i].onclick = function(){ 
//当前点的页码 
var sHref = this.getAttribute('href').substring(1); 
//清空页数显示 
//pageDIvBox.innerHTML = ''; 
setPage({ 
pageDivId:'page', 
showPageNum:5, //显示的个数 
allPageNum:10, //总页数 
curpageNum:sHref //当前页数 
}) 
} 
} 
} 
window.onload = function(){ 
setPage({ 
pageDivId:'page', 
showPageNum:5, //显示的个数 
allPageNum:10, //总页数 
curpageNum:1 //当前页数 
}) 
} */



//正确：

	$(document).ready(function(){
    var numRows=14;
    var numPerPage=10;
    var numPage=Math.ceil(numRows/numPerPage);
	//$('#page ul').empty();
	var html='		                <li class="disabled"><a href="#">«</a></li>';
	for(var i=0;i<numPage;i++)
	{
		html+='		                <li><a>'+(i+1)+'</a></li>'
		}
		var da=1;
		$('#page ul').append(html);
		var $pager=$('<div id="page" class="pagination pagination-centered"></div>');
	  for(var page=1;page<numPage+1;page++){
			//alert(page);
			var t=page;
			alert(page);
		  $('#page li').eq(page).bind('click',{'newPage':page},function(event){
	   	var currentPage=event.data['newPage'];
	   	alert(currentPage);
	   var data = { classCode: "0001"}; // 这里要直接使用JOSN对象 
      $.ajax({ 
       type: "GET", 
       contentType: "application/json; charset=utf-8", 
       url: "json_example/events.json", 
       dataType: "json", 
       anysc: false, 
       data: data, 
       success: function(result){
       	     var tata=result.events;
             var numRows=result.total;
             var numPerPage=10;
             var numPage=Math.ceil(numRows/numPerPage);
             alert("numPage");
             alert(numPage);
             var page=currentPage;
           // for(;page<numPage+1;page++)
         // {
             	var t=((page-1)*numPerPage);
             	var r=page*numPerPage;
             	$('#primary .list').empty();
             	for(;(r<result.total)?t<r:t<result.total;t++)
       	      {// alert(tata[t].title);
       	      	var tt=tata[t];
       	      //	alert(tt.id);
       	      	//alert(tt.title);
       	      	//alert(tt[id]);
       	      	 var eventDom=$('<tr><li id="'+tt.id+'" class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2>	<br></div><div class="info"><span class="pull-right"></span></div></tr>');
			           eventDom.find("h2").html('<a href="./event.html" title="'+tt.title+'">'+tt.title+'</a>');
			           $('<p>'+tt.desc+'</p>').insertAfter(eventDom.find(".article-content br"));	
			           var info="&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
		       	     $.each(tt.keywords,function(index,ele){info+='<a class="label">'+ele+'</a>';});
		      	     info = info+'<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;'+tt.started_at+'</span>';	
			           eventDom.find(".info").html(info)
			           eventDom.appendTo("#primary .list");	}
       	        // alert(111);
       	    // 	}
	         /* for (var i = (page-1)*numPerPage; i<(page*numPerPage-1)&&i<result.total; i++) {		
	        	  alert(i);
			        //$("#primary .list li").find("h2").get(0).innerHtml=getEvent(data[i],"title");
			        //var idt=id.substring(2);
			        var eventDom=$('<tr><li id="'+tata[i].id+'" class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2>	<br></div><div class="info"><span class="pull-right"></span></div></tr>');
			        eventDom.find("h2").html('<a href="./event.html" title="'+tt.title+'">'+tt.title+'</a>');
			        $('<p>'+tt[desc]+'</p>').insertAfter(eventDom.find(".article-content br"));	
			        var info="&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
		       	  $.each(tt.keywords,function(index,ele){info+='<a class="label">'+ele+'</a>';});
		      	   info = info+'<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;'+tt.started_at+'</span>';	
			         eventDom.find(".info").html(info)
			         eventDom.appendTo("#primary .list");	
		      };*/
	        	// function getEvent(obj,attr){return obj[attr];}
            }
           /*, 
           error: function (XMLHttpRequest, textStatus, errorThrown) { 
           alert(errorThrown + ':' + textStatus); // 错误处理 
              } */
         }); 
		   	});
	}
	});