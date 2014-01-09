<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css"
	media="all">
<link rel="stylesheet" type="text/css" href="css/style.css" media="all">
<script type="text/javascript" src="js/jquery-1.8.2.js"></script>
<script type="text/javascript" src="js/bootstrap.js"></script>
<script type="text/javascript" src="js/wordtagging.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>中文词语标注页面</title>
</head>
<body>
  
  <div class="container">
     <div class="page-header" style="margin-top:70px;">
         <h1 style="line-height:50px;">这是一个中文词语标注网页，如果您觉得页面上显示的词语对分析数据有用，请选择“保留”，若您觉得没用请选择“放弃”，否则，请选择“跳过”。</h1>
     </div>
     <div id = "container-inner" style = "padding: 20px 0;border:2px solid #AAA;">
        <div id = "word" style="height:100px;line-height:100px;text-align:center">
             <p id ="keyword" class="keyword" style="font-size:60px;text-align:center"></p>
             <input type="hidden" id = "keywordId">
        </div>
        <div id = "tagging" style="text-align:center">
             <button id = "keep-word"  class = "btn-large">保留</button>
             <button id = "jump-word"  class = "btn-large">跳过</button>
             <button id = "abort-word" class = "btn-large">放弃</button>
        </div>
     </div>
     <div style="margin-top:3px;"><p style="font-size:15px;">上述词语表述中，“|”前面为关键词，“|”后面为该词的词性标注，其中以“n”开头的为名词，以“v”开头的为动词，
         标注过程中，主要考虑这两种词性的词语。具体词性对照表见该页面最后的表格。</p></div>
     <div class="page-footer" style="margin-top:40px;">
         <p style="font-size:40px;"><strong>举例说明：</strong></p>
         <br>
         <p style="font-size:20px;line-height:35px;">1）诸如<strong>“食品安全”、“中标率”、
         “亚太经合组织”</strong>等名词很可能是某条新闻的关键词，可以考虑<strong>保留</strong>。</p>
         <p style="font-size:20px;line-height:35px;">2）诸如<strong>“吸毒”、“哄抢”、
         “受贿”</strong>等区分性很强的动词，也可以选择<strong>保留</strong>。</p>
         <p style="font-size:20px;line-height:35px;">3）对于像<strong>“小毛头”、“二招”、
         “头昏眼花”、“该厂”、“十点”</strong>等没有区分性甚至不是词语的词组可以考虑<strong>放弃</strong>。</p>
         <p style="font-size:20px;line-height:35px;">4）对于自己不能确定是否有用的词组，可以选择<strong>跳过</strong>。</p>
		<div id="cingxing" style="margin-top:50px;border-top:2px solid #AAA;">
		    <br>
		    <p style="font-size:40px;">汉语词性对照表：</p>
			<table style="table-layout: fixed; width: 100%">
				<tbody>
					<tr>
						<td>
							<div class="cnt" id="blog_text">
								<table cellspacing="0" cols="5" rules="all" border="1"
									frame="box"
									style="border-right: #999999 1px solid; border-top: #999999 1px solid; margin-top: 0.6em; font-size: 100%; margin-bottom: 0.3em; border-left: #999999 1px solid; width: 100%; border-bottom: #999999 1px solid">
									<tbody>
										<tr valign="top" bgcolor="#cccccc">
											<th align="middle" width="17%"
												style="border-right: #999999 1px solid; padding-right: 6px; border-top: #999999 1px solid; padding-left: 6px; padding-bottom: 4px; border-left: #999999 1px solid; padding-top: 4px; border-bottom: #999999 1px solid; text-align: left">
												<center>词性编码</center>
											</th>
											<th align="middle" width="17%"
												style="border-right: #999999 1px solid; padding-right: 6px; border-top: #999999 1px solid; padding-left: 6px; padding-bottom: 4px; border-left: #999999 1px solid; padding-top: 4px; border-bottom: #999999 1px solid; text-align: left">
												<center>词性名称</center>
											</th>
											<th align="middle" width="66%"
												style="border-right: #999999 1px solid; padding-right: 6px; border-top: #999999 1px solid; padding-left: 6px; padding-bottom: 4px; border-left: #999999 1px solid; padding-top: 4px; border-bottom: #999999 1px solid; text-align: left">
												注 解</th>
										</tr>
										<tr valign="top">
											<td class="hang" width="17%" height="31">
												<p align="center">
													<strong>Ag</strong>
												</p>
											</td>
											<td class="hang" width="17%">
												<p>形语素</p>
											</td>
											<td class="hang" width="66%">
												<p>形容词性语素。形容词代码为 a，语素代码ｇ前面置以A。</p>
											</td>
										</tr>
										<tr valign="top">
											<td class="hang" width="17%">
												<p align="center">
													<strong>a</strong>
												</p>
											</td>
											<td class="hang" width="17%">
												<p>形容词</p>
											</td>
											<td class="hang" width="66%">
												<p>取英语形容词 adjective的第1个字母。</p>
											</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>ad</strong>
												</p>
											</td>
											<td class="hang">
												<p>副形词</p>
											</td>
											<td class="hang">
												<p>直接作状语的形容词。形容词代码 a和副词代码d并在一起。</p>
											</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>an</strong>
												</p>
											</td>
											<td class="hang">
												<p>名形词</p>
											</td>
											<td class="hang">具有名词功能的形容词。形容词代码 a和名词代码n并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>b</strong>
												</p>
											</td>
											<td class="hang">
												<p>区别词</p>
											</td>
											<td class="hang">取汉字“别”的声母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>c</strong>
												</p>
											</td>
											<td class="hang">
												<p>连词</p>
											</td>
											<td class="hang">取英语连词 conjunction的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang" width="17%">
												<div align="center">
													<strong>dg</strong>
												</div>
											</td>
											<td class="hang" width="17%">
												<p>副语素</p>
											</td>
											<td class="hang" width="66%">副词性语素。副词代码为 d，语素代码ｇ前面置以D。</td>
										</tr>
										<tr valign="top">
											<td class="hang" height="31">
												<p align="center">
													<strong>d</strong>
												</p>
											</td>
											<td class="hang">
												<p>副词</p>
											</td>
											<td class="hang">取 adverb的第2个字母，因其第1个字母已用于形容词。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>e</strong>
												</p>
											</td>
											<td class="hang">
												<p>叹词</p>
											</td>
											<td class="hang">取英语叹词 exclamation的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>f</strong>
												</p>
											</td>
											<td class="hang">
												<p>方位词</p>
											</td>
											<td class="hang">取汉字“方”</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">g</span>
												</p>
											</td>
											<td class="hang">
												<p>语素</p>
											</td>
											<td class="hang">绝大多数语素都能作为合成词的“词根”，取汉字“根”的声母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">h</span>
												</p>
											</td>
											<td class="hang">
												<p>前接成分</p>
											</td>
											<td class="hang">取英语 head的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">i</span>
												</p>
											</td>
											<td class="hang">
												<p>成语</p>
											</td>
											<td class="hang">取英语成语 idiom的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">j</span>
												</p>
											</td>
											<td class="hang">
												<p>简称略语</p>
											</td>
											<td class="hang">取汉字“简”的声母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">k</span>
												</p>
											</td>
											<td class="hang">
												<p>后接成分</p>
											</td>
											<td class="hang">&nbsp;</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>l</strong>
												</p>
											</td>
											<td class="hang">
												<p>习用语</p>
											</td>
											<td class="hang">习用语尚未成为成语，有点“临时性”，取“临”的声母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>m</strong>
												</p>
											</td>
											<td class="hang">
												<p>数词</p>
											</td>
											<td class="hang">取英语 numeral的第3个字母，n，u已有他用。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>Ng</strong>
												</p>
											</td>
											<td class="hang">
												<p>名语素</p>
											</td>
											<td class="hang">名词性语素。名词代码为 n，语素代码ｇ前面置以N。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>n</strong>
												</p>
											</td>
											<td class="hang">
												<p>名词</p>
											</td>
											<td class="hang">取英语名词 noun的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>nr</strong>
												</p>
											</td>
											<td class="hang">
												<p>人名</p>
											</td>
											<td class="hang">名词代码 n和“人(ren)”的声母并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>ns</strong>
												</p>
											</td>
											<td class="hang">
												<p>地名</p>
											</td>
											<td class="hang">名词代码 n和处所词代码s并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>nt</strong>
												</p>
											</td>
											<td class="hang">
												<p>机构团体</p>
											</td>
											<td class="hang">“团”的声母为 t，名词代码n和t并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>nz</strong>
												</p>
											</td>
											<td class="hang">
												<p>其他专名</p>
											</td>
											<td class="hang">“专”的声母的第 1个字母为z，名词代码n和z并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>o</strong>
												</p>
											</td>
											<td class="hang">
												<p>拟声词</p>
											</td>
											<td class="hang">取英语拟声词 onomatopoeia的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>p</strong>
												</p>
											</td>
											<td class="hang">
												<p>介词</p>
											</td>
											<td class="hang">取英语介词 prepositional的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>q</strong>
												</p>
											</td>
											<td class="hang">
												<p>量词</p>
											</td>
											<td class="hang">取英语 quantity的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>r</strong>
												</p>
											</td>
											<td class="hang">
												<p>代词</p>
											</td>
											<td class="hang">取英语代词 pronoun的第2个字母,因p已用于介词。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>s</strong>
												</p>
											</td>
											<td class="hang">
												<p>处所词</p>
											</td>
											<td class="hang">取英语 space的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>tg</strong>
												</p>
											</td>
											<td class="hang">
												<p>时语素</p>
											</td>
											<td class="hang">时间词性语素。时间词代码为 t,在语素的代码g前面置以T。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>t</strong>
												</p>
											</td>
											<td class="hang">
												<p>时间词</p>
											</td>
											<td class="hang">取英语 time的第1个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">u</span>
												</p>
											</td>
											<td class="hang">
												<p>助词</p>
											</td>
											<td class="hang">取英语助词 auxiliary</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>vg</strong>
												</p>
											</td>
											<td class="hang">
												<p>动语素</p>
											</td>
											<td class="hang">动词性语素。动词代码为 v。在语素的代码g前面置以V。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">v</span>
												</p>
											</td>
											<td class="hang">
												<p>动词</p>
											</td>
											<td class="hang">取英语动词 verb的第一个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>vd</strong>
												</p>
											</td>
											<td class="hang">
												<p>副动词</p>
											</td>
											<td class="hang">直接作状语的动词。动词和副词的代码并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>vn</strong>
												</p>
											</td>
											<td class="hang">
												<p>名动词</p>
											</td>
											<td class="hang">指具有名词功能的动词。动词和名词的代码并在一起。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>w</strong>
												</p>
											</td>
											<td class="hang">
												<p>标点符号</p>
											</td>
											<td class="hang">&nbsp;</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<span class="cfe">x</span>
												</p>
											</td>
											<td class="hang">
												<p>非语素字</p>
											</td>
											<td class="hang">非语素字只是一个符号，字母 x通常用于代表未知数、符号。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>y</strong>
												</p>
											</td>
											<td class="hang">
												<p>语气词</p>
											</td>
											<td class="hang">取汉字“语”的声母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>z</strong>
												</p>
											</td>
											<td class="hang">
												<p>状态词</p>
											</td>
											<td class="hang">取汉字“状”的声母的前一个字母。</td>
										</tr>
										<tr valign="top">
											<td class="hang">
												<p align="center">
													<strong>un</strong>
												</p>
											</td>
											<td class="hang">
												<p>未知词</p>
											</td>
											<td class="hang">不可识别词及用户自定义词组。取英文Unkonwn首两个字母。<strong>(非北大标准，CSW分词中定义)
											</strong>
											</td>
										</tr>
									</tbody>
								</table>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>

</body>
</html>