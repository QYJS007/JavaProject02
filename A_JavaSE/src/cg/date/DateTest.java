package cg.date;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTest {
	public static void main(String[] args) {

		Date departdate = null;
		//String departdatestr=formatDate(departdate );
		String departdatestr=formatDate(departdate );
		System.out.println(departdatestr);
	}

	/**
	 * @描述：将日期转换为字符串,格式为:yyyy-MM-dd
	 * @开发人员：
	 * @开发时间：2015年7月24日 上午08:00:00
	 * @param date 要格式化的日期
	 * @return 格式化字符串
	 */
	public static String formatDate(Date date) {
		String yyyy_MM_dd ="";
		SimpleDateFormat sf = new SimpleDateFormat(yyyy_MM_dd );
		return sf.format(date);
	}
}
