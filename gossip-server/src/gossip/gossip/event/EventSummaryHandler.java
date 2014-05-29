package gossip.gossip.event;

import gossip.gossip.segment.SegSentences;
import gossip.model.Event;
import gossip.model.News;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.IOUtils;

public class EventSummaryHandler implements Handler {

	@Override
	public void handle(Event event) {
		List<News> news = event.getNewsList();
		List<Entry<String, Double>> important = new ArrayList<>();
		for (News n : news) {
			List<Entry<String, Double>> summary = findImportantSentences(n);
			important.addAll(summary);
		}
		sort(important);

//		StringBuilder sb = new StringBuilder();
//		int i = 0;
//		for (Entry<String, Double> entry : important) {
//			if (i < 3 && i < important.size()) {
//				sb.append(entry.getKey() + "\n");
//			}
//		}
//		System.err.println("摘要： "+ sb.toString());
		event.setDesc(important.get(0).getKey());
	}

	public List<Entry<String, Double>> findImportantSentences(News news) {

		String content = news.getBody();
		SegSentences segSentences = new SegSentences();
		List<String> sentences = segSentences.getSentence(content);
		Map<String, Double> scoreBoard = new HashMap<>();
		for (String sentence : sentences) {
			double score = computeImportance(sentence);
			scoreBoard.put(sentence, score);
		}

		List<Entry<String, Double>> sentenceArray = new ArrayList<>(
				scoreBoard.entrySet());
		sort(sentenceArray);

		if (sentenceArray.size() > 3) {
			return sentenceArray.subList(0, 2);
		} else {
			return sentenceArray;
		}
	}

	private void sort(List<Entry<String, Double>> sentenceArray) {
		Collections.sort(sentenceArray,
				new Comparator<Entry<String, Double>>() {
					@Override
					public int compare(Entry<String, Double> o1,
							Entry<String, Double> o2) {
						double res = o1.getValue() - o2.getValue();
						if (res > 0) {
							return -1;
						} else if (res == 0) {
							return 0;
						} else {
							return 1;
						}
					}
				});
	}

	static Map<String, Double> wordsWeight = new HashMap<>();
	static {
		InputStream is = EventSummaryHandler.class.getClassLoader()
				.getResourceAsStream("words-weight.txt");
		try {
			List<String> lines = IOUtils.readLines(is);
			for (String line : lines) {
				String key = line.substring(0, line.indexOf(" "));
				String value = line.substring(line.indexOf(" ") + 1,
						line.length());
				double weight = Double.parseDouble(value);
				wordsWeight.put(key, weight);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private double computeImportance(String sentence) {
		List<Term> terms = ToAnalysis.parse(sentence);
		double total = 1;
		for (Term term : terms) {
			String word = term.getName();
			Double weightO = wordsWeight.get(word);
			double weight = 0;
			if (weightO == null) {
				weight = 1;
			} else {
				weight = weightO.doubleValue();
			}
			total += weight;
		}
		return total;
	}

}
