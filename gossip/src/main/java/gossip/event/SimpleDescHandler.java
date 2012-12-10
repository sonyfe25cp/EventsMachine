package gossip.event;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.MMapDirectory;

import edu.bit.dlde.utils.DLDEConfiguration;
import gossip.Handler;
import gossip.summary.GenerateSummary;

/**
 * 生成事件摘要
 * 
 * @author zhangchangmin
 *
 */
public class SimpleDescHandler implements Handler {
	
	String indexPath = DLDEConfiguration.getInstance("gossip.properties")
			.getValue("IndexPath");
	
	GenerateSummary gs=new GenerateSummary();
	
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
			if (ir != null){
				List<Integer> pages=e.getPages();
				String desc=gs.description(pages);
				e.setDesc(desc);
			}
			else{
				
			}
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
