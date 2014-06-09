package com.omartech.gossip.tools;

import com.omartech.gossip.parse.ParseNewsFromWKS;

public class AutoParser {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ParseNewsFromWKS pfw = new ParseNewsFromWKS();
		pfw.parse();
	}
}
