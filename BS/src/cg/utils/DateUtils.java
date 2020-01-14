package cg.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	//private static org.slf4j.Logger log = LoggerFactory.getLogger(DateUtils.class);
	public final static String yyyy_MM_dd = "yyyy-MM-dd";
	public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public final static String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public final static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public final static String HH_mm="HH:mm";

	/**
	 * @描述：将字符串按指定格式转换成日期,格式为:yyyy-MM-dd
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param str 要转换的字符串
	 * @return 转换成的日期
	 */
	public static Date strToDate(String str) {

		SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * @描述：将字符串按指定格式转换成日期,格式为:yyyy-MM-dd HH:mm:ss.SSS
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param str 要转换的字符串
	 * @return 转换成的日期
	 */
	public static Date strToDateTime(String str) {

		SimpleDateFormat format = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss_SSS);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
		}
		System.out.println("sdfsfds");
		return date;
	}


	/**
	 * 字符串转换为日期
	 * @param str
	 * @param parsePatterns
	 * @return
	 * @throws ParseException
	 */
	public static  Date parseDate(String str) throws ParseException {
		// 支持的日期格式
		String[] formatStrs = { "yyyyMMddHHmmss", "yyyy-MM-dd-HH-mm-ss", "yyyyMMddHHmm", "yyyyMMddHH", "yyyyMMdd" };
		//空字符串检验
		if (str == null) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}
		// 创建format 对象 
		SimpleDateFormat parser = new SimpleDateFormat();
		//setLenient用于设置Calendar是否宽松解析字符串，如果为false，则严格解析；默认为true，宽松解析
		parser.setLenient(true);
		/*创建一个具有给定初始索引的新 ParsePosition。ParsePosition 是 Format 及其子类所使用的简单类，用来在解析过程中跟踪当前位置。
		  各种 Format 类中的 parseObject 方法要求将 ParsePosition 对象作为一个变量。 解析具有不同格式的字符串时，可以使用同一个 ParsePosition，因为索引参数记录的是当前位置。*/
		ParsePosition pos = new ParsePosition(0);
		//遍历数据  parsePatterns
		for (int i = 0; i < formatStrs.length; i++) {
			String pattern = formatStrs[i];
			parser.applyPattern(pattern);
			// 设置当前解析位置
			pos.setIndex(0);
			String str2 = str;
			Date date = parser.parse(str2, pos);
			if ((date != null) && (pos.getIndex() == str2.length())) {
				return date;
			}
		}
		throw new ParseException("Unable to parse the date: " + str, -1);
	}

}
