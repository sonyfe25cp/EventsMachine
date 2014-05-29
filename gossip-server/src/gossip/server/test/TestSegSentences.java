package gossip.server.test;

import gossip.gossip.segment.SegSentences;
import gossip.gossip.utils.Utils;

import java.util.List;


public class TestSegSentences {

	
	public static void main(String[] args) {
		String file = "news.txt";
		String content = Utils.getResouce(file);
		SegSentences segSentences = new SegSentences();
		List<String> sentence = segSentences.getSentence(content);
		for(String str : sentence){
			System.out.println(str);
		}
	}
	
}
