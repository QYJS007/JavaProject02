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
	 * @������������ת��Ϊ�ַ���,��ʽΪ:yyyy-MM-dd
	 * @������Ա��
	 * @����ʱ�䣺2015��7��24�� ����08:00:00
	 * @param date Ҫ��ʽ��������
	 * @return ��ʽ���ַ���
	 */
	public static String formatDate(Date date) {
		String yyyy_MM_dd ="";
		SimpleDateFormat sf = new SimpleDateFormat(yyyy_MM_dd );
		return sf.format(date);
	}
}
