package utils.pdf;

import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * @author dongchao
 * @version 创建时间：Oct 31, 2013 9:22:45 AM
 * 根据传入的数据List listData跟列名String[] clumn生成PdfPTable对象的模板
 */
public class CenterTable extends PdfPTable{
	int submitAmount=500;
	public CenterTable(Document document,List<Object[]> listData,String[] column) throws DocumentException{
		super(column.length);
		setHorizontalAlignment(ALIGN_LEFT);
		//setLockedWidth(true);
	    setWidthPercentage(100);
		//将列渲染至文档
		for(int i=0;i<column.length;i++){
			PdfPCell p = new BasePdfPCell(column[i]);
			addCell(p);
		}
		//将数据渲染至文档
		int rowsNumber=listData.size();
		for(int m=0;m<rowsNumber;m++){
			Object[] o = listData.get(m);
			for(int n=0;n<o.length;n++){
				PdfPCell p = null;
				if(null ==o[n]){
					p = new BasePdfPCell("");
				}else{
					p = new BasePdfPCell(o[n].toString());
				}
				addCell(p);
			}
			 //表格内容每写满某个数字的行数时，其内容一方面写入物理文件，另一方面释放内存中存留的内容。
			if((m%submitAmount)==0){
				document.add(this);
				deleteBodyRows();
            }else if(m==rowsNumber){
            	//如果全部类容完毕且又没达到某个行数限制，则也要写入物理文件中。
            	document.add(this);
            	this.deleteBodyRows();
            }
		}
	}
	
	
	public CenterTable(Document document,List<Object[]> listData,String[] column,Integer fontsize,int length) throws DocumentException{
		super(length);
		setHorizontalAlignment(ALIGN_LEFT);
		//setLockedWidth(true);
	    setWidthPercentage(100);
		boolean s=column.length==length;
		
		//将列渲染至文档
		int rowsNumber=listData.size();
		for(int i=0;i<column.length;i++){
			PdfPCell p = new BasePdfPCell(column[i]);
			if(!s&&column[i].indexOf("数")!=column[i].length()-1){
				p.setColspan(2);
			}
			addCell(p);
		}
		//将数据渲染至文档
		for(int m=0;m<listData.size();m++){
			Object[] o = listData.get(m);
			for(int n=0;n<o.length;n++){
				PdfPCell p = null;
				if(null ==o[n]){
					p = new BasePdfPCell("",fontsize);
				}else{
					p = new BasePdfPCell(o[n].toString(),fontsize);
				}
				if(!s&&column[n].indexOf("数")!=column[n].length()-1){
					p.setColspan(2);
				}
				addCell(p);
			}
			 //表格内容每写满某个数字的行数时，其内容一方面写入物理文件，另一方面释放内存中存留的内容。
			if((m%submitAmount)==0){
				document.add(this);
				deleteBodyRows();
            }else if(m==rowsNumber){
            	//如果全部类容完毕且又没达到某个行数限制，则也要写入物理文件中。
            	document.add(this);
            	this.deleteBodyRows();
            }
		}
	}
}
