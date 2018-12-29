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
public class TimeTable extends PdfPTable{
	public TimeTable(){
		super(1);
		setLockedWidth(true);
		setHorizontalAlignment(ALIGN_LEFT);
		
		Date time = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		
		Chunk timeChunk = new Chunk(sdf.format(time),FontUtil.timeFont);
		Chunk chineseChunk =new Chunk("出单时间：",FontUtil.operInfoFont);
		Phrase p = new Phrase();
		p.add(chineseChunk);
		p.add(timeChunk);
		PdfPCell timeCell = new PdfPCell(p);
		timeCell.setBorderWidth(0f);
		
		timeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		addCell(timeCell);
	}
	
}
