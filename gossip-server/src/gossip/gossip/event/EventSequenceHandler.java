package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 把网页id排序 把事件时间设置为第一篇新闻的时间 关系到事件单页的新闻的排序
 * 
 * @author ChenJie
 * 
 *         Nov 13, 2013
 */
public class EventSequenceHandler implements Handler {
	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void handle(Event event) {
		List<Integer> ids = event.getPagesList();
		Collections.sort(ids);// 升序
		Collections.reverse(ids);// 降序
		/**
		 * 页面序号
		 */
		StringBuilder sb = new StringBuilder();
		int i = 1;
		for (Integer tmp : ids) {
			sb.append(tmp);
			if (i != ids.size()) {
				sb.append(",");
			}
		}
		String pages = sb.toString();

		/**
		 * 事件时间
		 */
		List<News> newsList = event.getNewsList();
		Collections.sort(newsList, new Comparator<News>() {
			@Override
			public int compare(News o1, News o2) {
				String date1 = o1.getDate();
				String date2 = o2.getDate();

				try {
					Date d1 = df.parse(date1);
					Date d2 = df.parse(date2);

					if (d1.before(d2)) {
						return 1;
					} else {
						return -1;
					}
				} catch (ParseException e) {
					return 0;
				}
			}

		});

		String firstTime = newsList.get(newsList.size() - 1).getDate();
		String lastTime = newsList.get(0).getDate();

		System.out.println("事件：" + event.getTitle());
		System.out.println("该事件起始时间：" + firstTime);
		System.out.println("该事件更新时间：" + lastTime);

		try {
			Date beginDate = df.parse(firstTime);
			Date endDate = df.parse(lastTime);
			event.setCreateAt(beginDate);
			event.setUpdateAt(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		event.setPages(pages);
		event.setPagesList(ids);
	}

	public static void main(String[] args) throws ParseException {
		String d = "2013-12-30";
		String d2 = "2013-12-31";
		List<String> list = new ArrayList<>();
		list.add(d);
		list.add(d2);

		Collections.sort(list, new Comparator<String>() {

			@Override
			public int compare(String date1, String date2) {
				try {
					Date d1 = df.parse(date1);
					Date d2 = df.parse(date2);

					if (d1.before(d2)) {
						return 1;
					} else {
						return -1;
					}
				} catch (ParseException e) {
					return 0;
				}
			}
		});
		System.out.println(list);
		Date beginDate = df.parse(d);
		Date endDate = df.parse(d2);
		if (beginDate.before(endDate)) {
			System.out.println("yes");
		}
	}
}
