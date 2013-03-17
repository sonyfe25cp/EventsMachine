package gossip.index;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.dao.NewsDAO;
import gossip.model.News;
import gossip.utils.DatabaseUtils;

/**
 * 临时建索引方案
 * 
 * @author ChenJie
 * 
 */
public class Indexer {
	DLDELogger logger = new DLDELogger();
	public static void main(String[] args) {
		Indexer index = new Indexer();
		index.index();
	}

	String indexPath = DLDEConfiguration.getInstance("gossip.properties").getValue("IndexPath");

	void index() {

		IndexWriter iw = null;
		IKAnalyzer analyzer = new IKAnalyzer();
		try {
			MMapDirectory dir = new MMapDirectory(new File(indexPath));

			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_35, analyzer);

			iw = new IndexWriter(dir, conf);

			Connection conn = DatabaseUtils.getInstance().getConnection();
			NewsDAO newsDao = new NewsDAO();
			newsDao.init(conn);
			List<News> list = newsDao.getFreshNews();
			int count = 0;
			for (News news : list) {
				iw.addDocument(news.toDocument());
				count++;
				if (count % 100 == 0)
					iw.commit();
			}
			iw.commit();
			iw.close();
			newsDao.batchUpdateNews(list, News.INDEX);
			newsDao.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (iw != null) {
				try {
					iw.close();
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
