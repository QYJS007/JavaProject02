package cg.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDF {

	public static void main(String[] args) {

		try {
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	public static void test() throws Exception{
		// 2.第二，定义一个字符串是存储PDF文件的路径，代码如下：
		//String pdfFile = "D:/BaiduNetdiskDownload/PdfTest/a.pdf";
		String pdfFile = "C:/Users/Administrator/Desktop/01111.pdf";
		//第三：加载文件的路径
		PDDocument doc = PDDocument.load(new File(pdfFile ));
		//4.第四获取总页数
		int pagenumber=doc.getNumberOfPages();//获取总页数
		
		//5.第五：定义转换的.doc文件的路径，需要截取来自pdfFile 文件的路径名
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(pdfFile .substring(0, pdfFile .indexOf("."))+".doc");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//6.第六：设置文件的格式编码
		Writer writer = null;
		try {
			writer = new OutputStreamWriter(fos,"UTF-8");//文件按字节读取，然后按照UTF-8的格式编码显示
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//7.第七：生成PDF文档内容剥离器，然后，设置转换的开始页、结束页

		PDFTextStripper stripper = new PDFTextStripper();//生成PDF文档内容剥离器
		stripper.setSortByPosition(true);//排序
		stripper.setStartPage(1);//设置转换的开始页
		stripper.setEndPage(pagenumber);//设置转换的结束页
		try {
			stripper.writeText(doc,writer);
			writer.close();
			doc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
