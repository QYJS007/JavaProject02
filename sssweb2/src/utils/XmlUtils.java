package utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {
	
	
	/**
	 * 将xml的某个唯一节点转变为map
	 * @author likaihao
	 * @date 2015年9月30日 下午3:09:44
	 * @param in
	 * @param singleNodeName
	 * @return
	 */
	public static Map<String,String> singNodeToMap(ByteArrayInputStream in,String singleNodeName){
		Map<String,String> map = new HashMap<String,String>();
		try {
			in.reset();
			SAXReader saxReader=new SAXReader();
			Document document=saxReader.read(in);
			Element baseElement = null;
			if(singleNodeName==null || singleNodeName.length()==0){
				//获取根元素
				baseElement = document.getRootElement();
			}else{
				baseElement = (Element) document.selectSingleNode("//"+singleNodeName);
			}
			
			// 获得所有元素
			List<?> allChildElements = baseElement.elements();
			for (Object o : allChildElements) {
				Element element = (Element) o;
				String name = element.getName();// 获取元素名称
				String text = element.getTextTrim();// 获取元素文本
				map.put(name, text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 将xml的某个唯一节点转变为map
	 * @author likaihao
	 * @date 2015年9月30日 下午3:09:44
	 * @param in
	 * @param singleNodeName
	 * @return
	 */
	public static Map<String,String> singNodeToMap(String str,String encoding,String singleNodeName){
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(encoding));
			return singNodeToMap(in,singleNodeName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 将xml的根节点转变为map
	 * @author likaihao
	 * @date 2015年9月30日 下午3:09:44
	 * @param in
	 * @return
	 */
	public static Map<String,String> baseNodeToMap(ByteArrayInputStream in){
		return singNodeToMap(in, null);
	}
	
	/**
	 * 将xml的根节点转变为map
	 * @author likaihao
	 * @date 2015年9月30日 下午3:09:44
	 * @param in
	 * @return
	 */
	public static Map<String,String> baseNodeToMap(String str,String encoding){
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(encoding));
			return baseNodeToMap(in);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	/**
	 * 格式化XML文件
	 * @param xml
	 * @return
	 */
	public static String getFormatXml(String xml) {
		try {
			SAXReader saxReader = new SAXReader();
			
			// 设置不验证dtd文件
			saxReader.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId)
						throws SAXException, IOException {
					return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes())); 
				}
			});
			
			Document document = saxReader.read(new ByteArrayInputStream(xml.getBytes()));
			// 创建输出格式
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 制定输出xml的编码类型
			format.setEncoding("utf-8");
			
			StringWriter writer = new StringWriter();
			// 创建一个文件输出流
			XMLWriter xmlwriter = new XMLWriter(writer, format);
			// 将格式化后的xml串写入到文件
			xmlwriter.write(document);
			String returnValue = writer.toString();
			writer.close();

			// 返回编译后的字符串格式
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}