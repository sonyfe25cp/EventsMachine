package gossip.event;

import edu.bit.dlde.utils.DLDEConfiguration;
import gossip.Handler;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.MMapDirectory;

/**
 * 一个空的Title handler，给event添加title 建议以后的实现可以直接使用相似最多文档的标题作为title
 * 
 * @author lins 2012-8-18
 */
public class SimpleTitleHandler implements Handler {
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

		Iterator<Event> it = events.iterator();
		while (it.hasNext()) {
			Event e = it.next();
			if (ir != null)
				try {
					int docId = e.getPages().get(0);// 使用第一文章的标题作为标题
					Document doc = ir.document(docId);
					if (doc != null)
						e.setTitle(doc.get("title"));
					else
						e.setTitle("无标题");
				} catch (CorruptIndexException e1) {
					e1.printStackTrace();
					e.setTitle("无标题");
				} catch (IOException e1) {
					e1.printStackTrace();
					e.setTitle("无标题");
				}
			else
				e.setTitle("无标题");
		}
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
