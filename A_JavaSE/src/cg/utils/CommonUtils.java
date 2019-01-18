package cg.utils;

import java.io.UnsupportedEncodingException;

public class CommonUtils {
	public static void main(String[] args) throws UnsupportedEncodingException {
		// String  Str = "浣犲ソ";
		
		//String str = "你好";
		/*try {
			// 编码
			byte[] bs4  = str.getBytes("UTF-8");
			//通过使用指定的 charset 解码指定的 byte 数组，构造一个新的 String。
			System.out.println(new String(bs4, "GBK"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		
		String formatCharset = formatCharset("你好", "GBK","UTF-8");
		
	}
	
	/**字符串的转化;
	 * @param param
	 * @param encode
	 * @param decode
	 * @return
	 */
	public static String formatCharset(String param , String encode, String decode) {
		try {
			return new String("浣犲ソ".getBytes("GBK"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			
			return null;
		}
	}

}
