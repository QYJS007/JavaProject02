package ocr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.aip.ocr.AipOcr;

public class OCRUtils {

	public static void main(String[] args) throws Exception {

		//byte[] fileByte = getFileByte();

	}

	//����APPID/AK/SK
	public static final String APP_ID = "15916515";
	public static final String API_KEY = "NnA1zs4neW1VzuWtvi2Ykn0c";
	public static final String SECRET_KEY = "aeGoBSGtoNlGquwVHj2jAHDXGGxx0HB2";

	public static void  getAipOcr() {
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

		//basicGeneral(client);
	}
	
	//�Ի�������ʻ֤���йؼ��ֶν���ʶ��

	public String drivingLicense(AipOcr client, byte[] file) {
	    // �����ѡ�������ýӿ�
	    HashMap<String, String> options = new HashMap<String, String>();
	    options.put("detect_direction", "true");
	    
	    
	    // ����Ϊ����·��
	   // String image = "test.jpg";
	   // JSONObject res = client.drivingLicense(image, options);
	   // System.out.println(res.toString(2));

	    // ����Ϊ����������
	    // byte[] file = readFile("test.jpg");
	    JSONObject  res = client.drivingLicense(file, options);
	    //System.out.println(res.toString(2));
	    return res.toString();
	}


	public static String idcard(AipOcr client,String idCardSide,byte[] file) throws Exception {// �����ѡ�������ýӿ�
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("detect_direction", "true");//��ѡֵ����:- true����⳯��	- false������⳯��
		options.put("detect_risk", "false");//�Ƿ������֤��������

		//String idCardSide = "back";
		//String idCardSide = "front";

		// ����Ϊ����·��
		/* String image = "C:\\Users\\Administrator\\Desktop\\1234.png";
	    JSONObject res = client.idcard(image, idCardSide, options);
	    System.out.println(res.toString(2));*/

		// ����Ϊ����������
		//byte[] file = readFile(new File ("C:\\Users\\Administrator\\Desktop\\1234.png"));
		JSONObject res = client.idcard(file, idCardSide, options);
		//System.out.println(res.toString(2));
		return res.toString();
	}
	
	public String basicAccurateGeneral(AipOcr client,byte[] file) {
	    // �����ѡ�������ýӿ�
	    HashMap<String, String> options = new HashMap<String, String>();
	    /*
	     * detect_direction	��	String	true
							false
							false	�Ƿ���ͼ����Ĭ�ϲ���⣬����false��������ָ����ͼ��������������ʱ����ת90/180/270�ȡ���ѡֵ����:- true����⳯��	- false������⳯��
	     */
	    options.put("detect_direction", "true");//��ѡֵ����:- true����⳯��	- false������⳯��
	    options.put("probability", "true"); // probability	��	String	true  false  �Ƿ񷵻�ʶ������ÿһ�е����Ŷ�
	    
	    
	    // ����Ϊ����·��
	    //String image = "test.jpg";
	    //JSONObject res = client.basicAccurateGeneral(image, options);
	   // System.out.println(res.toString(2));

	    // ����Ϊ����������
	   // byte[] file = readFile("test.jpg");
	    JSONObject res = client.basicAccurateGeneral(file, options);
	   // System.out.println(res.toString(2));
	    
	    return res.toString();
	}


	public static String basicGeneral(AipOcr client ) throws Exception {
		// ���ýӿ�
		//String path = "C:\\Users\\Administrator\\Desktop\\12343.jpg";
		//JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
		//System.out.println(res.toString());

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

		    // ͨ������ʶ��, ͼƬ����ΪԶ��urlͼƬ
		    JSONObject res = client.basicGeneralUrl(url, options);
		    System.out.println(res.toString(2));*/

		// ����Ϊ����������
		byte[] file = readFile(new File ("C:\\Users\\Administrator\\Desktop\\1234.png"));
		JSONObject res = client.basicGeneral(file, options);
		//System.out.println(res.toString());
		return res.toString();
	}






	public static byte[] readFile(File file) throws Exception {
		try {
			//File file = new File("/Users/mac/Pictures/test.JPEG");
			byte[] fileByte = Files.readAllBytes(file.toPath());
			return fileByte;
		} catch (IOException e) {
			throw  new Exception(e);
		}
	}
}
