package ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONObject;


public class F {
	public static void main(String[] args) {
		//��ȡ���صľ���·��ͼƬ
		File file = new File("C:\\Users\\Administrator\\Desktop\\1234.png");
		//����BASE64λ����
		String imageBase = BASE64.encodeImgageToBase64(file);
		imageBase = imageBase.replaceAll("\r\n","");
		imageBase = imageBase.replaceAll("\\+","%2B");
		long t1 = System.currentTimeMillis();
		//System.out.println(System.currentTimeMillis());
		
		//�ٶ��Ƶ�����ʶ��ӿ�,�������Ϊ��ȡ����token
		String httpUrl=" https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token="+baiduOcr.getAuth();
		String httpArg = "detect_direction=true&id_card_side=front&image="+imageBase;
		String jsonResult = request(httpUrl, httpArg);
		System.out.println("���صĽ��--------->"+jsonResult);
		HashMap<String, String> map = getHashMapByJson(jsonResult);
		Collection<String> values=map.values();
		Iterator<String> iterator2=values.iterator();
		while (iterator2.hasNext()){
			System.out.print(iterator2.next()+", ");
		}
		//Set<String> keySet = map.keySet();
		
		Set<Entry<String,String>> entrySet = map.entrySet();
		for (Entry<String, String> entry : entrySet) {
			entry.getKey();
		}
		
		
		long t2 = System.currentTimeMillis();
		System.out.println("��ʱ��"+(t2-t1)/1000+ "��");
	}

	public static String request(String httpUrl, String httpArg) {
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();
		try {
			//��java JDK�Դ���URLȥ����
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//���ø��������Ϣͷ
			//����HTTP������POST
			connection.setRequestMethod("POST");
			//������Header��Content-Type����Ϊapplication/x-www-form-urlencoded
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			// ����apikey��HTTP header
			connection.setRequestProperty("apikey", "uml8HFzu2hFd8iEG2LkQGMxm");
			//���ڶ�����ȡ����token���뵽HTTP header
			connection.setRequestProperty("access_token", baiduOcr.getAuth());
			connection.setDoOutput(true);
			connection.getOutputStream().write(httpArg.getBytes("UTF-8"));
			connection.connect();
			java.io.InputStream is = connection.getInputStream();
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
	
	
	
	public static HashMap<String,String> getHashMapByJson(String jsonResult){
		HashMap<String, String> map = new HashMap<String,String>();
		try {
			JSONObject jsonObject = new JSONObject(jsonResult);
			JSONObject words_result= jsonObject.getJSONObject("words_result");
			Iterator<String> it = words_result.keys();
			while (it.hasNext()){
				String key = it.next();
				JSONObject result = words_result.getJSONObject(key);
				String value=result.getString("words");
				switch (key){
				case "����":
					map.put("name",value);
					break;
				case "����":
					map.put("nation",value);
					break;
				case "סַ":
					map.put("address",value);
					break;
				case "������ݺ���":
					map.put("IDCard",value);
					break;
				case "����":
					map.put("Birth",value);
					break;
				case "�Ա�":
					map.put("sex",value);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
