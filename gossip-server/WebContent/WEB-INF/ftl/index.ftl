
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Carousel Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/css/carousel.css" rel="stylesheet">
  </head>
<!-- NAVBAR
================================================== -->
  <body>
    <div class="navbar-wrapper">
      <div class="container">

        <#include "/head-index.ftl">

    <!-- Carousel
    ================================================== -->
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
      <!-- Indicators -->
      <ol class="carousel-indicators">
        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
        <li data-target="#myCarousel" data-slide-to="1"></li>
        <li data-target="#myCarousel" data-slide-to="2"></li>
        <!--
        <li data-target="#myCarousel" data-slide-to="3"></li>
        <li data-target="#myCarousel" data-slide-to="4"></li>
        -->
      </ol>
      <div class="carousel-inner">
        <div class="item active">
          <img src="/images/slide-01.jpg">
          <div class="container">
            <div class="carousel-caption">
              <h1>事件发现</h1>
              <p>本功能将近期发生的新闻进行事件归纳，按照不同的事件进行划分。</p>
              <p><a class="btn btn-lg btn-primary" href="/events.html" role="button">看看事件</a></p>
            </div>
          </div>
        </div>
        <div class="item">
          <img src="/images/slide-04.jpg">
          <div class="container">
            <div class="carousel-caption">
              <h1>文本摘要</h1>
              <p>本模块实现了新闻文本的自动生成摘要的功能，用户可以仅仅通过摘要了解一篇新闻的主要内容，而不必查看新闻的全部内容</p>
              <p><a class="btn btn-lg btn-primary" href="/events.html" role="button">文本摘要</a></p>
            </div>
          </div>
        </div>
         <div class="item">
          <img src="/images/slide-02.jpg">
          <div class="container">
            <div class="carousel-caption">
              <h1>新闻检索</h1>
              <p>本模块实现了新闻检索功能，用户可以输入检索词对相关新闻进行搜索，同时该模块中加入了查询扩展功能提高查询准确度</p>
              <p><a class="btn btn-lg btn-primary" href="/news-search" role="button">我要搜索</a></p>
            </div>
          </div>
        </div>
        <!--
        <div class="item">
          <img src="/images/slide-02.jpg">
          <div class="container">
            <div class="carousel-caption">
              <h1>词语标注</h1>
              <p>本功能是为了提高分词的准确性而开发，标注之后的词用于生成查询扩展、新闻摘要和内容相似度比较。</p>
              <p><a class="btn btn-lg btn-primary" href="/wordtagging" role="button">我要标注</a></p>
            </div>
          </div>
        </div>
        
        <div class="item">
         <img src="/images/slide-03.jpg">
          <div class="container">
            <div class="carousel-caption">
              <h1>后台管理</h1>
              <p>这里集中了对事件、用户和新闻的管理功能。</p>
              <p><a class="btn btn-lg btn-primary" href="/admin" role="button">进入后台</a></p>
            </div>
          </div>
        </div>
      -->
      </div>
      <a class="left carousel-control" href="#myCarousel" data-slide="prev"><span class="glyphicon glyphicon-chevron-left"></span></a>
      <a class="right carousel-control" href="#myCarousel" data-slide="next"><span class="glyphicon glyphicon-chevron-right"></span></a>
    </div><!-- /.carousel -->

      <!-- FOOTER -->
      <footer>
        <p>&copy; 2013 BIT DLDE</p>
      </footer>

    </div><!-- /.container -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <script src="/js/holder.js"></script>
  </body>
</html>
