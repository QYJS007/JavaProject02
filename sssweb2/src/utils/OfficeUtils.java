package utils;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class OfficeUtils {
	
	/**
	 * 读取word文件
	 * @param file
	 * @return
	 */
	public static String readWord(File file) {
		try {
			if(!file.exists()){
				throw new RuntimeException("文件不存在!");
			}
			String fileName = file.getName();
			if(!fileName.endsWith("doc") && !fileName.endsWith("docx")){
				throw new RuntimeException("文件不是word文件!");
			}
			if(fileName.startsWith("~$") || file.length()<1024*5){
				//临时文件
				return "";
			}
			FileInputStream fis = new FileInputStream(file);
			String text = null;
			if(fileName.endsWith("doc")){
				HWPFDocument doc = new HWPFDocument(fis);
				Range rang = doc.getRange();
				text = rang.text();
			}else{
				XWPFDocument doc = new XWPFDocument(fis);  
				XWPFWordExtractor extractor = new XWPFWordExtractor(doc);  
				text = extractor.getText(); 
			}
			fis.close();
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 读取word文件,返回list
	 * @param file
	 * @return
	 */
	public static List<String> readWordReturnList(File file) {
		String content = readWord(file);
		List<String> list = Arrays.asList(content.split("[\r\n]+"));
		return new ArrayList<String>(list);
	}
	
	/**
	 * 读取Excel返回String
	 * @param file 文件
	 * @param dateFormat 输出的日期格式
	 * @param columnSplitStr 列分隔符
	 * @return
	 */
	public static String readExcelReturnString(File file,String dateFormat,String columnSplitStr){
		List<String> list = readExcelReturnList(file, dateFormat, columnSplitStr);
		return StringUtils.join(list, "\r\n");
	}
	
	/**
	 * 读取Excel返回list
	 * @param file 文件
	 * @param dateFormat 输出的日期格式
	 * @param columnSplitStr 列分隔符
	 * @return
	 */
	public static List<String> readExcelReturnList(File file,String dateFormat,String columnSplitStr){
		List<String> list = new ArrayList<String>();
		Map<String,ArrayList<ArrayList<String>>> map = readExcel(file,"yyyy-MM-dd");
		for(String sheetName : map.keySet()){
			ArrayList<ArrayList<String>> dataList = map.get(sheetName);
			for(ArrayList<String> columnList : dataList){
				list.add(StringUtils.join(columnList, columnSplitStr));
			}
		}
		return list;
	}
	
	/**
	 * 获取Excel的数据
	 * @param file 文件
	 * @param dateFormat 输出的日期格式
	 * @return
	 */
	public static Map<String,ArrayList<ArrayList<String>>> readExcel(File file,String dateFormat){
		try {
			Workbook wb = WorkbookFactory.create(new FileInputStream(file));
			Map<String,ArrayList<ArrayList<String>>> map = new LinkedHashMap<String,ArrayList<ArrayList<String>>>();
			for(int i=0;i<wb.getNumberOfSheets();i++){
				map.put(wb.getSheetName(i), readSheetData(wb,i,dateFormat));
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取指定sheet的数据
	 * @param wb
	 * @param sheetIndex 工作簿索引
	 * @param dateFormat 输出的日期格式
	 * @return
	 */
	private static ArrayList<ArrayList<String>> readSheetData(Workbook wb,int sheetIndex,String dateFormat) {
		ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Sheet sheet = wb.getSheetAt(sheetIndex);
		
		for (int j=0;j<=sheet.getLastRowNum();j++) {
			Row row = sheet.getRow(j);
			ArrayList<String> columnList = new ArrayList<String>();
			if(row==null){
				dataList.add(columnList);
				continue;
			}
			for (int i = 0; i < row.getLastCellNum(); i++) {
				Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					columnList.add("");
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					columnList.add(cell.getBooleanCellValue()+"");
					break;
				// 数值
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						columnList.add(df.format(cell.getDateCellValue()));
					} else {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String temp = cell.getStringCellValue();
						// 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
						if (temp.indexOf(".") > -1) {
							columnList.add(String.valueOf(new Double(temp)).trim());
						} else {
							columnList.add(temp.trim());
						}
					}
					break;
				case Cell.CELL_TYPE_STRING:
					columnList.add(cell.getStringCellValue().trim());
					break;
				case Cell.CELL_TYPE_ERROR:
					columnList.add("");
					break;
				case Cell.CELL_TYPE_FORMULA:
					cell.setCellType(Cell.CELL_TYPE_STRING);
					if (cell.getStringCellValue() != null) {
						columnList.add(cell.getStringCellValue().replaceAll("#N/A", "").trim());
					}else{
						columnList.add("");
					}
					break;
				default:
					columnList.add("");
					break;
				}
			}
			dataList.add(columnList);
		}
		return dataList;
	}
	
}
