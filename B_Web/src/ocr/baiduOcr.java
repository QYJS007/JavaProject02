package ocr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;


/**
 * �ٶ�����ʶ��demo
 */
public class baiduOcr {
	/**
	 * ��ȡȨ��token
	 * @return ����ʾ����
	 * {
	 * "access_token": "24.c9303e47f0729c40f2bc2be6f8f3d589.2592000.1530936208.282335-
		1234567",
	 * "expires_in":2592000
	 * }
	 */
	
	public static String getAuth() {
		// ������ȡ�� API Key
		String clientId = "NnA1zs4neW1VzuWtvi2Ykn0c";
		// ������ȡ�� Secret Key
		String clientSecret = "aeGoBSGtoNlGquwVHj2jAHDXGGxx0HB2";
		return getAuth(clientId, clientSecret);
	}
	
	
	
	/**
	 * ��ȡAPI����token
	 * ��token��һ������Ч�ڣ���Ҫ���й�����ʧЧʱ�����»�ȡ.
	 * @param ak - �ٶ��Ƶ� API Key
	 * @param sk - �ٶ��Ƶ� Securet Key
	 * @return assess_token ʾ����
	 * "24.c9303e47f0729c40f2bc2be6f8f3d589.2592000.1530936208.282335-1234567"
	 */
	@SuppressWarnings("unused")
	public static String getAuth(String ak, String sk) {
		// ��ȡtoken��ַ
		String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
		String getAccessTokenUrl = authHost
				// 1. grant_typeΪ�̶�����
				+ "grant_type=client_credentials"
				// 2. ������ȡ�� API Key
				+ "&client_id=" + ak
				// 3. ������ȡ�� Secret Key
				+ "&client_secret=" + sk;
		try {
			URL realUrl = new URL(getAccessTokenUrl);
			// �򿪺�URL֮�������
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setRequestMethod("POST");//�ٶ��Ƽ�ʹ��POST����
			connection.connect();
			// ��ȡ������Ӧͷ�ֶ�
			Map<String, List<String>> map = connection.getHeaderFields();
			// ���� BufferedReader����������ȡURL����Ӧ
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String result = "";
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
			System.err.println("result:" + result);
			JSONObject jsonObject = new JSONObject(result);
			String access_token = jsonObject.getString("access_token");
			return access_token;
		} catch (Exception e) {
			System.err.printf("��ȡtokenʧ�ܣ�");
			e.printStackTrace(System.err);
		}
		return null;
	}
}