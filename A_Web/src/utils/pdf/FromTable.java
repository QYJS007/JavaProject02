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
public class FromTable extends PdfPTable{
	public FromTable(String from){
		super(1);
		setLockedWidth(true);
		setHorizontalAlignment(ALIGN_LEFT);
		
		Chunk chineseChunk =new Chunk(from,FontUtil.operInfoFont);
		Phrase p = new Phrase();
		p.add(chineseChunk);
		PdfPCell fromCell = new PdfPCell(p);
		fromCell.setBorderWidth(0f);
		
		fromCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		addCell(fromCell);
	}
	
}
