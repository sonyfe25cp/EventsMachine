package gossip.server.test;

import gossip.model.News;
import gossip.server.service.local.NewsService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

public class TestComputeWordWeight {

	public static void main(String[] args) {
		NewsService service = new NewsService();
		List<News> news = service.findALL();
		List<String> bag = new ArrayList<>();
		for (News n : news) {
			String content = n.getBody();
			List<Term> terms = ToAnalysis.parse(content);
			Set<String> set = new HashSet<>();
			for (Term term : terms) {
				set.add(term.getName());
			}
			bag.addAll(set);
		}

		Map<String, Integer> count = new HashMap<>();
		for (String word : bag) {
			Integer c = count.get(word);
			if (c == null) {
				c = 0;
			}
			c++;
			count.put(word, c);
		}
		try {
			FileWriter fw = new FileWriter(new File("words-weight.txt"));
			for (Entry<String, Integer> entry : count.entrySet()) {
				System.out.println(entry.getKey() + " -- " + entry.getValue());

				String word = entry.getKey();
				int num = entry.getValue();
				double idf = Math.log((double)bag.size()/num) ;
				if(word.length() > 1){
					fw.write(word +" " +  idf+"\n");
				}
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
