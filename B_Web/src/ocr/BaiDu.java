package ocr;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONObject;


import com.baidu.aip.ocr.AipOcr;

public class BaiDu {
	//����APPID/AK/SK
	public static final String APP_ID = "15916515";
	public static final String API_KEY = "NnA1zs4neW1VzuWtvi2Ykn0c";
	public static final String SECRET_KEY = "aeGoBSGtoNlGquwVHj2jAHDXGGxx0HB2";

	public static void main(String[] args) {
		// ��ʼ��һ��AipOcr
		/*
		 * AipOcr��Optical Character Recognition��Java�ͻ��ˣ�
		 * 	Ϊʹ��Optical Character Recognition�Ŀ�����Ա�ṩ��һϵ�еĽ���������
		 * �û����Բο����´����½�һ��AipOcr,��ʼ����ɺ��鵥��ʹ��,�����ظ���ȡaccess_token��
		 */
		AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

		// ��ѡ�������������Ӳ���
		client.setConnectionTimeoutInMillis(2000);
		client.setSocketTimeoutInMillis(60000);

		// ��ѡ�����ô����������ַ, http��socket��ѡһ�����߾�������
		//client.setHttpProxy("proxy_host", proxy_port);  // ����http��s��
		//client.setSocketProxy("proxy_host", proxy_port);  // ����socket����

		// ��ѡ������log4j��־�����ʽ���������ã���ʹ��Ĭ������
		// Ҳ����ֱ��ͨ��jvm�����������ô˻�������
		//System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

		// ���ýӿ�
		String path = "C:\\Users\\Administrator\\Desktop\\12343.jpg";
		JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
		System.out.println(res.toString());

		// �����ѡ�������ýӿ�
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("language_type", "CHN_ENG");
		options.put("detect_direction", "true");
		options.put("detect_language", "true");
		options.put("probability", "true");


		// ����Ϊ����·��
		/* String image = "test.jpg";
	    JSONObject res = client.basicGeneral(image, options);
	    System.out.println(res.toString(2));

	    // ����Ϊ����������
	    byte[] file = readFile("test.jpg");
	    res = client.basicGeneral(file, options);
	    System.out.println(res.toString(2));

	    // ͨ������ʶ��, ͼƬ����ΪԶ��urlͼƬ
	    JSONObject res = client.basicGeneralUrl(url, options);
	    System.out.println(res.toString(2));*/


		sample( client);


	}
	public static void sample(AipOcr client) {// �����ѡ�������ýӿ�
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "true");
		options.put("detect_risk", "false");

		//	    String idCardSide = "back";
		String idCardSide = "front";

		// ����Ϊ����·��
		/* String image = "C:\\Users\\Administrator\\Desktop\\1234.png";
	    JSONObject res = client.idcard(image, idCardSide, options);
	    System.out.println(res.toString(2));*/

		// ����Ϊ����������
		byte[] file = readFile(new File ("C:\\Users\\Administrator\\Desktop\\1234.png"));
		JSONObject res = client.idcard(file, idCardSide, options);
		System.out.println(res.toString(2));
	}

	public static byte[] readFile(File imageFile) {
		// ��ͼƬ�ļ�ת��Ϊ�ֽ������ַ��������������Base64���봦��
		// �����Base64���봦��
		byte[] data = null;
		// ��ȡͼƬ�ֽ�����
		try {
			InputStream in = new FileInputStream(imageFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ���ֽ�����Base64����
		//BASE64Encoder encoder = new BASE64Encoder();
		//return encoder.encode(data);// ����Base64��������ֽ������ַ���.
		// byte[] file = readFile("test.jpg");
		return data;
	}
}
