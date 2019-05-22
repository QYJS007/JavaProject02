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
	 * ������ͼƬ����Base64λ����
	 *
	 * @param imgUrl
	 * ͼƬ��url·������e:\\123.png
	 * @return
	 */
	public static String encodeImgageToBase64(File imageFile) {
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
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// ����Base64��������ֽ������ַ���
	}


	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			//��java JDK�Դ���URLȥ����
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			//���ø��������Ϣͷ
			//����HTTP������POST
			connection.setRequestMethod("POST");
			//������Header��Content-Type����Ϊapplication/x-www-form-urlencoded
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// ����apikey��HTTP header
			connection.setRequestProperty("apikey", "uml8HFzu2hFd8iEG2LkQGMxm");
			//���ڶ�����ȡ����token���뵽HTTP header
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