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

public class A_bus365 {

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
		
		
		
		String title = "3652322332";
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
			//table1
			PdfPTable table1 = new PdfPTable(3);
			PdfPCell cell11 = new PdfPCell();
			cell11.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell11.setBorder(0);
			// String imagePath = "D:/alibaba.jpg";
			// Image image1 = Image.getInstance(imagePath); 
			Image img = Image.getInstance("src/image/122_119.png");

			// Image image2 = Image.getInstance(imagePath); 
			//设置每列宽度比例   
			int width11[] = {35,40,25};
			table1.setWidths(width11); 
			table1.getDefaultCell().setBorder(0);
			table1.addCell(img);  
			table1.addCell(cell11);  
			table1.addCell(cell11);
			doc.add(table1);
			BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);   
			//加入空行
			Font FontChinese18 = new Font(bfChinese, 18, Font.BOLD); 

			Paragraph blankRow1 = new Paragraph(18f, " ", FontChinese18); 
			doc.add(blankRow1);


			// 设置标题: ========start =====================================
			//BaseFont bfChinese = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			//Font FontChinese = new Font(bfChinese, 12, Font.NORMAL); 正常字体 ; 

			Font fontCN = new Font(bfChinese, 20,Font.BOLD);// 加粗字体
			Paragraph paragraph = new Paragraph("3651313", fontCN);

			paragraph.setAlignment(Cell.ALIGN_CENTER);
			doc.add(paragraph);

			doc.add(new  Paragraph());
			/*PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream( file));
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
				dateMap.put(string, "超过"+i++);
			}
			date.add(dateMap);
			// ================准备数据结束; 
			//加入空行
			Font FontChinese11 = new Font(bfChinese, 11, Font.ITALIC);
			Paragraph blankRow41 = new Paragraph(18f, " ", FontChinese11); 
			doc.add(blankRow41);



			// 创建Chunk对象，设置下划线的厚度为0.1
			/* Chunk underline = new Chunk("                          ");
            underline.setUnderline(0.1f, -1f);
            doc.add(underline);*/
			// 创建Chunk对象，设置下划线的厚度为1
			// Chunk strike = new Chunk("outofmemory.cn");
			// strike.setUnderline(1f, 3f);
			//doc.add(strike);


			Font FontChinese11Normal = new Font(bfChinese, 11, Font.NORMAL);
			//table3
			PdfPTable table2 = new PdfPTable(6);
			int width2[] = {10,25,15,25,15,25};
			table2.setWidths(width2); 
			
			Chunk underline = new Chunk("                          ");
            underline.setUnderline(0.1f, -1f);
			PdfPCell cell21 = new PdfPCell(new Paragraph("\r姓名:",FontChinese11Normal));
			PdfPCell cell221 = new PdfPCell();
			PdfPCell cell22 = new PdfPCell(new Paragraph("\r工号:",FontChinese11Normal));
			PdfPCell cell222 = new PdfPCell();
			PdfPCell cell23 = new PdfPCell(new Paragraph("\r部门:",FontChinese11Normal));
			PdfPCell cell233 = new PdfPCell();

			cell21.setBorder(0);
			cell22.setBorder(0);
			cell23.setBorder(0);

			//cell221.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
			cell221.setBorderColor(new Color(0xC0, 0xC0, 0xC0));
			cell222.setBorderColor(new Color(0xC0, 0xC0, 0xC0));
			cell233.setBorderColor(new Color(0xC0, 0xC0, 0xC0));
			
			cell221.disableBorderSide(13);
			cell222.disableBorderSide(13);
			cell233.disableBorderSide(13);
			
			cell21.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell22.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell23.setHorizontalAlignment(Element.ALIGN_RIGHT);

			table2.addCell(cell21);
			table2.addCell(cell221);
			table2.addCell(cell22);
			table2.addCell(cell222);
			table2.addCell(cell23);
			table2.addCell(cell233);
			doc.add(table2);

			//加入空行
			Paragraph blankRow31 = new Paragraph(18f, " ", FontChinese11); 
			doc.add(blankRow31);


			//table3
			PdfPTable table3 = new PdfPTable(2);
			// int width3[] = {40,35,25};
			int width3[] = {40,60};
			table3.setWidths(width3); 
			PdfPCell cell31 = new PdfPCell(new Paragraph("申请日期："+"2011-11-11",FontChinese11Normal));
			PdfPCell cell32 = new PdfPCell(new Paragraph("行程日期："+"2011-11-11",FontChinese11Normal));
			cell31.setBorder(0);
			cell32.setBorder(0);
			// cell33.setBorder(0);
			table3.addCell(cell31);
			table3.addCell(cell32);
			// table3.addCell(cell33);
			doc.add(table3);
			//加入空行
			Paragraph blankRow21 = new Paragraph(18f, " ", FontChinese11); 
			doc.add(blankRow21);

			//table4
			PdfPTable table4 = new PdfPTable(2);
			int width4[] = {40,60};
			table4.setWidths(width4); 
			PdfPCell cell41 = new PdfPCell(new Paragraph("行程人手机号："+"XXX",FontChinese11Normal));
			PdfPCell cell42 = new PdfPCell(new Paragraph("共一笔行程合计6元"+"XXX",FontChinese11Normal));
			cell41.setBorder(0);
			cell42.setBorder(0);
			table4.addCell(cell41);
			table4.addCell(cell42);
			doc.add(table4);
			//加入空行
			Paragraph blankRow411 = new Paragraph(18f, " ", FontChinese11); 
			doc.add(blankRow411);


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
			Font FontChinese11Bold = new Font(bfChinese, 11, Font.BOLD);

	//Font FontChinese11 = new Font(bfChinese, 11, Font.ITALIC);

			for (String str : columnList) {
				/*//System.out.println(str);
				//把下面这9项顺次的加入到表格中，当一行充满时候自动折行到下一行
				BasePdfPCell basePdfPCell = new BasePdfPCell(str.toString());
				//定义单元格的框颜色
				//	basePdfPCell.setBorderColor(new Color(255, 0, 0));
				//定义单元格的背景颜色
				basePdfPCell.setBackgroundColor(new Color(0xC0, 0xC0, 0xC0));
 cell71.setBorderColor(lightGrey);
				PdfPCell cell1 = new PdfPCell();
		        Paragraph para = new Paragraph("该单元居中");
		        //设置该段落为居中显示
		        para.setAlignment(1);
		        cell1.setPhrase(para);
		        table.addCell(cell1);
				//水平居中
				basePdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				basePdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
*/
				PdfPCell basePdfPCell = new PdfPCell(new Paragraph(str.toString(),FontChinese11Bold));
				//先调用Cell.setUseAscender(true); 
				//再调用Cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); 
				//basePdfPCell.setUseAscender(true);
				basePdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				basePdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				Color lightGrey = new Color(0xCC,0xCC,0xCC);

				basePdfPCell.setBackgroundColor(lightGrey);
				/*
				 * //table7
            PdfPTable table7 = new PdfPTable(6);
            BaseColor lightGrey = new BaseColor(0xCC,0xCC,0xCC);
            int width7[] = {20,18,13,20,14,15};
            table7.setWidths(width7); 
            PdfPCell cell71 = new PdfPCell(new Paragraph("费用类型",FontChinese11Bold));
            PdfPCell cell72 = new PdfPCell(new Paragraph("费用发生时间",FontChinese11Bold));
            PdfPCell cell73 = new PdfPCell(new Paragraph("详细信息",FontChinese11Bold));
            PdfPCell cell74 = new PdfPCell(new Paragraph("消费金币/币种",FontChinese11Bold));
            PdfPCell cell75 = new PdfPCell(new Paragraph("报销汇率",FontChinese11Bold));
            PdfPCell cell76 = new PdfPCell(new Paragraph("报销金额",FontChinese11Bold));
            //表格高度
            cell71.setFixedHeight(25);
            cell72.setFixedHeight(25);
            cell73.setFixedHeight(25);
            cell74.setFixedHeight(25);
            cell75.setFixedHeight(25);
            cell76.setFixedHeight(25);
            //水平居中
            cell71.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell72.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell73.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell74.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell75.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell76.setHorizontalAlignment(Element.ALIGN_CENTER);
            //垂直居中
            cell71.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell72.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell73.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell74.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell75.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell76.setVerticalAlignment(Element.ALIGN_MIDDLE);
            //边框颜色
            cell71.setBorderColor(lightGrey);
            cell72.setBorderColor(lightGrey);
            cell73.setBorderColor(lightGrey);
            cell74.setBorderColor(lightGrey);
            cell75.setBorderColor(lightGrey);
            cell76.setBorderColor(lightGrey);
            //去掉左右边框
            cell71.disableBorderSide(8);
            cell72.disableBorderSide(4);
            cell72.disableBorderSide(8);
            cell73.disableBorderSide(4);
            cell73.disableBorderSide(8);
            cell74.disableBorderSide(4);
            cell74.disableBorderSide(8);
            cell75.disableBorderSide(4);
            cell75.disableBorderSide(8);
            cell76.disableBorderSide(4);
            table7.addCell(cell71);
            table7.addCell(cell72);
            table7.addCell(cell73);
            table7.addCell(cell74);
            table7.addCell(cell75);
            table7.addCell(cell76);
            document.add(table7);

				 */
				table.addCell(basePdfPCell);
			}

	  Font FontChinese12 = new Font(bfChinese, 12, Font.NORMAL);

			for (Map<String, String> map : date) {
				for (String str : columnEnList) {
					//BasePdfPCell basePdfPCell2 = new BasePdfPCell(map.get(str));
					//table.addCell(basePdfPCell2);
					//FontChinese12
					//先调用Cell.setUseAscender(true); 
					//再调用Cell.setVerticalAlignment(Cell.ALIGN_MIDDLE); 
					//basePdfPCell.setUseAscender(true);
					PdfPCell basePdfPCell = new PdfPCell(new Paragraph(map.get(str),FontChinese12));
					basePdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					basePdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					basePdfPCell.setFixedHeight(25);

					//Color lightGrey = new Color(0xCC,0xCC,0xCC);

					//basePdfPCell.setBackgroundColor(lightGrey);
					table.addCell(basePdfPCell);
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
