package gossip.gossip.event;

import gossip.model.Event;
import gossip.model.News;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmotionHandler implements Handler {
	static Logger logger = LoggerFactory.getLogger(EmotionHandler.class);
	@Override
	public void handle(Event event) {
		List<News> newsList = event.getNewsList();
		float total = 0;
		for (News news : newsList) {
			float score = computeEmotion(news);
			news.setEmotion(score);
			total += score;
		}
		event.setEmotion(total);
		logger.info("event emotion score : {}", total);
	}

	private float computeEmotion(News news) {
		float score = 0;
		String title = news.getTitle();
		String body = news.getBody();
		float titleScore = compute(title);
		float bodyScore = compute(body);

		score = titleScore * 2 + bodyScore;

		return score;

	}

	public static Map<String, Float> posWords = new ConcurrentHashMap<>();
	public static Map<String, Float> negWords = new ConcurrentHashMap<>();
	static {
		InputStream posInputStream = EmotionHandler.class.getClassLoader()
				.getResourceAsStream("pos_sentiwords.txt");
		InputStream negInputStream = EmotionHandler.class.getClassLoader()
				.getResourceAsStream("neg_sentiwords.txt");
		try {
			List<String> posArray = IOUtils.readLines(posInputStream);
			List<String> negArray = IOUtils.readLines(negInputStream);
			for (String str : posArray) {
				String word = str.substring(0, str.indexOf(":"));
				float weight = Float.parseFloat(str.substring(
						str.indexOf(":") + 1, str.length()));
				posWords.put(word, weight);
			}
			for (String str : negArray) {
				String word = str.substring(0, str.indexOf(":"));
				float weight = Float.parseFloat(str.substring(
						str.indexOf(":") + 1, str.length()));
				negWords.put(word, weight);
			}
			logger.info("posWords.size : {}", posWords.size());
			logger.info("negWords.size : {}", negWords.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static float compute(String text) {
		List<Term> terms = ToAnalysis.parse(text);
		float score = 0;
		for (Term term : terms) {
			String word = term.getName();
			if (posWords.containsKey(word)) {
				float weight = posWords.get(word);
				score += weight;
			}
			if (negWords.containsKey(word)) {
				float weight = negWords.get(word);
				score -= weight;
			}
		}
		return score;
	}

	public static void main(String[] args) {
		String content = "包养了一个情妇牺牲了";
		float a = compute(content);
		System.out.println(a);
	}
}
