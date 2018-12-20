package cg.time;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NovaDateUtil {
	/**
	 * 从字符串类型获取日期Date
	 * 
	 * @param dateStr
	 *            日期字符串，如：20080321
	 * @param format
	 *            格式化模式，如yyyymmdd 和dateStr保持一致的格式
	 * @return 返回一个Date类型日期
	 * @throws Exception
	 */
	public static Date getDateFromString(String dateStr, String format) {

		if (dateStr == null || format == null) {
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date;
		try {
			date = df.parse(dateStr);
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	public static java.sql.Date getSQLDateFromString(String dateStr,
			String format) {

		if (dateStr == null || format == null) {
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat(format);
		java.sql.Date date;
		try {
			date = new java.sql.Date(df.parse(dateStr).getTime());
			return date;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Calendar类型转换成Date类型日期
	 * 
	 * @param calendar
	 * @return
	 */
	public static Date getDateFromCalendar(Calendar calendar) {
		return calendar.getTime();
	}

	/**
	 * Timestamp类型转换成Date类型日期
	 * 
	 * @param timestamp
	 * @param format
	 * @return
	 */
	public static Date getDateFromTimestamp(Timestamp timestamp, String format) {
		return getDateFromString(timestamp.toString(), format);
	}

	public static Timestamp getTimestampFromString(String dateStr,
			String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Timestamp ts = null;
		try {
			ts = new Timestamp(sdf.parse(dateStr).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ts;
	}

	/**
	 * String类型转换成Timestamp类型日期
	 * 
	 * @param dateStr
	 *            日期字符串
	 * @return
	 * @throws Exception
	 */
	public static Timestamp getTimestampFromString(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
		// 出票格式 不优化 从新设置
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		// 2011.12.9
		return Timestamp.valueOf(sdf.format(getDateFromString(dateStr,
				"yyyyMMddHHmmss")));
		// return Timestamp.valueOf(sdf.format(getDateFromString(dateStr,
		// "yyyyMMdd")));
	}

	/**
	 * Calendar类型转换Timestamp类型的日期
	 * 
	 * @param calendar
	 *            参数是Calendar类型
	 * @return
	 */
	public static Timestamp getTimestampFromCalendar(Calendar calendar) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00.000");
		return Timestamp.valueOf(sdf.format(calendar.getTime()));
	}

	/**
	 * Date类型转换Timestamp类型的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp getTimestampFromDate(Date date) {
		try {
			return getTimestampFromString(getStringFromDate(date, "yyyy-MM-dd"));
		} catch (Exception e) {
			// //System.out.println("获取日期出错");
			return null;
		}
	}

	/**
	 * Date类型转换Timestamp类型的日期
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp getTimestampFromDate(Date date, String format) {
		try {
			return getTimestampFromString(getStringFromDate(date, format));
		} catch (Exception e) {
			// //System.out.println("获取日期出错");
			return null;
		}
	}

	/**
	 * String(字符串类型)转换成Calendar类型的日期
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Calendar getCalendarFromString(String dateStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDateFromString(dateStr, "yyyymmdd"));
		return calendar;
	}

	/**
	 * Date类型转换成Calendar类型日期
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getCalendarFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * Timestamp类型转换成Calendar类型日期
	 * 
	 * @param timestamp
	 * @return
	 */
	public static Calendar getCalendarFromTimestamp(Timestamp timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getDateFromTimestamp(timestamp, "yyyymmdd"));
		return calendar;
	}

	public static String format(Calendar c, String format) {
		Date date = null;
		if (c == null) {
			date = new Date();
		} else {
			date = c.getTime();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String formatHm(Calendar date) {
		return format(date, "HH:mm");
	}

	public static String formatHms(Calendar date) {
		return format(date, "HH:mm:ss");
	}

	public static String formatYMD(Calendar date) {
		return format(date, "yyyy-MM-dd");
	}

	public static String getStringValFromDate(Date date) {
		return getStringValFromDate(date, true);
	}

	public static String getStringValFromDate(Date date, boolean hasSpace) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		String flag = "上午";
		if (hour > 12) {
			hour -= 12;
			flag = "下午";
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		return formatYMD(c) + (hasSpace ? " " : "") + flag + formatHm(c);
	}

	public static String getStringValFromTime(Date date) {
		if (date == null) {
			date = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		String flag = "上午";
		if (hour > 12) {
			hour -= 12;
			flag = "下午";
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		return flag + formatHm(c);
	}

	/**
	 * Date类型转换成String(字符串)类型的日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringFromDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String getStringFromDate(java.sql.Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String getStringFromTime(Time time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	public static String formatTimestamp(Timestamp date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * Calendar类型转换成String类型的日期
	 * 
	 * @param celendar
	 * @return
	 */
	public static String getStringFromCalendar(Calendar celendar, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(celendar.getTime());
	}

	/**
	 * Timestamp类型转换成String类型的日期
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String getStringFromTimestamp(Timestamp timestamp) {
		return timestamp.toString();
	}

	/**
	 * 格式化format
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static String getStringFormat(String dateStr, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String result = "";

		try {
			result = sdf.format(dateStr);
		} catch (Exception e) {
			// System.out.println("出现错误");
		}

		return result;

	}

	/**
	 * 是否为某个月的最后一天
	 * 
	 * @param days
	 * @param month
	 * @param year
	 * @return
	 */
	public static boolean isLastDayOfMonth(int days, int month, int year) {
		month = month - 1;
		int monthCompare = month;
		Calendar calendar = Calendar.getInstance();
		// calendar.set(year, month, days);
		calendar.set(Calendar.DAY_OF_MONTH, days);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		// //System.out.println("月份："+(calendar.get(Calendar.MONTH)+1));
		// //System.out.println("几号："+calendar.get(Calendar.DAY_OF_MONTH));
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		// //System.out.println("月份："+(calendar.get(Calendar.MONTH)+1));
		// //System.out.println("几号："+calendar.get(Calendar.DAY_OF_MONTH));
		if (monthCompare != calendar.getInstance().MONTH) {
			return true;
		}
		return false;
	}

	//
	// /**
	// * @param args
	// * @throws ParseException
	// */
	// public static void main(String[] args) throws ParseException {
	// DateTool dateTool=new DateTool();
	// //测试是否为最后一天。。。
	// boolean b=dateTool.isLastDayOfMonth(29, 02, 2007);
	// if(b){s
	// //System.out.println("最后一天");
	// }else{
	// //System.out.println("不是最后一天");
	// }
	// Date date=new Date();
	// SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd");
	// sdf.format(date);
	//
	// Date date1=new Date();
	//
	// Date date2=new Date();
	//
	// //System.out.println(getTimestampFromDate(date1).toString());
	// }

	/**
	 * 获取当前的月份的几号
	 * 
	 * @return
	 */
	public static int getCurrentDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取当前的月份
	 * 
	 * @return
	 */
	public static int getCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH);
	}

	/**
	 * 获取当前的年
	 * 
	 * @return
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 获取当前的日期是一年中的第几天
	 * 
	 * @return
	 */
	public static int getCurrentDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取当前的日期在当前月中是第几周
	 * 
	 * @return
	 */
	public static int getCurrentWeekOfMonth() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取当前的日期在当前年里的第几周
	 * 
	 * @return
	 */
	public static int getCurrentWeekOfYear() {
		return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 获取指定calendar的月中的几号
	 * 
	 * @param calendar
	 * @return 返回月份值，是一个int型数据
	 */
	public static int getDayOfMonth(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获取指定calendar的月份
	 * 
	 * @param calendar
	 * @return 返回月份值，是一个int型数据
	 */
	public static int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取指定calendar的年
	 * 
	 * @param calendar
	 * @return 返回年值，是一个int型数据
	 */
	public static int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 获取指定calendar中年份中的第几天
	 * 
	 * @param calendar
	 * @return
	 */
	public static int getDayOfYear(Calendar calendar) {
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取指定calendar的月中的第几周
	 * 
	 * @param calendar
	 * @return
	 */
	public static int getWeekOfMonth(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * 获取指定calendar的年中的第几周
	 * 
	 * @param calendar
	 * @return
	 */
	public static int getWeekOfYear(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * 得到两个日期之间相差多少天
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static int dateDiff(String beginDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(endDate);
		} catch (ParseException e) {
			date = new Date();
			e.printStackTrace();
		}

		long end = date.getTime();
		try {
			date = sdf.parse(beginDate);
		} catch (ParseException e) {
			date = new Date();
			e.printStackTrace();
		}
		long begin = date.getTime();

		long day = (end - begin) / (1000 * 3600 * 24); // 除1000是把毫秒变成秒

		return Integer.parseInt(Long.toString(day));
	}

	/**
	 * 日期推迟
	 * 
	 * @param date
	 *            需要推迟的日期
	 * @param dayNumber
	 *            需要推迟的天数
	 * @return 返回新的日期
	 */
	public static Date dateAdd(Date date, int dayNumber) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, dayNumber);
		return calendar.getTime();
	}

	/**
	 * 得到某种类型的中间的所有日期.格式为"yyyy-MM-dd"
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static String[] getDaysByRang(String beginDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 得到两个日期间相差多少天
		int num = dateDiff(beginDate, endDate);
		if (num < 0) {
			// 颠倒一下日期
			String tmp = beginDate;
			beginDate = endDate;
			endDate = tmp;
			num = 0 - num;
		}

		String[] result = {};
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(beginDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		num = num + 1; // 把开始和结束日期都包含进去

		result = new String[num];
		for (int i = 0; i < num; i++) {
			if (i > 0)
				cal.add(Calendar.DAY_OF_YEAR, 1);
			result[i] = sdf.format(new Date(cal.getTimeInMillis()));
		}

		return result;
	}

	/**
	 * 得到某种类型的中间的所有日期.格式为"yyyy-MM-dd"
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static Date[] getDateDaysByRang(String beginDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		// 得到两个日期间相差多少天
		int num = dateDiff(beginDate, endDate);
		if (num < 0) {
			// 颠倒一下日期
			String tmp = beginDate;
			beginDate = endDate;
			endDate = tmp;
			num = 0 - num;
		}

		Date[] result = {};
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(beginDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		num = num + 1; // 把开始和结束日期都包含进去

		result = new Date[num];
		for (int i = 0; i < num; i++) {
			if (i > 0)
				cal.add(Calendar.DAY_OF_YEAR, 1);
			result[i] = new Date(cal.getTimeInMillis());
		}

		return result;
	}

	/**
	 * 得到最近num天的全部日期 说明: 1.日期是从昨天开始算的. 2.如果num=2 , 今天的日期是2008-03-14 ,则返回的结果为
	 * 2008-03-12、2008-03-13
	 * 
	 * @param num
	 * @return
	 */
	public static String[] getDaysByNum(int num) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] result = {};
		Calendar cal = Calendar.getInstance();

		// 最近一周
		result = new String[num];
		for (int i = num; i > 0; i--) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			result[i - 1] = sdf.format(new Date(cal.getTimeInMillis()));
		}

		return result;
	}

	/**
	 * 得到最近num天的全部日期 说明: 1.日期是从昨天开始算的. 2.如果num=2 , 今天的日期是2008-03-14 ,则返回的结果为
	 * 2008-03-12、2008-03-13
	 * 
	 * @param num
	 * @return
	 */
	public static String[] getDaysByNum(int num, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] result = {};
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(date, "yyyy-MM-dd"));

		// 最近一周
		result = new String[num];
		for (int i = num; i > 0; i--) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			result[i - 1] = sdf.format(new Date(cal.getTimeInMillis()));
		}

		return result;
	}

	/**
	 * 得到最近num天的全部日期 说明: 1.日期是从今天开始算的. 2.如果num=2 , 今天的日期是2008-03-14 ,则返回的结果为
	 * 2008-03-13、2008-03-14
	 * 
	 * @param num
	 * @return
	 */
	public static String[] getDaysByBefore(int num, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] result = {};
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(date, "yyyy-MM-dd"));
		cal.add(Calendar.DAY_OF_YEAR, 1);
		// 最近num的日期，包括date日期
		result = new String[num];
		for (int i = num; i > 0; i--) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
			result[i - 1] = sdf.format(new Date(cal.getTimeInMillis()));

		}

		return result;
	}

	/**
	 * 得到最近num天的全部日期 说明: 1.日期是从今天开始算的. 2.如果num=2 , 今天的日期是2012-05-24
	 * ,则返回的结果为2012-05-25,2012-05-26
	 * 
	 * @param num
	 * @return
	 */
	public static String[] getDaysByback(int num, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String[] result = {};
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(date, "yyyy-MM-dd"));
		// 最近num的日期，包括date日期
		result = new String[num];
		for (int i = 0; i < num; i++) {
			result[i] = sdf.format(new Date(cal.getTimeInMillis()));
			cal.add(Calendar.DAY_OF_YEAR, +1);

		}

		return result;
	}

	/**
	 * 根据当前日期获得星期几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(String dt) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dd = new Date();
		try {
			dd = df.parse(dt);
		} catch (ParseException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dd);

		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	/**
	 * 根据日期获得周几
	 * 
	 * @param dt
	 * @return
	 */
	public static String getWeekOfDate(Date date) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;

		return weekDays[w];
	}

	public static int getBetweenDayNumber(String dateA, String dateB) {
		long dayNumber = 0;
		// 1小时=60分钟=3600秒=3600000
		long mins = 60L * 1000L;
		// long day= 24L * 60L * 60L * 1000L;计算天数之差
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			java.util.Date d1 = df.parse(dateA);
			java.util.Date d2 = df.parse(dateB);
			dayNumber = (d2.getTime() - d1.getTime()) / mins;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) dayNumber;
	}

	public static int getBetweenDayNumber(Date dateA, Date dateB) {
		long dayNumber = 0;
		// 1小时=60分钟=3600秒=3600000
		long mins = 60L * 1000L;
		// long day= 24L * 60L * 60L * 1000L;计算天数之差
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dayNumber = (dateB.getTime() - dateA.getTime()) / mins;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) dayNumber;
	}

	/**
	 * 两个时间相差多少分钟
	 * 
	 * @author 刘德龙
	 * @param args
	 * @throws ParseException
	 */
	public static Long TimeDiff(String pBeginTime, String pEndTime) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date bedate = format.parse(pBeginTime);
			Date endate = format.parse(pEndTime);
			Calendar becal = Calendar.getInstance();
			Calendar encal = Calendar.getInstance();
			becal.setTime(bedate);
			encal.setTime(endate);
			long diffMins = (encal.getTimeInMillis() - becal.getTimeInMillis())
					/ (1000 * 60);
			return diffMins;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: secondDiff
	 * @Description: (比较两个时间相差多少秒钟)
	 * @author cuiyakun
	 * @date 2015年12月29日 上午10:14:00
	 * @param pBeginTime
	 * @param pEndTime
	 * @return
	 * @throws
	 */
	public static Long secondDiff(Date pBeginTime, Date pEndTime) {
		Calendar becal = Calendar.getInstance();
		Calendar encal = Calendar.getInstance();
		becal.setTime(pBeginTime);
		encal.setTime(pEndTime);
		return (encal.getTimeInMillis() - becal.getTimeInMillis()) / 1000;
	}

	/**
	 * 计算两个日期的时间间隔
	 *
	 * @param sDate开始时间
	 *
	 * @param eDate结束时间
	 *
	 * @param type间隔类型
	 *            ("Y/y"--年 "M/m"--月 "D/d"--日)
	 *
	 * @return interval时间间隔
	 * */
	public static int getInterval(Date sDate, Date eDate, String type) {
		// 时间间隔，初始为0
		int interval = 0;

		/* 比较两个日期的大小，如果开始日期更大，则交换两个日期 */
		// 标志两个日期是否交换过
		boolean reversed = false;
		if (compareDate(sDate, eDate) > 0) {
			Date dTest = sDate;
			sDate = eDate;
			eDate = dTest;
			// 修改交换标志
			reversed = true;
		}

		/* 将两个日期赋给日历实例，并获取年、月、日相关字段值 */
		Calendar sCalendar = Calendar.getInstance();
		sCalendar.setTime(sDate);
		int sYears = sCalendar.get(Calendar.YEAR);
		int sMonths = sCalendar.get(Calendar.MONTH);
		int sDays = sCalendar.get(Calendar.DAY_OF_YEAR);

		Calendar eCalendar = Calendar.getInstance();
		eCalendar.setTime(eDate);
		int eYears = eCalendar.get(Calendar.YEAR);
		int eMonths = eCalendar.get(Calendar.MONTH);
		int eDays = eCalendar.get(Calendar.DAY_OF_YEAR);

		if (cTrim(type).equals("Y") || cTrim(type).equals("y")) { // 年
			interval = eYears - sYears;
			if (eMonths < sMonths) {
				--interval;
			}
		} else if (cTrim(type).equals("M") || cTrim(type).equals("m")) { // 月
			interval = 12 * (eYears - sYears);
			interval += (eMonths - sMonths);
		} else if (cTrim(type).equals("D") || cTrim(type).equals("d")) { // 日
			interval = 365 * (eYears - sYears);
			interval += (eDays - sDays);
			// 除去闰年天数
			while (sYears < eYears) {
				if (isLeapYear(sYears)) {
					--interval;
				}
				++sYears;
			}
		}
		// 如果开始日期更大，则返回负值
		if (reversed) {
			interval = -interval;
		}
		return interval;
	}

	/**
	 * 判定某个年份是否是闰年
	 *
	 * @param year待判定的年份
	 *
	 * @return 判定结果
	 * */
	private static boolean isLeapYear(int year) {
		return (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0));
	}

	/**
	 * 字符串去除两头空格，如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
	 * 
	 * @param tStr输入字符串
	 * @return 如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
	 */
	public static String cTrim(String tStr) {
		String ttStr = "";
		if (tStr == null) {

		} else {
			ttStr = tStr.trim();
		}
		return ttStr;
	}

	/**
	 * 比较两个Date类型的日期大小
	 * 
	 * @param sDate开始时间
	 * @param eDate结束时间
	 * @return result返回结果(0--相同 1--前者大 2--后者大)
	 * */
	private static int compareDate(Date sDate, Date eDate) {
		int result = 0;
		// 将开始时间赋给日历实例
		Calendar sC = Calendar.getInstance();
		sC.setTime(sDate);
		// 将结束时间赋给日历实例
		Calendar eC = Calendar.getInstance();
		eC.setTime(eDate);
		// 比较
		result = sC.compareTo(eC);
		// 返回结果
		return result;
	}

	public static void main(String[] args) throws ParseException {
		Date date = getDateFromString("2015-10-15", "yyyy-MM-dd");
		Time time = getTimefromString("10:20", "HH:mm");

		System.out.println(getStringFromDate(getDataByDateAndTime(date, time),
				"yyyy-MM-dd HH:mm:ss"));

		// System.out.println(getStringFromTimestamp(new Timestamp(new
		// java.util.Date().getTime())));
		// java.text.SimpleDateFormat format = new
		// java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
		// Date datetime=format.parse("2010-12-03"+" "+"09:50");
		// Calendar departcal=Calendar.getInstance();
		// departcal.setTime(datetime);
		//
		// Calendar nowcal = Calendar.getInstance();
		// nowcal.add(Calendar.DATE,new Integer("30"));//.
		// System.out.println(format.format(nowcal.getTime()));
		// System.out.println(departcal.compareTo(nowcal));
	}

	public static String getSysTime() {
		return getStringFromDate(new Date(System.currentTimeMillis()),
				"yyyy-MM-dd");
	}

	public static String getCurrentTime(String format) {
		return getStringFromDate(new Date(System.currentTimeMillis()), format);
	}

	public static Timestamp getCurrentTimestamp() {
		return new Timestamp(Calendar.getInstance().getTimeInMillis());
	}

	/**
	 * @author 刘德龙 获取当前时间 精确到分钟
	 * @return
	 */
	public static String getCurrentTime() {
		return getStringFromDate(new Date(System.currentTimeMillis()),
				"yyyy-MM-dd HH:mm");
	}

	public static java.sql.Date getCurrentDate() {
		return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
	}

	public static String getCurrentStringFromDate() {
		return new SimpleDateFormat("yyyy-MM-dd").format(getCurrentDate());
	}

	public static java.sql.Time getCurrentSQLTime() {
		return new Time(System.currentTimeMillis());
	}

	public static java.sql.Time getTimefromString(String time_str, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		try {
			Date date = sf.parse(time_str);
			return new Time(date.getTime());
		} catch (ParseException e) {
		}
		return null;
	}

	/**
	 * 获取当前过mine分钟后的时间
	 * 
	 * @param mine
	 * @return like "2013-01-14 16:32:23"
	 */
	public static String getNextTime(int mine) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, mine); // 得到30分钟后
		return sf.format(calendar.getTime());
	}

	/**
	 * 获取当前日期所在星期的星期一
	 * 
	 * @author ldl
	 * @param date
	 * @return
	 */
	public static String getMonday(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return dateFormat.format(calendar.getTime());
	}

	public static String getCTSToFormatDate(String date2) {
		try {
			java.util.Date date;
			DateFormat df = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
			date = df.parse(date2);
			df = new SimpleDateFormat("yyyy-MM-dd");
			String str = df.format(date);
			return str;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 处理webservice返回的不同格式的日期时间
	 * 
	 * @param orgdatetime
	 * @return
	 */
	public static Timestamp convertWebserviceDateTime(String orgdatetime) {
		if (orgdatetime != null) {
			if (orgdatetime.contains("-")) {
				return NovaDateUtil.getTimestampFromString(orgdatetime,
						"yyyy-MM-dd HH:mm:ss");
			} else {
				return NovaDateUtil.getTimestampFromString(orgdatetime,
						"yyyyMMddHHmmss");
			}
		}
		return null;
	}

	/**
	 * @描述：获取多少小时以后的时间
	 * @开发人员：sunbo
	 * @开发时间：2015年11月3日 上午10:00:07
	 * @param time
	 * @param h
	 * @return
	 */
	public static Time getAfterTimeHour(Time time, double h) {
		Time newTime = new Time((long) (time.getTime() + h * 60 * 60 * 1000));
		return newTime;
	}

	/**
	 * @描述：获取多少小时以后的时间
	 * @开发人员：sunbo
	 * @开发时间：2015年11月3日 上午10:00:07
	 * @param time
	 * @param h
	 * @return
	 */
	public static String getAfterTimeSecond(String timeStr, int sec) {
		String[] timeArr = timeStr.split(":");
		if (timeArr.length == 2) {
			timeStr += ":00";
		}
		Time time = Time.valueOf(timeStr);
		Time newTime = new Time((long) (time.getTime() + sec * 1000));
		return newTime.toString();
	}

	/**
	 * 获取多少秒之后的时间
	 * 
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date getDateByAfterSecond(Date date, int second) {
		if (null == date) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		Date afterDate = calendar.getTime();
		return afterDate;
	}

	/**
	 * 描述: 获取多少天之后的时间
	 * 
	 * @author: cuiyakun
	 * @date:2017年4月24日 下午5:13:58
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateByAfterDay(Date date, int day) {
		if (null == date) {
			date = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		Date afterDate = calendar.getTime();
		return afterDate;
	}

	public static Date getDataByDateAndTime(Date date, Time time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, time.getHours());
		calendar.set(Calendar.MINUTE, time.getMinutes());
		calendar.set(Calendar.SECOND, time.getSeconds());
		return calendar.getTime();
	}

	public static Date getDataByDateAndStringTime(Date date, String timeval,
			String format) {
		Time time = getTimefromString(timeval, format);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, time.getHours());
		calendar.set(Calendar.MINUTE, time.getMinutes());
		calendar.set(Calendar.SECOND, time.getSeconds());
		return calendar.getTime();
	}

	/**
	 * 将date转换为另一种格式的date
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date getDataByDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateString = sdf.format(date);
		Date newDate;
		try {
			newDate = sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return newDate;
	}

	/**
	 * 例如今天是11-12日，如果是当天返回12:30，如果过了当天则返回 11-10 12:30
	 * 
	 * @param date
	 * @param format
	 * @return
	 */

	public static String getStringFromDateSpecial(Date date, String format) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dateStr = sdf.format(date);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = new Date();
		int diff = dateDiff(sdf2.format(date), sdf2.format(nowDate));// 判断两个日期相差的天数

		if (diff == 0) {
			dateStr = "今天 " + dateStr.substring(6, 11);
		}
		return dateStr;
	}

	/**
	 * 
	 * 描述:格式化当前时间
	 * 
	 * @author: Yang Yanjiao
	 * @date:2017年6月14日 上午9:01:16
	 * @param format
	 * @return
	 */
	public static Date getNowDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = sdf.format(new Date());
		Date newDate;
		try {
			newDate = sdf.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
		return newDate;
	}

	/**
	 * @描述：指定日期凌晨
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:51:58
	 * @param date
	 * @return
	 */
	public static Date beginOfDay(Date date) {
		if (date == null) {
			date = current();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * @描述：今天凌晨
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:52:08
	 * @return
	 */
	public static Date beginOfToday() {
		return beginOfDay(current());
	}

	/**
	 * @描述：当前日期
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:52:16
	 * @return
	 */
	public static Date current() {
		return new Date();
	}

	/**
	 * @描述：指定日期午夜
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:52:24
	 * @param date
	 * @return
	 */
	public static Date endOfDay(Date date) {
		if (date == null) {
			date = current();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.SECOND, -1);
		return c.getTime();
	}

	/**
	 * @描述：今天午夜
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:52:34
	 * @return
	 */
	public static Date endOfToday() {
		return endOfDay(current());
	}

	/**
	 * @描述：当前日期的月初
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:52:46
	 * @return
	 */
	public static Date firstDayOfMonth() {
		return firstDayOfMonth(current());
	}

	/**
	 * @描述：指定日期的月初
	 * @开发人员：wuyanxi
	 * @开发时间：2018年8月17日下午1:52:53
	 * @param date
	 * @return
	 */
	public static Date firstDayOfMonth(Date date) {
		if (date == null) {
			date = current();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DATE, 1);
		return c.getTime();
	}

}