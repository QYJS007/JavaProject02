package conf;

import play.i18n.Messages;

import java.util.ResourceBundle;


public class ResourceConfig {
	private static ResourceBundle rb = ResourceBundle.getBundle("resource");
	public static String getValue(String key) {
		return (rb.getString(key));
	}
	
	
	public static String getValue(String key, Object... args) {
		String value=rb.getString(key);
		return 	Messages.formatString(value,args);
	}
}
