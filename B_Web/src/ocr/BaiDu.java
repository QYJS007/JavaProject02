package ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONObject;


import com.baidu.aip.ocr.AipOcr;

public class BaiDu {
	//设置APPID/AK/SK
	public static final String APP_ID = "15916515";
	public static final String API_KEY = "NnA1zs4neW1VzuWtvi2Ykn0c";
	public static final String SECRET_KEY = "aeGoBSGtoNlGquwVHj2jAHDXGGxx0HB2";

	public static void main(String[] args) {
		// 初始化一个AipOcr
		/*
		 * AipOcr是Optical Character Recognition的Java客户端，
		 * 	为使用Optical Character Recognition的开发人员提供了一系列的交互方法。
		 * 用户可以参考如下代码新建一个AipOcr,初始化完成后建议单例使用,避免重复获取access_token：
		 */
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

		// 可选：设置网络连接参数
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		// 可选：设置代理服务器地址, http和socket二选一，或者均不设置
		//client.setHttpProxy("proxy_host", proxy_port);  // 设置http代s理
		//client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

		// 可选：设置log4j日志输出格式，若不设置，则使用默认配置
		// 也可以直接通过jvm启动参数设置此环境变量
		//System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

		// 调用接口
		String path = "C:\\Users\\Administrator\\Desktop\\12343.jpg";
		JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
		System.out.println(res.toString());

		// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("language_type", "CHN_ENG");
		options.put("detect_direction", "true");
		options.put("detect_language", "true");
		options.put("probability", "true");


		// 参数为本地路径
		/* String image = "test.jpg";
	    JSONObject res = client.basicGeneral(image, options);
	    System.out.println(res.toString(2));

	    // 参数为二进制数组
	    byte[] file = readFile("test.jpg");
	    res = client.basicGeneral(file, options);
	    System.out.println(res.toString(2));

	    // 通用文字识别, 图片参数为远程url图片
	    JSONObject res = client.basicGeneralUrl(url, options);
	    System.out.println(res.toString(2));*/


		sample( client);


	}
	public static void sample(AipOcr client) {// 传入可选参数调用接口
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "true");
		options.put("detect_risk", "false");

		//	    String idCardSide = "back";
		String idCardSide = "front";

		// 参数为本地路径
		/* String image = "C:\\Users\\Administrator\\Desktop\\1234.png";
	    JSONObject res = client.idcard(image, idCardSide, options);
	    System.out.println(res.toString(2));*/

		// 参数为二进制数组
		byte[] file = readFile(new File ("C:\\Users\\Administrator\\Desktop\\1234.png"));
		JSONObject res = client.idcard(file, idCardSide, options);
		System.out.println(res.toString(2));
	}

	public static byte[] readFile(File imageFile) {
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
		//BASE64Encoder encoder = new BASE64Encoder();
		//return encoder.encode(data);// 返回Base64编码过的字节数组字符串.
		// byte[] file = readFile("test.jpg");
		return data;
	}
}
