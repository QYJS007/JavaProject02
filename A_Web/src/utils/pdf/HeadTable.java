package utils.pdf;

import com.lowagie.text.Chunk;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author dongchao
 * @version 创建时间：Oct 30, 2013 3:19:01 PM
 * 导出的pdf文件头部显示标题、操作员跟时间的PdfPTable模板
 */
public class HeadTable extends PdfPTable{
	public HeadTable(String title,String operName,String startTime,String endTime){
		super(3);
		setLockedWidth(true);
		setHorizontalAlignment(ALIGN_LEFT);
		
		BasePdfPCell nothing = new BasePdfPCell(0f);
		addCell(nothing);
		PdfPCell titleCell = new BasePdfPCell(0f,title,FontUtil.titleFont);
		titleCell.setHorizontalAlignment(ALIGN_CENTER);
		addCell(titleCell);
		addCell(nothing);
		PdfPCell oper = null;
		if(null != operName && !("".equals(operName.trim()))){
			oper = new BasePdfPCell(0f,"出单人: "+operName,FontUtil.operInfoFont);
			addCell(oper);
		}else{
			addCell(nothing);
		}
		Chunk startChunk = null;
		if(startTime != null && !"".equals(startTime)){
			startChunk = new Chunk(startTime,FontUtil.timeFont);
		}
		Chunk chineseChunk =new Chunk("至",FontUtil.operInfoFont);
		Chunk endChunk = null;
		if(endTime != null && !"".equals(endTime)){
			endChunk = new Chunk(endTime,FontUtil.timeFont);
		}
		Phrase p = new Phrase();
		if(startChunk != null && endChunk != null){
			p.add(startChunk);
			p.add(chineseChunk);
			p.add(endChunk);
		}
		PdfPCell timeCell = new PdfPCell(p);
		timeCell.setBorder(0);
		timeCell.setHorizontalAlignment(ALIGN_CENTER);
		addCell(timeCell);
		addCell(nothing);
		
	}
	
	public HeadTable(){
		super();
	}
}
