package ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sun.misc.BASE64Encoder;

//import sun.misc.BASE64Encoder;
public class BASE64 {
	/**
	 * 将本地图片进行Base64位编码
	 *
	 * @param imgUrl
	 * 图片的url路径，如e:\\123.png
	 * @return
	 */
	public static String encodeImgageToBase64(File imageFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		// 其进行Base64编码处理
		byte[] data = null;
		// 读取图片字节数组
		try {
			InputStream in = new FileInputStream(imageFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}


	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			//用java JDK自带的URL去请求
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			//设置该请求的消息头
			//设置HTTP方法：POST
			connection.setRequestMethod("POST");
			//设置其Header的Content-Type参数为application/x-www-form-urlencoded
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// 填入apikey到HTTP header
			connection.setRequestProperty("apikey", "uml8HFzu2hFd8iEG2LkQGMxm");
			//将第二步获取到的token填入到HTTP header
			connection.setRequestProperty("access_token", baiduOcr.getAuth());
			connection.setDoOutput(true);
			connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}