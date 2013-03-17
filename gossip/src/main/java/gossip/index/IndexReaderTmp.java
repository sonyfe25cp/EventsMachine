package gossip.index;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;

import edu.bit.dlde.model.DLDEWebPage;
import edu.bit.dlde.utils.DLDEConfiguration;
import gossip.utils.SystemSettings;
import gossip.utils.Utils;

public class IndexReaderTmp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//http://news.qq.com/a/20120618/000315.htm -- 重庆市第四次党代会开幕 张德江作报告
		//http://news.qq.com/a/20120618/000365.htm -- 重庆市第四次党代会开幕 张德江作报告
		//http://news.qq.com/a/20120618/000344.htm -- 快讯：重庆市第四次党代会开幕 张德江作报告
		//http://news.qq.com/a/20120618/000468.htm
//		String url="http://news.qq.com/a/20120618/000468.htm";
		String url="http://news.qq.com/a/20120618/000344.htm";
//		String url="http://news.qq.com/a/20120618/000468.htm";
//		String url="http://news.qq.com/a/20120618/000468.htm";
		doc_compare(url);
	}
	
	public static void doc_compare(String url) {
//		String indexPath = SystemSettings.indexPath;
		String indexPath = DLDEConfiguration.getInstance("gossip.properties").getValue(
						"IndexPath");
		try {
			MMapDirectory dir = new MMapDirectory(new File(indexPath));
			IndexReader ir = IndexReader.open(dir, true);
			IndexSearcher searcher=new IndexSearcher(ir);
			
			TopDocs docs=searcher.search(new TermQuery(new Term("url",url)), 1);
			ScoreDoc[] sdocs=docs.scoreDocs;
			for(ScoreDoc sdoc:sdocs){
				int docId=sdoc.doc;
				Document document = ir.document(docId);
				System.out.println("docId:"+docId);
				DLDEWebPage page=Utils.transfrom(document);
				System.out.println(page.toString());
			}
			searcher.close();
			ir.close();
			dir.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}

}
