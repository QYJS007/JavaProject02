package utils.pdf;

import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;

/**
 * @author dongchao
 * @version 创建时间：Oct 30, 2013 4:14:16 PM
 * 一些可以使用borderWidth,或者String作为参数的PdfPCell构造函数
 */
public class BasePdfPCell extends PdfPCell{
	/**
	 * @param width  border宽度
	 */
	public BasePdfPCell(float width){
		super();
		setBorderWidth(width);
	}
	/**
	 * @param content  要写入Cell中的内容
	 * @param font  使用的字体
	 */
	public BasePdfPCell(String content,Font font){
		super(new Phrase(content,font));
	}
	/**
	 * @param content  要写入Cell中的内容使用默认字体
	 */
	public BasePdfPCell(String content){
		super();
		Font font = null;
		if(RegexUtil.letter.matcher(content).matches()){//非汉字
			font = FontUtil.letter_common;
			font.setSize(12);
		}else{
			font = FontUtil.commonFont;
			font.setSize(12);
		}
		Phrase p = new Phrase(content,font);
		addElement(p);
		
	}
	/**
	 * @param content  要写入Cell中的内容使用默认字体
	 */
	public BasePdfPCell(String content,Integer fontsize){
		super();
		Font font = null;
		if(fontsize==null||fontsize<=0){
			fontsize=12;
		}
		if(new RegexUtil().letter.matcher(content).matches()){//非汉字
			font = FontUtil.letter_common;
			font.setSize(fontsize);
		}else{
			font = FontUtil.commonFont;
			font.setSize(fontsize);
		}
		Phrase p = new Phrase(content,font);
		addElement(p);
		
	}
	/**
	 * @param width  border宽度
	 * @param content  要写入Cell中的内容
	 * @param font  使用的字体
	 */
	public BasePdfPCell(float width,String content,Font font){
		this(content,font);
		setBorderWidth(width);
	}
}
