package cg.pdf;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.pdf.BasePdfPCell;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class A_Test {

	public static void main(String[] args) {
		try {
			File createPDF = createPDF();
			System.out.println(createPDF);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据传入数据生成pdf文件,以 时间戳(System.currentTimeMillis)_标题.pdf 命名
	 * @param pageSize  页面大小 传入null则为A4纸.
	 * @param centerRowsDate 页面中部要渲染的数据
	 * @param centerColumn   页面中部要显示的列名称
	 * @param bottomRowsDate  页面底部数据要渲染的数据
	 * @param buttomColumn    页面地图要显示的列名称
	 * @param operName        操作员名称
	 * @param startDate		   查询定义的开始时间
	 * @param endDate        查询定义的截止时间
	 * @param title           pdf文档标题
	 * @return File  根据传入数据生成的pdf文件,以时间戳(System.currentTimeMillis)_标题.pdf  命名
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws Exception 
	 */
	public static File createPDF()throws DocumentException, MalformedURLException, IOException{
		Rectangle pageSize  = null;
		if(null == pageSize){
			pageSize = PageSize.A4;
		}
		pageSize = PageSize.A4.rotate();
		Document doc = new Document(pageSize);
		String title = "bus365";
		//		创建要返回的文件
		String path = "D:/AAA/";
		String tempFileName = System.currentTimeMillis()+"_"+"bus365"+".pdf";
		File temp = new File(path);
		if(!temp.exists()){
			temp.mkdir();
		}else{
			/*try {
				deletefile(path);
			} catch (IOException e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		temp = new File(path+tempFileName);
		OutputStream os;
		try {
			os = new BufferedOutputStream(new FileOutputStream(temp));
			PdfWriter.getInstance(doc, os);
			//设置标题
			doc.addTitle(title);
			doc.open();
			Image img = Image.getInstance("src/image/bus365.jpg");
			img.setAlignment(Image.RIGHT);
			img.setBorder(Image.BOX);
			img.setBorderWidth(10);
			//img.setBorderColor(BaseColor.WHITE);
			//img.scaleToFit(1000, 72);// 大小
			//img.setRotationDegrees(-30);// 旋转

			// 设置图片宽高
			//img.scaleToFit(signRect.getWidth(), signRect.getHeight());
			//img.scaleAbsolute(2, 1);
			// 设置图片位置
			//img.setAbsolutePosition(x, y);// 左边距、底边距
			//doc.setPageSize(img);
			//doc.newPage();
			img.setAbsolutePosition(0, doc.top(20));
			doc.add(img);



			// 设置标题: ========start =====================================
			BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			//Font FontChinese = new Font(bfChinese, 12, Font.NORMAL); 正常字体 ; 

			Font fontCN = new Font(bfChinese, 20,Font.BOLD);// 加粗字体
			Paragraph paragraph = new Paragraph("Bus365—行程单", fontCN);

			paragraph.setAlignment(Cell.ALIGN_CENTER);
			doc.add(paragraph);

			doc.add(new  Paragraph());
			/*PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(
					 file));
					 doc.open();
					 BaseFont bfChinese = BaseFont.createFont("STSong-Light",
					 "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
					 Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
					 Font FontChinese1 = new Font(bfChinese, 14, Font.BOLD);

					 Paragraph t = new Paragraph(d.getTitle(), FontChinese1);
					 t.setAlignment(Paragraph.ALIGN_CENTER);
					 doc.add(t);*/

			// ============设置标题 =end=================

			List<String> columnList = new ArrayList<String>();

			columnList.add("业务类型 ");
			columnList.add("用车时间 ");
			columnList.add("起点   ");
			columnList.add("终点     ");
			columnList.add("序号     ");
			columnList.add("金额（元）");
			columnList.add("备注     ");
			List<String> columnEnList = new ArrayList<String>();
			columnEnList.add("businesscode") ;
			columnEnList.add("transportTime") ;
			columnEnList.add("startStation") ;
			columnEnList.add("endStation ") ;
			columnEnList.add("num") ;
			columnEnList.add("money");
			columnEnList.add("note") ;


			List<Map<String,String >> date = new ArrayList<Map<String,String >>();


			HashMap<String, String> dateMap = new HashMap<String, String>();
			int i= 1; 
			for (String string : columnEnList) {
				/*dateMap.put(string, "1");
				dateMap.put(string, "2");
				dateMap.put(string, "3");
				dateMap.put(string, "4");
				dateMap.put(string, "5");
				dateMap.put(string, "6");*/
				dateMap.put(string, "超过"+i++);
			}
			date.add(dateMap);
			Integer columnNum= columnList.size();
			//创建一个有3列的表格
			
			PdfPTable table = new PdfPTable(columnNum);
			
			//table.set
			
			//定义一个表格单元
			//PdfPCell cell = new PdfPCell(new Paragraph("header with colspan 3"));
			//PdfPCell cell = new PdfPCell();
			//定义一个表格单元的跨度
			//cell.setColspan(columnNum);
			//把单元加到表格中
			//table.addCell(cell);
			for (String str : columnList) {
				//System.out.println(str);
				//把下面这9项顺次的加入到表格中，当一行充满时候自动折行到下一行
				BasePdfPCell basePdfPCell = new BasePdfPCell(str.toString());
				//定义单元格的框颜色
				//	basePdfPCell.setBorderColor(new Color(255, 0, 0));
				//定义单元格的背景颜色
				basePdfPCell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));

				/*PdfPCell cell1 = new PdfPCell();
		        Paragraph para = new Paragraph("该单元居中");
		        //设置该段落为居中显示
		        para.setAlignment(1);
		        cell1.setPhrase(para);
		        table.addCell(cell1);*/


				//先调用Cell.setUseAscender(true); 
				//再调用Cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); 
				basePdfPCell.setUseAscender(true);
				basePdfPCell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
				basePdfPCell.setHorizontalAlignment(Cell.ALIGN_CENTER);

				table.addCell(basePdfPCell);
			}


			for (Map<String, String> map : date) {
				for (String str : columnEnList) {
					BasePdfPCell basePdfPCell2 = new BasePdfPCell(map.get(str));
					table.addCell(basePdfPCell2);
				}
			}

			doc.add(table);
			/*// 定义中文字体
			  BaseFont bfChinese = BaseFont.createFont("STSong-Light",
			    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			com.lowagie.text.Font fontCN = new com.lowagie.text.Font(bfChinese, 12,
				    com.lowagie.text.Font.NORMAL);
			doc.add(new Paragraph("默认情况下的大小---居中 80%", fontCN));
			 doc.add(new Paragraph("默认情况下的大小---居中 80%", fontCN));
			   // 增加到文档中
			 doc.add(table);
			 doc.add(new Paragraph("居中 100%", fontCN));
			   // 设置表格大小为可用空白区域的100%
			   table.setWidthPercentage(100);
			   // 增加到文档中2
			   doc.add(table);
			   doc.add(new Paragraph("居右 50%", fontCN));
			   // 设置表格大小为可用空白区域的50%
			   table.setWidthPercentage(50);
			   // 设置水平对齐方式为 居右
			   table.setHorizontalAlignment(Element.ALIGN_RIGHT);
			   doc.add(new Paragraph("居左 50%", fontCN));
			   // 增加到文档中3
			   doc.add(table);
			   // 设置水平对齐方式为 居左
			   table.setHorizontalAlignment(Element.ALIGN_LEFT);
			   doc.add(table);*/
			doc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (DocumentException e) {
			e.printStackTrace();
			throw e;
		} 
		/*if(iswatermark){
			String templateFileName = System.currentTimeMillis()+"_w_"+title+".pdf";
			temp=setWatermark(temp,path+templateFileName,"bus365");
		}*/
		return temp;
	}

}
