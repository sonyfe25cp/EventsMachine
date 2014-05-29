package gossip.gossip.segment;

import java.util.ArrayList;
import java.util.List;

public class SegSentences {
	public List<String> getSentence(String line) {
		line = line.trim();
		ArrayList<String> sentence = new ArrayList<String>();
		int length = line.length();
		int begin = 0;
		int state = 0;
		int tmps = 0;
		for (int i = 0; i < length; i++) {
			char c = line.charAt(i);
			switch (state) {
			case 0: {
				switch (c) {
				case '“':
					tmps = i;
					state = 1;
					break;
				case '。':
				case '！':
				case '？':
					state = 2;
					break;
				case '…':
					i++;
					state = 2;
					break;
				default:
					break;
				}
				break;
			}
			case 1: {
				switch (c) {
				case '。':
				case '！':
				case '？':
					state = 3;
					break;
				case '…':
					i++;
					state = 3;
					break;
				case '”':
					state = 0;
					break;

				default:
					break;
				}
				break;
			}
			case 2: {
				sentence.add(line.substring(begin, i));
				begin = i;
				state = 0;
				i--;
				break;
			}
			case 3: {
				switch (c) {
				case '”':
					state = 4;
					break;

				default:
					break;
				}
				break;
			}
			case 4: {
				if (tmps - 1 > begin && line.charAt(tmps - 1) != ' ') {
					// sentence.add(line.substring(begin, i));
					state = 0;
					// begin = i;
					break;
				} else {
					state = 0;
				}
				break;
			}

			default:
				break;
			}
		}
		if (begin != length) {
			sentence.add(line.substring(begin));
		}
		/*
		 * for (int j = 0; j < sentence.size(); j++) {
		 * System.out.println(sentence.get(j)); }
		 */
		
		return sentence;
	}
}
