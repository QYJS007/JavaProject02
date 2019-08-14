package conf;

import play.i18n.Messages;

import java.util.ResourceBundle;

public class MessageConfig {
	private static ResourceBundle rb = ResourceBundle.getBundle("messages");
	public static String getValue(String key) {
		String value=rb.getString(key);
		if(value!=null&&value.indexOf("|")!=-1){
			value=value.substring(0,value.indexOf("|"));
		}
		return value;
	}
	
	/**
	 * 获得messages的全部信息,若对应信息不存在返回key
	 * @author liurupeng
	 * @param key
	 * @return
	 */
	public static String getValues(String key) {
		try {
			String value = rb.getString(key);
			return value;
		} catch (Exception e) {
			return key;
		}
	}
	
	public static String getValue(String key, Object... args) {
		String value=rb.getString(key);
		if(value!=null&&value.indexOf("|")!=-1){
			value=value.substring(0,value.indexOf("|"));
		}
		return 	Messages.formatString(value,args);
	}
}
