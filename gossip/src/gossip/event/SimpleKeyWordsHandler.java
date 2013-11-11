package gossip.event;

import edu.bit.dlde.utils.DLDEConfiguration;
import gossip.Handler;
import gossip.model.Event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.MMapDirectory;

/**
 * 
 * 一个空的KeyWords handler，给event添加KeyWords 建议以后使用共现词作为keyword
 * 
 * @author lins 2012-8-18
 */
public class SimpleKeyWordsHandler implements Handler {
	static HashSet<String> stopwords = new HashSet<String>();
	String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");

	public SimpleKeyWordsHandler() {
		// 初始化停词表
		if (stopwords.size() == 0) {
			BufferedReader br =null;
			try {
				 br= new BufferedReader(new FileReader(new File(
						"src/main/resources/stopwords")));
				stopwords.add(br.readLine());
				br.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void handle(Collection<Event> events) {
		// 打开索引
		MMapDirectory dir = null;
		IndexReader ir = null;
		IndexSearcher is = null;
		try {
			dir = new MMapDirectory(new File(indexPath));
			ir = IndexReader.open(dir, true);
			is = new IndexSearcher(ir);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		Iterator<Event> it = events.iterator();
		while (it.hasNext()) {
			Event e = it.next();
			HashMap<String, Integer> keywords=new HashMap<String, Integer>();
			//统计词
			for(int i: e.getPages()){
				TermFreqVector tfv;
				try {
					
					TermQuery query = new TermQuery(new Term("id",i+""));
					TopDocs tdocs = is.search(query, 1);
					ScoreDoc[] sdocs = tdocs.scoreDocs;
					int docId = 0;
					for(ScoreDoc sdoc : sdocs){
						docId = sdoc.doc;
					}
					tfv = ir.getTermFreqVector(docId, "title");
					for(int j = 0; j < tfv.size()&&j<5;j++){
						if(keywords.containsKey(tfv.getTerms()[j])){
							keywords.put(tfv.getTerms()[j], keywords.get(tfv.getTerms()[j])+tfv.getTermFrequencies()[j]);
						}else{
							keywords.put(tfv.getTerms()[j], tfv.getTermFrequencies()[j]);
						}
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			//添加关键词
			Iterator<String> kwit = keywords.keySet().iterator();
			while(kwit.hasNext()){
				String kw = kwit.next();
				if(!stopwords.contains(kw)){
					e.addKeyWord(kw, (double)keywords.get(kw));
				}
			}
		}
		//关闭索引
		try {
			if (ir != null)
				ir.close();
			if (dir != null)
				dir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
