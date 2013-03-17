  /*
          分页显示思路：设置pageNo为当前页页码，即点击的页码。然后在events中查找limit*(pageNo-1)到limit*pageNo-1的内容显示出来（判断是否超出total上限，超出时结束）
          条件：算出总页数，pageNo<=总页数pageNum的情况下循环    当pageNo=pageNum时候时候，从limit*(pageNo-1)到total-1显示
         并设置当前页面的页码为活动状态。
          */

  /*
          点击某一条事件后的显示：做一函数，直接在这个页面获取点击的时间的id，然后作为参数传给函数，调用然后跳转
          
          */
  var data = {
    classCode: "0001"
  }; // 这里要直接使用JOSN对象    
  $.ajax({
    type: "GET",
    contentType: "application/json; charset=utf-8",
    //url: "json_example/events.json",
    url:"/gossip-server/events?pageNo=1&limit=15",
    dataType: "json",
    anysc: false,
    data: data,
    success: function(result) {
      $(document).ready(function() {
        var numRows = result.total;
        var numPerPage = 10;
        var numPage = Math.ceil(numRows / numPerPage);
        //$('#page ul').empty();
        var html = '                    <li class="disabled"><a href="#">«</a></li>';
        for(var i = 0; i < numPage; i++) {
          html += '                   <li><a>' + (i + 1) + '</a></li>'
        }
        //var da=1;
        $('#page ul').append(html);
        var $pager = $('<div id="page" class="pagination pagination-centered"></div>');
        ppp(1, numPerPage, result);
        //$('#page ul').removeClass('active');
        $('#page li').eq(1).addClass('active');
        for(var page = 1; page < numPage + 1; page++) {
          //alert(page);
          //  var t=page;
          //alert(page);
          $('#page li').eq(page).bind('click', {
            'newPage': page
          }, function(event) {
            var currentPage = event.data['newPage'];

            //alert(currentPage);
            var data = {
              classCode: "0001"
            }; // 这里要直接使用JOSN对象 
            $.ajax({
              type: "GET",
              contentType: "application/json; charset=utf-8",
              url:"/gossip-server/events?pageNo=1&limit=15",
              //url: "json_example/events.json",
              dataType: "json",
              anysc: false,
              data: data,
              success: function(result) {
                var tata = result.events;
                var numRows = result.total;
                var numPerPage = 10;
                var numPage = Math.ceil(numRows / numPerPage);
                //alert("numPage");
                //alert(numPage);
                var page = currentPage;
                // for(;page<numPage+1;page++)
                // {
                ppp(page, numPerPage, result);
                $('#page ul li').removeClass('active');
                $('#page li').eq(page).addClass('active');
                // alert(111);
                //  }
                /* for (var i = (page-1)*numPerPage; i<(page*numPerPage-1)&&i<result.total; i++) {    
            alert(i);
            //$("#primary .list li").find("h2").get(0).innerHtml=getEvent(data[i],"title");
            //var idt=id.substring(2);
            var eventDom=$('<tr><li id="'+tata[i].id+'" class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2>  <br></div><div class="info"><span class="pull-right"></span></div></tr>');
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
    }
  });

  var ppp = function(page, numPerPage, result) {
      var tata = result.events;
      var t = ((page - 1) * numPerPage);
      var r = page * numPerPage;
      $('#primary .list').empty();
      for(;
      (r < result.total) ? t < r : t < result.total; t++) { // alert(tata[t].title);
        var tt = tata[t];
        //  alert(tt.id);
        //alert(tt.title);
        //alert(tt[id]);
        var eventDom = $('<li id="' + tt.id + '" class="article"><div class="article-content"><h2><a href="./event.html" title=""></a></h2> <br></div><div class="info"><span class="pull-right"></span></div>');
        eventDom.find("h2").html('<a href="./event.jsp?id=' + tt.id + '" title="' + tt.title + '">' + tt.title + '</a>');
        $('<p>' + tt.desc + '</p>').insertAfter(eventDom.find(".article-content br"));
        var info = "&#x7279;&#x5F81;&#x8BCD;&#xFF1A;";
        $.each(tt.keywords, function(index, ele) {
          info += '<a class="label">' + ele + '</a>';
        });
        info = info + '<span class="pull-right">&#x65F6;&#x95F4;&#xFF1A;' + tt.started_at + '</span>';
        eventDom.find(".info").html(info)
        eventDom.appendTo("#primary .list");
      }
    }



  $(document).ready(function() {
    $.get('/gossip-server/hot-words', function(data) {
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
//    	alert(data.hotKeywords);
//      var tt = data;
//      //alert("22222222222222"+tt);
//      var dd = new Array();
//      dd = tt.split('","');
//      var cc = dd[0].toString();
//      cc = cc.substring(2);
//      dd[0] = cc;
//      cc = dd[dd.length - 1];
//      cc.toString();
//      cc = cc.substring(0, (cc.length - 2));
//      dd[dd.length - 1] = cc;
//      //  for(var i=0;i<dd.length;i++)
//      //alert(dd[i]); 
    	var hotKeywords=data.hotKeywords;
    	//alert(hotKeywords.length);
    	if(hotKeywords==null||hotKeywords.length<1)
    		return;
      $('#sidebar').empty();
      var html = '<div class="sidemodule">';
      html += '<h3 class="sub">热门关键词</h3>';

      for(var i = 0; i < hotKeywords.length; i++) {
        html += '           <a href="#" class="label">' + hotKeywords[i] + '</a>                                                                                                                                                                                           ';
      }
      html+= '</div>';
      $('#sidebar').append(html);
      //  alert(3);
    });
  });


  $(document).ready(function() {
    $.get('/gossip-server/hot-people', function(data) {
//      var tt = data;
//      var dd = new Array();
//      dd = tt.split('","');
//      var cc = dd[0].toString();
//      cc = cc.substring(2);
//      dd[0] = cc;
//      cc = dd[dd.length - 1];
//      cc.toString();
//      cc = cc.substring(0, (cc.length - 2));
//      dd[dd.length - 1] = cc;
//      //  for(var i=0;i<dd.length;i++)
//      //alert(dd[i]); 
    	var hotPeople=data.hotPeople;
    	//alert(hotPeople.length);
    	if(hotPeople==null||hotPeople.length<1)
    		return;
      $('#sidebar div:eq(1)').empty();
      var html = '<div class="sidemodule">';
      html += '<h3 class="sub">热门人名</h3>';

      for(var i = 0; i < hotPeople.length; i++) {
        html += '           <a href="#" class="label">' + hotPeople[i] + '</a>                                                                                                                                                                                           ';
      }
      html+= '</div>';
      $('#sidebar').append(html);
      //alert(3);
    });
  });


  $(document).ready(function() {
    $.get('/gossip-server/hot-places', function(data) {
//      var tt = data;
//      var dd = new Array();
//      dd = tt.split('","');
//      var cc = dd[0].toString();
//      cc = cc.substring(2);
//      dd[0] = cc;
//      cc = dd[dd.length - 1];
//      cc.toString();
//      cc = cc.substring(0, (cc.length - 2));
//      dd[dd.length - 1] = cc;
//      //  for(var i=0;i<dd.length;i++)
//      //  alert(dd[i]); 
    	var hotPlaces=data.hotPlaces;
    	alert(hotPlaces.length);
    	if(hotPlaces==null||hotPlaces.length<1)
    		return;
      $('#sidebar div:eq(2)').empty();
      var html = '<div class="sidemodule">';
      html += '<h3 class="sub">热门地名</h3>';

      for(var i = 0; i < hotPlaces.length; i++) {
        html += '           <a href="#" class="label">' + hotPlaces[i] + '</a>                                                                                                                                                                                           ';
      }
      html+= '</div>';
      $('#sidebar').append(html);
      //  alert(3);
    });
  });
  
