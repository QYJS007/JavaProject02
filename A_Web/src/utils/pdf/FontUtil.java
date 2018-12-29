package utils.pdf;

import java.io.IOException;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

/**
 * @author dongchao
 * @version 创建时间：Oct 30, 2013 4:01:31 PM
 * 获取项目使用到的各种Font
 */
public class FontUtil {
	private static BaseFont bfChinese= null;
	public static Font commonFont = null;  //汉字通用字体
	public static Font titleFont = null; //标题字体
	public static Font operInfoFont = null;  //“出单人”  字体
	public static Font timeFont = null;     //“出单人” 一行时间字体
	public static Font letter_common = null;//非汉字通用字体
	static {
		try {
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			commonFont = new Font(bfChinese,6);
			titleFont = new Font(bfChinese,11, Font.BOLD);
			operInfoFont = new Font(bfChinese, 9, Font.BOLD);
			timeFont = new Font(FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD));
			letter_common = new Font(FontFactory.getFont("Arial", 6));
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
