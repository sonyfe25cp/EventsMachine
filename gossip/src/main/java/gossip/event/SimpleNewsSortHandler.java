package gossip.event;

import edu.bit.dlde.utils.DLDEConfiguration;
import gossip.Handler;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.MMapDirectory;

/**
 * 
 * @author lins
 * @date 2012-9-5
 **/
public class SimpleNewsSortHandler implements Handler {
	String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");

	public void handle(Collection<Event> events) {
		// 打开索引
		MMapDirectory dir = null;
		IndexReader ir = null;
		try {
			dir = new MMapDirectory(new File(indexPath));
			ir = IndexReader.open(dir, true);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		//对每一个event的pages根据时间进行排序
		Iterator<Event> it = events.iterator();
		while (it.hasNext() && ir != null) {
			Event event = it.next();
			List<Integer> ids = event.getPages();
			Document[] docs = new Document[ids.size()];
			try {
				for (int i = 0; i < ids.size(); i++) {
					Document doc = ir.document(ids.get(i));
					if (doc != null)
						docs[i] = doc;
				}
				// 排序
				for(int i =0 ;i < docs.length ;i++){
					for(int j = i+1; j <docs.length; j++){
						if(Integer.parseInt(docs[i].get("crawlat"))<Integer.parseInt(docs[j].get("crawlat"))){
							Document tmp = docs[i];
							docs[i] = docs[j];
							docs[j] = tmp;
						}
					}
				}
			} catch (CorruptIndexException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			if(ir!=null)
				ir.close();
			if(dir!=null)
				dir.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
