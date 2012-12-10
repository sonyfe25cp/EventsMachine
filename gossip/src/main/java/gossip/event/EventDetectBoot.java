package gossip.event;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.bit.dlde.utils.DLDELogger;

import gossip.Boot;
import gossip.Handler;
import gossip.dao.EventDAO;
import gossip.sim.SimilarDocPair;
import gossip.sim.SimilarityReader;
import gossip.utils.DateTrans;

/**
 * 事件检测任务的启动器。使用构造函数创建时，必须添加各种handler，否则的话请暂时使用工厂方法getMockInstance()。
 * 获得的事件的creat_time由处理的相似度文件名称决定；keywords，title，recommended以及事件的过期由各个handler决定
 * PS:当前事件过期的机制是事件距离当天不超过一天，这就会导致假如我想录入以前的数据无法进行，不过影响不是很大，关键是以后得一直运行
 * 
 * @author lins 2012-8-14
 */
public class EventDetectBoot extends Boot {
	DLDELogger log = new DLDELogger();
	String previousProcessedFileName = "";
	public EventDetectBoot(){
	}
	private int date;
	public EventDetectBoot(int date){
		this.date = date;
	}
	public void process() {
		if(date == 0){
			Calendar cal = Calendar.getInstance();
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH) + 1;
			int year = cal.get(Calendar.YEAR);
			int today = DateTrans.YYMMDDToInt(year, month, day);
			for(int begin = 20120601; begin <= today; begin = DateTrans.theDayAfterYYMMDD(begin)){
				process_once(begin);
			}
		}else{
			process_once(date);
		}
	}

	/**
	 * 从sim-info目录读最新的相似读文件，检测事件，将事件入库，然后删除相似度文件
	 * 
	 * @see gossip.Boot#process()
	 */
	public void process_once(int date) {
		log.info("begin to detect events of "+date);
		/** 初始化 **/
		EventDAO dao = new EventDAO();
		dao.init();
		/** 根据一些老的事件获得更新后的事件 **/
		HashSet<Event> events = detectEvents(date);
		/** 只有当old被更新时才进行如下操作 **/
		if (events.size() != 0) {
			log.info("Found new EVENTS!");
			/** 对获得的事件进行后期处理，需要我们写各种handler，当前只是使用一些mock的handler **/
			for (Handler handler : handlers) {
				log.info(handler.getClass().getName() + " begin to  hadle events");
				handler.handle(events);
				log.info(handler.getClass().getName() + " hadle events over");
			}
			/** 数据入库 **/
			dao.saveORupdateEvents(events);
			/** 放入memcached，暂无 **/
			// Iterator<Event> it = old.iterator();
			// while (it.hasNext()) {
			//
			// }
		}
	}

	private final String[] fields = new String[] { "title", "body" };

	/**
	 * 检查新的事件并以此更新参数old. 使用旧的HashSet<Event> old进行累加式的计算
	 * 
	 */
	public HashSet<Event> detectEvents(int date) {
		HashSet<Event> events = new HashSet<Event>();
		/** 初始化一个记录了某某文档被归到某某event的map **/
		Map<Integer, Event> docEventMap = new HashMap<Integer, Event>();

		/** 读取最新的相似度文件并返回对应的List<SimilarDocPair> **/
		SimilarityReader sreader = new SimilarityReader();
		for (String f : fields) {
			sreader.readSimilarityFromFile(f, date+"");
			List<SimilarDocPair> resultsList = sreader.getSimilarDocPairList();
			if (resultsList == null)
				continue;

			/** 检测事件 **/
			Event event = null;
			for (SimilarDocPair result : resultsList) {
				int doc1 = result.getDoc1();
				int doc2 = result.getDoc2();

				if (docEventMap.containsKey(doc1)) {
					event = docEventMap.get(doc1);
				} else if (docEventMap.containsKey(doc2)) {
					event = docEventMap.get(doc2);
				} else {
					event = new Event();
					event.setCreateTime(sreader.getDateInLastProcessedFile()
							.getTime());
					events.add(event);
				}
				if (!event.getPages().contains(doc1))
					event.addPage(doc1);
				if (!event.getPages().contains(doc2))
					event.addPage(doc2);

				docEventMap.put(doc1, event);
				docEventMap.put(doc2, event);
			}
		}

		return events;
	}

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
				try{
					date_int = Integer.parseInt(args[i + 1]);
				}catch(Exception e){
					System.out.println("-d 参数不合法，请输入格式为 YYYYMMDD,如 20121206");
					System.exit(0);
				}
				i += 2;
			}else if (args[i].equals("-h")) {
				System.out.println("-d 计算某天的相似度,默認值：今天，例如 20121206；若值为0，则表示全部计算");
				System.exit(0);
			}
		}
		
		EventDetectBoot edb = new EventDetectBoot(0);
		edb.addHandler(new SimpleKeyWordsHandler());
		edb.addHandler(new SimpleTitleHandler());
		edb.addHandler(new MockRecommendedHndler());
		edb.addHandler(new SimpleNewsSortHandler());
		edb.addHandler(new SimpleDescHandler());
		edb.run();
	}
}
