package gossip.sim;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.MMapDirectory;

import edu.bit.dlde.model.DLDEWebPage;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.utils.Utils;

/**
 * 纯粹从索引中查看文档的一个类。方便对比相似度以及事件。
 */
public class Compare {
	private static DLDELogger logger = new DLDELogger();

	/**
	 * @param args
	 * Jul 13, 2012
	 */
	public static void main(String[] args) {
		int[] docs={3741,3798,3741,3747,3741,3785,3741,3751,3741,3770};
		doc_compare(docs);

	}
	public static void doc_compare(int... docIds) {
		doc_compare(false,docIds);
	}
	public static void doc_compare(boolean title_only,int... docIds) {
//		String indexPath = SystemSettings.indexPath;
		String indexPath = DLDEConfiguration.getInstance("gossip.properties").getValue(
				"IndexPath");
		try {
			MMapDirectory dir = new MMapDirectory(new File(indexPath));
			IndexReader ir = IndexReader.open(dir, true);
			for(int docId:docIds){
				Document document = ir.document(docId);
	
				DLDEWebPage page=Utils.transfrom(document);
				if(!title_only){
					logger.info(page.toString());
				}else{
					logger.info(page.getUrl()+" -- "+page.getTitle());
				}
			}
			ir.close();
			dir.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}

}
