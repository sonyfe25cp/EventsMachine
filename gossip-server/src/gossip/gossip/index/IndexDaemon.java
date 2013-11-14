//package gossip.gossip.index;
//
//import edu.bit.dlde.eventserver.Notifier;
//import edu.bit.dlde.eventserver.SingleEngine;
//import edu.bit.dlde.eventserver.adapter.EventAdapter;
//import edu.bit.dlde.eventserver.model.Request;
//import edu.bit.dlde.eventserver.model.Response;
//import edu.bit.dlde.utils.DLDEConfiguration;
//import gossip.gossip.Boot;
//import gossip.gossip.summary.GenerateSummary;
//
//import java.io.File;
//import java.io.IOException;
//
//import net.sf.json.JSONObject;
//
//import org.apache.lucene.analysis.Analyzer;
//import org.apache.lucene.analysis.standard.StandardAnalyzer;
//import org.apache.lucene.document.Document;
//import org.apache.lucene.document.Fieldable;
//import org.apache.lucene.index.CorruptIndexException;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.queryParser.ParseException;
//import org.apache.lucene.queryParser.QueryParser;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.NumericRangeQuery;
//import org.apache.lucene.search.Query;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.Sort;
//import org.apache.lucene.search.SortField;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.store.MMapDirectory;
//import org.apache.lucene.util.Version;
//
///**
// * 索引的守护进程。期望实现的功能有2：A能够读取通过socket传递过来的docid来读出文档并返回；B能够自动检测文件夹并建立索引，归档旧文件等等工作
// * 
// * @author lins 2012-8-14
// */
//public class IndexDaemon extends Boot {
//	private String indexPath = DLDEConfiguration.getInstance(
//			"gossip.gossip.properties").getValue("IndexPath");// 索引路径
//	String ip = "localhost";
//	int port = 7777;
//
//	public void process() {
//		/** 创建handler，该handler处理socket传来的docid，并根据docid读索引 **/
//		IndexServerHandler handler = new IndexServerHandler();
//		MMapDirectory dir;
//		try {
//			dir = new MMapDirectory(new File(indexPath));
//			IndexReader ir = IndexReader.open(dir, true);
//			handler.setIndexReader(ir);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//			return;
//		}
//
//		/** 向notifier注册监听器handler **/
//		Notifier notifier = Notifier.getNotifier();
//		notifier.addListener(handler);
//
//		/** 启动监听socket的实例 **/
//		System.out.println("Server starting ...");
//		SingleEngine server = null;
//		try {
//			System.out.println("begin to listen ip:" + ip + " , port:" + port);
//			server = new SingleEngine(ip, port);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Server start error , begin to shut down ...");
//			return;
//		}
//		Thread tServer = new Thread(server);
//		tServer.start();
//	}
//
//	/**
//	 * 
//	 */
//	class IndexServerHandler extends EventAdapter {
//		IndexReader indexReader;
//
//		public void setIndexReader(IndexReader indexReader) {
//			this.indexReader = indexReader;
//		}
//
//		public void onRead(Request request) throws Exception {
//
//		}
//
//		public void onWrite(Request request, Response response) {
//			JSONObject jsonObj;
//			try {
//				String str = new String(request.getDataInput());
//				jsonObj = JSONObject.fromObject(str);
//			} catch (Exception e1) {
//				e1.printStackTrace();
//				return;
//			}
//			if (jsonObj == null)
//				return;
//			int operation = jsonObj.getInt("operation");
//
//			switch (operation) {
//			case 0:
//				try {
//					int docId = jsonObj.getInt("id");
//					Document document = indexReader.document(docId);
//					JSONObject repJsonObj = new JSONObject();
//					
//					repJsonObj.put("id", docId);
//					
//					repJsonObj.put("title", document.getFieldable("title")
//							.stringValue());
//					
//					Fieldable antiNullPointer = document.getFieldable("desc");
//					if (antiNullPointer == null){
//						GenerateSummary gs=new GenerateSummary();
//						String desc=gs.summary(docId);
//						repJsonObj.put("desc", desc);
//					}
//						
//					else
//						repJsonObj.put("desc", antiNullPointer.stringValue());
//					antiNullPointer = null;
//					
//					antiNullPointer = document.getFieldable("author");
//					if (antiNullPointer == null)
//						repJsonObj.put("author", "");
//					else
//						repJsonObj.put("author", antiNullPointer.stringValue());
//					antiNullPointer = null;
//					
//					repJsonObj.put("body", document.getFieldable("body")
//							.stringValue());
//					
//					repJsonObj.put("publish_at", document.getFieldable("date")
//							.stringValue());
//					
//					repJsonObj.put("source", document.getFieldable("url")
//							.stringValue());
//					
//					antiNullPointer = document.getFieldable("started_location");
//					if (antiNullPointer == null)
//						repJsonObj.put("started_location", "");
//					else
//						repJsonObj.put("started_location", antiNullPointer.stringValue());
//					antiNullPointer = null;
//					
//					antiNullPointer = document.getFieldable("keywords");
//					if (antiNullPointer == null)
//						repJsonObj.put("keywords", "");
//					else
//						repJsonObj.put("keywords", antiNullPointer.stringValue());
//					
//					response.send(repJsonObj.toString().getBytes());
//				} catch (CorruptIndexException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				break;
//			case 1:
//				JSONObject repJsonObj = new JSONObject();
//				int begin = jsonObj.getInt("begin"),
//				offset = jsonObj.getInt("offset");
//				IndexSearcher searcher = new IndexSearcher(indexReader);
//				QueryParser parser = new MyQueryParser(Version.LUCENE_35,
//						"date_int", new StandardAnalyzer(Version.LUCENE_35));
//				try {
//					Query query = parser.parse("date_int:[0 TO 9999999]");
//					Sort sort = new Sort(new SortField("date_int",
//							SortField.INT, true));
//					TopDocs hits = searcher.search(query, 100, sort);
//					ScoreDoc[] sd = hits.scoreDocs;
//					for (int i = begin; i < sd.length && i < begin + offset; i++) {
//						repJsonObj.accumulate(i + "", sd[i].doc);
//					}
//					repJsonObj.accumulate("total", indexReader.numDocs());
//					response.send(repJsonObj.toString().getBytes());
//				} catch (ParseException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				break;
//			default:
//				break;
//			}
//		}
//	}
//
//	/**
//	 * 自定义QueryParser， 提供数字范围查询支持
//	 * 
//	 * @author Ping_QC
//	 * 
//	 */
//	public class MyQueryParser extends QueryParser {
//		public MyQueryParser(Version matchVersion, String field,
//				Analyzer analyzer) {
//			super(matchVersion, field, analyzer);
//		}
//
//		@Override
//		protected org.apache.lucene.search.Query getRangeQuery(String field,
//				String part1, String part2, boolean inclusive)
//				throws ParseException {
//			if ("size".equals(field) || "date".equals(field)) {
//				return NumericRangeQuery.newLongRange(field,
//						Long.parseLong(part1), Long.parseLong(part2),
//						inclusive, inclusive);
//			}
//			return super.newRangeQuery(field, part1, part2, inclusive);
//		}
//	}
//
//	public static void main(String[] args) {
//		IndexDaemon indexDaemon = new IndexDaemon();
//		indexDaemon.run();
//	}
//}
