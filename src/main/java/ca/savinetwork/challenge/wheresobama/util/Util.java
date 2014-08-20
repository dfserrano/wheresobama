package ca.savinetwork.challenge.wheresobama.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public static String getRandomString(int minLength, int maxLength) {
		return Util.getRandomString(random.nextInt(minLength)
				+ (maxLength - minLength));
	}
	
	public static byte[] fromFileToBytes(String filePath) {
		File file = new File(filePath);

		byte[] bytes = new byte[(int) file.length()];

		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			fis.read(bytes);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes;
	}
	
	public static void fromBytesToFile(byte[] bytes, String filePath) {
		FileOutputStream stream = null;
		
		try {
			stream = new FileOutputStream(filePath);
			stream.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
