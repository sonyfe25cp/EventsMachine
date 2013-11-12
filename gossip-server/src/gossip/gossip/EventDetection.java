package gossip.gossip;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.bit.dlde.utils.DLDELogger;
import gossip.gossip.service.GossipEventService;
import gossip.gossip.service.GossipNewsService;
import gossip.gossip.utils.DateTrans;
import gossip.model.Event;
import gossip.model.News;

public class EventDetection {
	private GossipNewsService newsService;
	private GossipEventService eventService;

	public static void main(String[] args) {
		DLDELogger logger = new DLDELogger();
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		logger.info("year:" + year + ",month:" + month + ",day:" + day);
		int date_int = DateTrans.YYMMDDToInt(year, month, day);
		for (int i = 0; i < args.length;) {
			if (args[i].equals("-d")) {
				try {
					date_int = Integer.parseInt(args[i + 1]);
				} catch (Exception e) {
					System.out.println("-d 参数不合法，请输入格式为 YYYYMMDD,如 20121206");
					System.exit(0);
				}
				i += 2;
			} else if (args[i].equals("-h")) {
				System.out
						.println("-d 计算某天的相似度,默認值：今天，例如 20121206；若值为0，则表示全部计算");
				System.exit(0);
			}
		}

	}
	public void run(){
		Date now = new Date(System.currentTimeMillis());
		//step0. 取出今天的新闻包括本周的未被识别为事件的新闻
		List<News> newsToday = newsService.getNewsLast7Days(now);
		//step1. 找出今天的events，标记没有存为事件的那些新闻
		List<Event> eventsTody = eventService.computeEventFromNews(newsToday);
		//step2. 找出前面7天的events
		List<Event> eventsLast7Days = eventService.getEventsLast7Days(now);
		//step3. 合并今天的与本周的，并找出新的
		List<Event> newOrUpdatedEvents = eventService.mergeEvents(eventsLast7Days, eventsTody);
		//step4. 插入or更新这些events
		eventService.updateOrInsert(newOrUpdatedEvents);
	}

}
