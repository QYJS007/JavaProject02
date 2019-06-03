package cg.string.ssss;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class s {
	public static void main(String[] args) throws UnsupportedEncodingException {

		byte[] bytes = "1213".getBytes("UTF-8");
		byte val = 1;
//		System.out.println();
		for (int i = 0; i < bytes.length; i++) {
			System.out.println(bytes[i]);
		}
		
	}
}
