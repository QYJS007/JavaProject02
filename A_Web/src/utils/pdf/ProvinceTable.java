package utils.pdf;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author dongchao
 * @version 创建时间：Nov 1, 2013 2:47:26 PM
 * 导出的pdf底部时间显示PdfPTable模板不需要传入参数，时间为系统当前时间，
 * 只是设置文本显示字体
 */
public class ProvinceTable extends PdfPTable{
	public ProvinceTable(String provincename){
		super(1);
		setLockedWidth(true);
		setHorizontalAlignment(ALIGN_LEFT);
		
		Chunk chineseChunk =new Chunk(provincename,FontUtil.titleFont);
		Phrase p = new Phrase();
		p.add(chineseChunk);
		PdfPCell provinceCell = new PdfPCell(p);
		provinceCell.setBorderWidth(0f);
		
		provinceCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		addCell(provinceCell);
	}
	
}
