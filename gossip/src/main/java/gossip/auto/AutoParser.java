package gossip.auto;

import gossip.parse.ParseNewsFromWKS;

public class AutoParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParseNewsFromWKS pfw = new ParseNewsFromWKS();
		pfw.parse();
	}
}
