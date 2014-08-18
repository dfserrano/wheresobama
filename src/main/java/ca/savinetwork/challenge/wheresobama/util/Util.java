package ca.savinetwork.challenge.wheresobama.util;

import java.util.Random;

public class Util {

	private static final String letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static Random random = new Random(System.currentTimeMillis());
	
	public static String getRandomString(int len) {
		StringBuilder sb = new StringBuilder(len);

		for (int i = 0; i < len; i++) {
			sb.append(letters.charAt(random.nextInt(letters.length())));
		}

		return sb.toString();
	}
}
