import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.math.NumberRange;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;

import edu.bit.dlde.utils.DLDEConfiguration;
import gossip.model.News;


public class TestSearch {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		int date,i =20120706;
		int end = 20121212;

		String indexPath = DLDEConfiguration.getInstance("gossip.properties").getValue("IndexPath");
		MMapDirectory dir = new MMapDirectory(new File(indexPath));
		IndexReader reader = IndexReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(reader);
//		for(int i = date; i< end; i++){
//			TopDocs docs = searcher.search(new TermQuery(new Term("crawlat", i + "")),searcher.maxDoc());
			NumericRangeQuery nrq= NumericRangeQuery.newIntRange("crawlat", i, i+10, true, true);
			TopDocs docs = searcher.search(nrq,searcher.maxDoc());
			ScoreDoc[] sdocs = docs.scoreDocs;
			for(ScoreDoc sdoc : sdocs){
				Document doc = searcher.doc(sdoc.doc);
				System.out.println(News.fromDocument(doc));
			}
			System.out.println("date at - "+i+" sdocs.size: "+sdocs.length);
//		}
		searcher.close();
		reader.close();
		dir.close();
	}

}
