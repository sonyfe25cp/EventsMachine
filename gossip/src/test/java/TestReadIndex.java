//import java.io.File;
//import java.io.IOException;
//
//import junit.framework.TestCase;
//
//import org.apache.lucene.document.Document;
//import org.apache.lucene.index.IndexReader;
//import org.apache.lucene.index.Term;
//import org.apache.lucene.search.IndexSearcher;
//import org.apache.lucene.search.ScoreDoc;
//import org.apache.lucene.search.TermQuery;
//import org.apache.lucene.search.TopDocs;
//import org.apache.lucene.store.MMapDirectory;
//import org.junit.Test;
//
//import edu.bit.dlde.model.DLDEWebPage;
//import edu.bit.dlde.utils.Utils;
//
//
//public class TestReadIndex extends TestCase{
//	String indexPath = "/data/gossip/index";
//	MMapDirectory dir;
//	IndexReader ir;
//	IndexSearcher is;
//	public TestReadIndex(){
//		init();
//	}
//	public void init(){
//		MMapDirectory dir;
//		try {
//			dir = new MMapDirectory(new File(indexPath));
//			ir = IndexReader.open(dir, true);
//			is=new IndexSearcher(ir);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	public void close(){
//		try {
//			is.close();
//			ir.close();
//			dir.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	@Test
//	public void testReader() throws IOException {
//		int totalNum = 0;
//		try {
//			totalNum = ir.maxDoc();
//			System.out.println("total:" + totalNum);
//			
//		}catch(Exception e){
//			
//		}
//	}
//	
//	@Test
//	public void testSearcher(){
//		try {
//			int day1= 2012 * 12 * 31 + 6 * 31 + 15;//2012/7/12
//			int day2 =2012 * 12 * 31 + 6 * 31 + 16;//2012/7/12
//			
//			int day=748680;
//			
////			TopDocs docs=is.search(new TermQuery(new Term("id","3615")),100);
////			TopDocs docs=is.search(new TermQuery(new Term("crawl_at",day+"")),100);
//			
//			TopDocs docs=is.search(new TermQuery(new Term("date_int",day+"")),100);
//			
////			Query q = NumericRangeQuery.newIntRange("crawl_at", day1, day2, true, true);
////			TopDocs docs=is.search(q,100);
//			
//			
//			ScoreDoc[] sdocs=docs.scoreDocs;
//			for(ScoreDoc doc:sdocs){
//				int docId=doc.doc;
//				Document document = ir.document(docId);
//
//				DLDEWebPage page1=Utils.transfrom(document);
//				
//				System.out.println(page1.toString());
//			}
//			
//			System.out.println(docs.totalHits);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//}
