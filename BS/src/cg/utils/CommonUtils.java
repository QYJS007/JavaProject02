package cg.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import cg.project.CopyUpdateFiles;

public class CommonUtils {
	public static void main(String[] args) {
		Date editDate = new Date();

		// 支持的日期格式
		String[] formatStrs = { "yyyyMMddHHmmss", "yyyy-MM-dd-HH-mm-ss", "yyyyMMddHHmm", "yyyyMMddHH", "yyyyMMdd" };
		// START_DATE_STR
		String START_DATE_STR = "2018-09-26-18-10-23"; //时间节点（获取该日期后修改的所有代码） 
		System.out.println("START_DATE_STR: "+START_DATE_STR);
		try {
			editDate = parseDate(START_DATE_STR, formatStrs);
			System.out.println("editDate: "+editDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 字符串转换为日期
	 * @param str
	 * @param parsePatterns
	 * @return
	 * @throws ParseException
	 */
	private static Date parseDate(String str, String[] parsePatterns) throws ParseException {
		if ((str == null) || (parsePatterns == null)) {
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}
		SimpleDateFormat parser = new SimpleDateFormat();
		parser.setLenient(true);
		/*
		 * 指定日期/时间解析是否不严格。进行不严格解析时，解析程序可以使用启发式的方法来解释与此对象的格式不精确匹配的输入。进行严格解析时，输入必须匹配此对象的格式。
		 */
		
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++) {
			String pattern = parsePatterns[i];

			if (parsePatterns[i].endsWith("ZZ")) {
				pattern = pattern.substring(0, pattern.length() - 1);
			}

			parser.applyPattern(pattern);
			pos.setIndex(0);

			String str2 = str;

			if (parsePatterns[i].endsWith("ZZ")) {
				int signIdx = indexOfSignChars(str2, 0);
				while (signIdx >= 0) {
					str2 = reformatTimezone(str2, signIdx);
					signIdx = indexOfSignChars(str2, ++signIdx);
				}
			}
/*
 * 	 此方法试图解析从 pos 给定的索引处开始的文本。
 * 		如果解析成功，则将 pos 的索引更新为所用最后一个字符后面的索引（不必对直到字符串结尾的所有字符进行解析），
 * 并返回解析得到的日期。
 * 更新后的 pos 可以用来指示下次调用此方法的起始点。如果发生错误，则不更改 pos 的索引，并将 pos 的错误索引设置为发生错误处的字符索引，并且返回 null。
 */
			Date date = parser.parse(str2, pos);
			if ((date != null) && (pos.getIndex() == str2.length())) {
				return date;
			}
		}
		throw new ParseException("Unable to parse the date: " + str, -1);
	}

	private static int indexOfSignChars(String str, int startPos) {
		int idx = str.indexOf('+', startPos);
		if (idx < 0) {
			idx = str.indexOf('-', startPos);
		}
		return idx;
	}

	private static String reformatTimezone(String str, int signIdx) {
		String str2 = str;
		if ((signIdx >= 0) && (signIdx + 5 < str.length()) && (Character.isDigit(str.charAt(signIdx + 1)))
				&& (Character.isDigit(str.charAt(signIdx + 2))) && (str.charAt(signIdx + 3) == ':')
				&& (Character.isDigit(str.charAt(signIdx + 4))) && (Character.isDigit(str.charAt(signIdx + 5)))) {
			str2 = str.substring(0, signIdx + 3) + str.substring(signIdx + 4);
		}
		return str2;
	}
}
