package cg.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdfwriter.ContentStreamWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

public class ToWord {
	//private static final int Pageno = 0;


	public static void main(String[] args) {

		String pdfFile = "C:/Users/Administrator/Desktop/01111.pdf";
		//READPDF(pdfFile);
		try {
			int pageNo = getPageNo(pdfFile);
			for (int i = 1; i <= pageNo; i++) {
				System.out.println();
				String str = readPageNO( pdfFile,i);
				System.out.println("第"+i+"ye, content "+str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 * 提取图片并保存
	 * @param pdfDomainVO 
	 * @throws IOException 
	 * 
	 */
	public static pdfDomainVO extractImage(pdfDomainVO vo ) throws IOException{
		//创建文档  
		PDDocument doc=null;
		try{
			//加载 pdf 文档,获取PDDocument文档对象
			doc=PDDocument.load(vo.getInputfile());           
			/** 文档页面信息 **/  
			//获取PDDocumentCatalog文档目录对象
			PDDocumentCatalog catalog = doc.getDocumentCatalog();
			//获取文档页面PDPage列表
			// List pages = catalog.getAllPages();  

			PDPageTree pages= catalog.getPages();
			// int pageNum=pages.size();   //文档页数
			int count2 = pages.getCount();

			PDPage page = null;
			// if(vo.getPageno()!=null){
			//  page = ( PDPage ) pages.get( vo.getPageno() ); 
			page = ( PDPage ) pages.get(2);
			if( null != page ){  
				//  PDResources resource = page.findResources();                      
				PDResources resources = page.getResources();
				COSDictionary cosObject = resources.getCOSObject();
				//cosObject.
			//	resources.getXObject(cosObject.getCOSName(key))

				//获取页面图片信息 
				/*Map<String,PDXObjectImage> imgs = resources.getImages();                    
				for(Map.Entry<String,PDXObjectImage> me: imgs.entrySet()){
					//System.out.println(me.getKey());
					PDXObjectImage img = me.getValue();  
					//保存图片，会自动添加图片后缀类型
					img.write2file( vo.getOutputfile() + vo.getFilename()+"-"+(vo.getPageno()+1) );     
				}  */
			}  
			/*  }else{
                //遍历每一页
                for( int i = 0; i < pageNum; i++ ){  
                    //取得第i页
                     page = ( PDPage ) pages.get( i ); 
                    if( null != page ){  
                        PDResources resource = page.findResources();                      
                        //获取页面图片信息 
                        Map<String,PDXObjectImage> imgs = resource.getImages();                    
                        for(Map.Entry<String,PDXObjectImage> me: imgs.entrySet()){
                            String count=(int)(Math.random()*1000)+"-"+(int)(Math.random()*1000);
                            //System.out.println(me.getKey());
                            PDXObjectImage img = me.getValue();  
                            //保存图片，会自动添加图片后缀类型
                            img.write2file( vo.getOutputfile() + count );  
                        }  
                    }  
                } 
            }*/
			//操作后的页数
			//vo.setAfterPages(doc.getNumberOfPages());
			System.out.println("extractImage:over");
		}finally{
			if( doc != null ){
				doc.close();
			}
		}
	}




	/**
	 * 读取pdf中文字信息(指定从第几页开始)
	 * @return 
	 * @throws IOException 
	 * @throws InvalidPasswordException 
	 */
	public static int  getPageNo(String inputFile) throws Exception{   
		PDDocument document = PDDocument.load(new File(inputFile));
		// 获取页码
		int pages = document.getNumberOfPages();
		return  pages;
	}

	/**
	 * 读取pdf中文字信息(指定从第几页开始)
	 * @return 
	 */
	public static String  readPageNO(String inputFile ,Integer Pageno){   
		String content="";        
		try{
			PDDocument document = PDDocument.load(new File(inputFile));
			// 获取页码
			//   int pages = document.getNumberOfPages();
			// 读文本内容
			PDFTextStripper stripper=new PDFTextStripper();
			// 设置按顺序输出
			stripper.setSortByPosition(true);
			stripper.setStartPage(Pageno);
			stripper.setEndPage(Pageno);
			//获取内容
			content = stripper.getText(document);
			// vo.setContent(content);
			//System.out.println("content "+content);

			System.out.println("function : readPageNO over");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}





	/** * 读取pdf中文字信息(全部)
	 */
	public static void READPDF(String inputFile){
		// String pdfFile = "C:/Users/Administrator/Desktop/01111.pdf";
		//创建文档对象
		PDDocument doc =null;
		String content="";
		try {
			//加载一个pdf对象
			doc =PDDocument.load(new File(inputFile));
			//获取一个PDFTextStripper文本剥离对象  
			PDFTextStripper textStripper = new PDFTextStripper();
			content = textStripper.getText(doc);
			//  PDFTextStripper textStripper =new PDFTextStripper("GBK");
			// content=textStripper.getText(doc);
			// vo.setContent(content);
			System.out.println("内容:"+content);
			System.out.println("全部页数"+doc.getNumberOfPages());  
			//关闭文档
			doc.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}


	/***
	 * 创建1到多个空白页面
	 * @param file
	 * @throws IOException
	 * @throws COSVisitorException
	 */
	public static void createBlank( String outputFile ) throws Exception {
		//首先创建pdf文档类
		PDDocument document = null;
		try{
			document = new PDDocument();
			//实例化pdf页对象
			PDPage blankPage = new PDPage();
			PDPage blankPage1 = new PDPage();
			PDPage blankPage2 = new PDPage();
			//插入文档类
			document.addPage( blankPage );
			document.addPage( blankPage1 );
			document.addPage( blankPage2 );
			//记得一定要写保存路径,如"H:\\text.pdf"
			document.save( outputFile );
			System.out.println("over");
		}
		finally
		{
			if( document != null )
			{
				document.close();
			}
		}
	}
}