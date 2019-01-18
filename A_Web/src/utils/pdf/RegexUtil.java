package utils.pdf;

import java.util.regex.Pattern;

/**
 * @author dongchao
 * @version 创建时间：Nov 12, 2013 7:48:41 PM
 */
public class RegexUtil {
	
	//非汉字
	public static final Pattern letter = Pattern.compile("[\\w\\s\\.\\-:@]+");
}
