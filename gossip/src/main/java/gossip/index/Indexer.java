package gossip.index;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import edu.bit.dlde.model.DLDEWebPage;
import edu.bit.dlde.utils.DLDEConfiguration;
import edu.bit.dlde.utils.DLDELogger;
import gossip.parse.ParseNewsFromWKS;

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

			List<DLDEWebPage> list = ParseNewsFromWKS.parse();
			int count = 0;
			Document doc = null;
			for (DLDEWebPage page : list) {
				doc = new Document();
				Field uniqueField = new Field("id", count + "", Store.YES,
						Index.NOT_ANALYZED);
				if (page.getTitle() == null) {
					System.out.println("url:" + page.getUrl()+ "  ------------------------- no title");
					continue;
				}

				Field titleField = new Field("title", page.getTitle(),
						Store.YES, Index.ANALYZED, TermVector.YES);
				if (page.getBody() == null) {
					System.out.println("url:" + page.getUrl() + "  ------------------------- no body");
					continue;
				}
				Field contentField = new Field("body", page.getBody(),
						Store.YES, Index.ANALYZED, TermVector.YES);
				Field authorField = new Field("author",
						page.getAuthor() == null ? "" : page.getAuthor(),
						Store.YES, Index.NOT_ANALYZED);
				Field urlField = new Field("url", page.getUrl(), Store.YES,
						Index.NOT_ANALYZED);
				String date = page.getDate();
				if (date == null) {
					System.out.println("url:" + page.getUrl() + "  ------------------------- no date");
					continue;
				}
				Field dateField = new Field("date", page.getDate(), Store.YES,
						Index.NOT_ANALYZED);// 显示用
				Field siteField = new Field("site",
						page.getFrom_site() == null ? "" : page.getFrom_site(),
						Store.YES, Index.ANALYZED);

				NumericField crawlAt = new NumericField("crawl_at",
						Field.Store.YES, true).setIntValue(0);// 便于范围查找

				Field dateIntField = new Field("date_int", page.getCrawl_at(),
						Store.YES, Index.NOT_ANALYZED);// 便于查找某一天
				crawlAt.setIntValue(Integer.parseInt(page.getCrawl_at()));

				doc.add(uniqueField);
				doc.add(titleField);
				doc.add(contentField);
				doc.add(authorField);
				doc.add(urlField);
				doc.add(dateField);
				doc.add(siteField);
				doc.add(crawlAt);
				doc.add(dateIntField);

				iw.addDocument(doc);
				count++;
				if (count % 100 == 0)
					iw.commit();
			}
			iw.commit();
			iw.close();
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
