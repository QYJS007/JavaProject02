package cg.toWord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

public class PDF2Word {

	public static void main(String[] args){
		try{
			//String pdfFile = "C:/xxxxx.pdf";
			String pdfFile = "C:/Users/Administrator/Desktop/01111.pdf";
			PDDocument doc = PDDocument.load(new File(pdfFile));
			
			int pagenumber = doc.getNumberOfPages();
			
			pdfFile = pdfFile.substring(0, pdfFile.lastIndexOf("."));
			
			String fileName = pdfFile + ".doc";
			
			File file = new File(fileName);
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(fileName);
			Writer writer = new OutputStreamWriter(fos, "UTF-8");
			
			
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);// 排序
			stripper.setStartPage(1);// 设置转换的开始页
			stripper.setEndPage(pagenumber);// 设置转换的结束页
			stripper.writeText(doc, writer);
			writer.close();
			doc.close();
			System.out.println("pdf转换word成功！");
			
			
/*
 *
	
	
		

 */
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
