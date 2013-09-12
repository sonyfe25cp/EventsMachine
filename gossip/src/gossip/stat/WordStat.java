package gossip.stat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.bit.dlde.utils.DLDEConfiguration;

public class WordStat {

	/**
	 * @param args
	 * @throws IOException
	 */

	public static Map<String, Integer> countFrequency(String indexPath) throws IOException {
		Map<String, Integer> termFreqAll = new HashMap<String, Integer>();
		System.out.println("Reading index...");
		Directory dir1 = FSDirectory.open(new File(indexPath));
		IndexReader trainingSetReader = IndexReader.open(dir1);
		for (int i = 0; i < trainingSetReader.numDocs(); i++) {
			String title = null;
			if (trainingSetReader.document(i).getField("title") != null)
				title = trainingSetReader.document(i).getField("title")
						.stringValue();
			// System.out.println("title="+title);
			TermFreqVector trainingSetTermVector = trainingSetReader
					.getTermFreqVector(i, "body");
			if (trainingSetTermVector != null) {
				// System.out.println("trainingSetTermVector.size="+trainingSetTermVector.size());
				String[] termSet = trainingSetTermVector.getTerms();
				int[] termFreqSet = trainingSetTermVector.getTermFrequencies();
				// System.out.println("termSet[0]="+termSet[0]+" freq="+termFreqSet[0]);
				for (int j = 0; j < termSet.length; j++) {
					if (termFreqAll.get(termSet[j]) != null)
						termFreqAll.put(termSet[j], termFreqAll.get(termSet[j])
								+ termFreqSet[j]);
					else
						termFreqAll.put(termSet[j], termFreqSet[j]);
				}
			} else {
				System.out.println("title=" + title);
				System.out.println("body=null");
			}
		}
		trainingSetReader.close();
		dir1.close();
		return termFreqAll;
	}

	public static void sortAndOutput(Map<String, Integer> termFreqAll,
			String outputFile) throws IOException {
		// sort
		List<Entry<String, Integer>> entryList = new LinkedList<Entry<String, Integer>>(
				termFreqAll.entrySet());
		Collections.sort(entryList, new Comparator<Entry<String, Integer>>() {

			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				return -(o1.getValue().compareTo(o2.getValue()));
			}
		});
		// write
		File f = new File(outputFile);
		f.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(f));
		for (int i = 0; i < entryList.size(); i++) {
			writer.write(entryList.get(i).getKey() + " "+ entryList.get(i).getValue());
			writer.write("\n");
		}
		writer.close();
	}

	public static void main(String[] args) throws IOException {
		
		String indexPath = DLDEConfiguration.getInstance("gossip.properties").getValue("IndexPath");
		
		sortAndOutput(countFrequency(indexPath),"/tmp/words.count");
		
		System.out.println("counts over~");
	}

}
