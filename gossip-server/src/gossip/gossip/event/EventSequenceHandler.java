package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
/**
 * 把网页id排序
 * 把事件时间设置为第一篇新闻的时间
 * 关系到事件单页的新闻的排序
 * @author ChenJie
 *
 * Nov 13, 2013
 */
public class EventSequenceHandler implements Handler{

	@Override
	public void handle(Event event) {
		List<Integer> ids = event.getPagesList();
		Collections.sort(ids);//升序
		int first = ids.get(0);
		Collections.reverse(ids);//降序
		int last = ids.get(0);
		/**
		 * 页面序号
		 */
		StringBuilder sb =  new StringBuilder();
		int i = 1;
		for(Integer tmp : ids){
			sb.append(tmp);
			if(i != ids.size()){
				sb.append(",");
			}
		}
		String pages = sb.toString();
		
		/**
		 * 事件时间
		 */
		News firstNews = null, lastNews = null;
		List<News> newsList = event.getNewsList();
		for(News tmp : newsList){
			int id = tmp.getId();
			if(id == first){
				firstNews = tmp;
			}else if(id == last){
				lastNews = tmp;
			}
		}
		String firstTime = firstNews.getDate();
		String lastTime = lastNews.getDate();
		
		System.out.println("事件：" + event.getTitle());
		System.out.println("该事件起始时间：" + firstTime);
		System.out.println("该事件更新时间：" + lastTime);
		
		//2013-11-30 日期格式
		
		Date beginDate = transFromString(firstTime);
		Date endDate = transFromString(lastTime);
		
		event.setCreateAt(beginDate);
		event.setUpdateAt(endDate);
		event.setPages(pages);
		event.setPagesList(ids);
	}

	//2013-11-30
	private Date transFromString(String stringDate){
		String[] firstTimeTmp = stringDate.split("-");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(firstTimeTmp[0]));
		cal.set(Calendar.MONTH, Integer.parseInt(firstTimeTmp[1])-1);
		cal.set(Calendar.DATE, Integer.parseInt(firstTimeTmp[2]));
		return cal.getTime();
	}
	
	public static void main(String[] args){
		String d = "2013-12-30";
		EventSequenceHandler esh = new EventSequenceHandler();
		Date date = esh.transFromString(d);
		System.out.println(date);
	}
}
