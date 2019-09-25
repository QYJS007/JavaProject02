package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableRow;

import service.model.Field;
import service.model.Inter;
import service.model.Model;
import service.model.NoMatchTableHeaderException;
import service.model.WordTitle;
import sys.SystemConf;
import utils.JetbrickTemplateUtils;
import utils.LoggerUtils;
import utils.RegexUtils;
import utils.SSHUtils;
import utils.StringUtils;

public class WordService {
	
	/**
	 * 获得word对象
	 * @author likaihao
	 * @date 2017年1月17日 下午2:52:44
	 * @param wordPath
	 * @return
	 */
	public HWPFDocument getDoc(String wordPath){
		HWPFDocument doc = null;
		try {
			if(wordPath.startsWith("svn:")){
				//从svn上下载最新文件
				String fileSavePath = SystemConf.getProjectPath()+"/WEB-INF/temp/"+StringUtils.getRandomStr()+".doc";
				//拼凑命令
				ParamsService ps = new ParamsService();
				String command = null;
				if(System.getProperty("os.name").toLowerCase().startsWith("win")){
					command = ps.findParamsByName("svn_export_window_command").getValue();
				}else{
					command = ps.findParamsByName("svn_export_linux_command").getValue();
				}
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("fileSvnPath", wordPath);
				paramMap.put("fileSavePath", fileSavePath);
				paramMap.put("svn_username", ps.findParamsByName("svn_username").getValue());
				paramMap.put("svn_password", ps.findParamsByName("svn_password").getValue());
				command = JetbrickTemplateUtils.render(command, paramMap);
				//执行命令,下载文件
				LoggerUtils.info("执行svn命令:"+command);
				String result = SSHUtils.exec(command,"gbk");
				if(!result.contains("完成导出") && !result.contains("Export complete")){
					throw new RuntimeException("导出文件失败,命令:"+command+",返回值:"+result);
				}
				//读取文件
				InputStream in = new FileInputStream(fileSavePath);
				doc = new HWPFDocument(in);
				//删除文件
				new File(fileSavePath).delete();
			}else{
				//读取本地文件
				InputStream in = new FileInputStream(wordPath);
				doc = new HWPFDocument(in);
			}
		} catch (Exception e) {
			throw new RuntimeException("读取文件失败,路径:"+wordPath,e);
		}
		return doc;
	}
	
	/**
	 * 获取word中标题的信息
	 * @author likaihao
	 * @datetime 2017年2月28日 上午11:29:11
	 * @param doc 文档
	 * @param titleIndexArr 允许的标题编号
	 * @return
	 */
	public Map<String,WordTitle> getWordTitleMap(String wordPath){
		HWPFDocument doc = getDoc(wordPath);
		
		Map<String, WordTitle> wordTitleMap = new LinkedHashMap<String, WordTitle>();
		
		//准备获取的标题样式(去掉空格后),如标题 2写为标题2
		List<String> titleList = Arrays.asList("标题1", "标题2", "标题3", "标题4");
		List<Integer> styleValueList = new ArrayList<Integer>();
		for(int i=0;i<titleList.size();i++){
			styleValueList.add(0);
		}
		
		Range range = doc.getRange();
		int allParagraphNum = range.numParagraphs();
		WordTitle lastWordTitle = null;
		for (int i = 0; i < allParagraphNum; i++) {
			Paragraph paragraph = range.getParagraph(i);
			
			//如果不是标题则跳过
			if(!paragraph.isInList()){
				continue;
			}
			
			String styleName = doc.getStyleSheet().getStyleDescription(paragraph.getStyleIndex()).getName();
			styleName = styleName.replace(" ", "");//去除空格,如标题 2写为标题2
			
			//如果遇到符合条件的样式
			if(titleList.contains(styleName)){
				//计算标题序号
				//碰见标题,同级值+1,下级清0
				int index = titleList.indexOf(styleName);
				styleValueList.set(index, styleValueList.get(index)+1);
				if(index+1 < titleList.size()){
					styleValueList.set(index+1, 0);
				}
				StringBuilder builder = new StringBuilder();
				for(Integer value : styleValueList){
					if(value!=0){
						if(builder.length()>0){
							builder.append(".");
						}
						builder.append(value);
					}
				}
				String titleIndex = builder.toString();
				
				//如果在标题在表格中,只增加序号,不获取内容
				if(!paragraph.isInTable()){
					//封装wordTitle对象
					WordTitle wordTitle = new WordTitle();
					wordTitle.setDoc(doc);
					wordTitle.setTitleIndex(titleIndex);
					wordTitle.setTitleName(getParagraphStr(paragraph));
					wordTitle.setStartParagraphIndex(i);
					if(lastWordTitle!=null){
						lastWordTitle.setEndParagraphIndex(i-1);
					}
					if(wordTitle.getTitleName().endsWith("\r")){
						wordTitle.setTitleName(wordTitle.getTitleName().substring(0,wordTitle.getTitleName().length()-1));
					}
					String name = wordTitle.getTitleIndex() + " " + wordTitle.getTitleName();
					wordTitleMap.put(name, wordTitle);
					
					lastWordTitle = wordTitle;
				}
			}
		}
		if(lastWordTitle!=null){
			lastWordTitle.setEndParagraphIndex(allParagraphNum-1);
		}
		return wordTitleMap;
	}
	
	/**
	 * 打印文档中所有样式的代码和名称
	 * @author likaihao
	 * @datetime 2017年2月28日 上午11:24:07
	 * @param doc
	 */
	public void printAllStyleIndexAndName(HWPFDocument doc){
		Map<Short,String> styleMap = new HashMap<Short,String>();
		StyleSheet style_sheet = doc.getStyleSheet();
		Range range = doc.getRange();
		for (int i = 0; i < range.numParagraphs(); i++) {
			Paragraph paragraph = range.getParagraph(i);
			short styleIndex = paragraph.getStyleIndex();
			String styleName = style_sheet.getStyleDescription(styleIndex).getName();
			styleMap.put(styleIndex, styleName);
		}
		System.out.println(styleMap);
	}
	
	/**
	 * 从word中获取接口信息(word接口生成)
	 * @author likaihao
	 * @datetime 2017年2月28日 下午3:30:42
	 * @param doc
	 * @param wordTitle
	 * @return
	 */
	public Inter getInterByWordTitle(WordTitle wordTitle){
		String interName = wordTitle.getTitleIndex() + " " + wordTitle.getTitleName();
		Range range = wordTitle.getDoc().getRange();
		
		//读取word内容
		String url = null;
		String method = null;
		Map<String,Table> tableMap = new LinkedHashMap<String,Table>();
		int lastTableEndOffset = -1;
		for(int i=wordTitle.getStartParagraphIndex(); i <= wordTitle.getEndParagraphIndex(); i++){
			Paragraph paragraph = range.getParagraph(i);
			String text = getParagraphStr(paragraph);
			
			//获取接口内容
			if(text.startsWith("请求路由：")){
				url = text.replace("请求路由：", "");
			}else if(text.startsWith("HTTP请求类型：")){
				method = text.replace("HTTP请求类型：", "");
			}else if(paragraph.isInTable()){
				//获取表格, 如果单元格在上一个表格内,则不获取
				if(paragraph.getEndOffset()>lastTableEndOffset){
					//获取描述
					String tableComment = getParagraphStr(range.getParagraph(i-1));
					//查看上3个段落内是否存在 返回内容字样,如果有,则将描述替换为返回内容
					if(getParagraphStr(range.getParagraph(i-2)).equals("返回内容") || getParagraphStr(range.getParagraph(i-3)).equals("返回内容") || getParagraphStr(range.getParagraph(i-4)).equals("返回内容")){
						tableComment = "返回内容";
					}
					//如果描述是空行,则向上取一行
					if(tableComment.trim().length()==0){
						tableComment = getParagraphStr(range.getParagraph(i-2));
					}
					
					Table table = range.getTable(paragraph);
					if(!tableComment.equals("返回失败")){//去除表格描述为 返回失败 的表格
						tableMap.put(tableComment,table);
					}
					lastTableEndOffset = table.getEndOffset();
				}
			}
		}
		
		if(tableMap.size()==0){
			//不是接口
			return null;
		}
		
		//解析word表格为model
		Model requestModel = null;
		Model responseModel = null;
		List<Model> modelList = new ArrayList<Model>();
		
		for(String tableComment : tableMap.keySet()){
			try {
				Table table = tableMap.get(tableComment);
				
				//解析字段
				List<Field> fieldList = getFieldListByTable(table);
				if(fieldList.size()==0){
					//忽略空表格
					continue;
				}
				
				Model model = new Model();
				model.setComment(tableComment);
				model.setFieldList(fieldList);
				//尝试从描述中获得modelName
				String modelName = "Abc";
				if(tableComment.contains("请求内容") || tableComment.contains("请求参数列表")){
					modelName = "Request";
					model.setName(modelName);
					requestModel = model;
				}else if(tableComment.contains("返回内容")){
					modelName = "Response";
					model.setName(modelName);
					responseModel = model;
					modelList.add(model);
				}else if(tableComment.matches(".*?\\w+.*?")){
					modelName = RegexUtils.getSubstrByRegex(tableComment, "(\\w+)");
					model.setName(modelName);
					modelList.add(model);
				}
			} catch (NoMatchTableHeaderException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException("解析接口失败,接口:"+interName+",表格:"+tableComment,e);
			}
		}
		
		//封装接口对象
		Inter inter = new Inter();
		inter.setUrl(url);
		inter.setMethod(method);
		inter.setName(interName);
		inter.setRequestModel(requestModel);
		inter.setResponseModel(responseModel);
		inter.setModelList(modelList);
		
		if(inter.getRequestModel()==null){
			inter.setRequestModel(new Model());
		}
		if(inter.getResponseModel()==null){
			inter.setResponseModel(new Model());
		}
		return inter;
	}
	
	/**
	 * 从表格中获取model信息(wordSql生成)
	 * @author likaihao
	 * @datetime 2017年3月9日 下午7:58:10
	 * @param doc
	 * @param wordTitle
	 * @return
	 */
	public Model getTableModelByTable(WordTitle wordTitle){
		String tableComment = wordTitle.getTitleIndex() + " " + wordTitle.getTitleName();
		Range range = wordTitle.getDoc().getRange();
		
		try {
			//获取表格
			Table table = null;
			for(int i=wordTitle.getStartParagraphIndex(); i <= wordTitle.getEndParagraphIndex(); i++){
				Paragraph paragraph = range.getParagraph(i);
				if(paragraph.isInTable()){
					table = range.getTable(paragraph);
					break;
				}
			}
			if(table==null){
				return null;
			}
			
			//如果第一行第一列不是表名,则跳过
			if(!getCellStr(table.getRow(0), 0).equals("表名")){
				return null;
			}
			
			//解析表名,主键,索引
			String tableName = null;
			String primaryKey = null;
			String indexStr = null;
			for(int i=0;i<table.numRows();i++){
				TableRow row = table.getRow(i);
				if(row.numCells()!=2){
					continue;
				}
				
				String key = getCellStr(row, 0);
				String value = getCellStr(row, 1).toLowerCase();
				if(key.equals("表名")){
					if(value.matches(".*?\\w+.*?")){
						tableName = RegexUtils.getSubstrByRegex(value, "(\\w+)");
					}
				}else if(key.equals("主键")){
					if(value.length()>0){
						primaryKey = value;
					}
				}else if(key.equals("索引字段")){
					if(value.length()>0){
						indexStr = value;
					}
				}
			}
			//如果表名为空,尝试从表头获取
			if(tableName==null && wordTitle.getTitleName().matches(".*?\\w+.*?")){
				tableName = RegexUtils.getSubstrByRegex(wordTitle.getTitleName(), "(\\w+)");
			}
			if(tableName==null){
				throw new RuntimeException("获取表名失败");
			}
			
			//解析字段
			List<Field> fieldList = getFieldListByTable(table);
			if(fieldList.size()==0){
				throw new RuntimeException("字段为空");
			}
			
			//将主键填充到字段信息中
			if(primaryKey==null){
				throw new RuntimeException("没有指定主键");
			}
			boolean flag = false;
			for(Field field : fieldList){
				if(field.getName().equalsIgnoreCase(primaryKey)){
					field.setPrimaryKey(true);
					flag = true;
					break;
				}
			}
			if(flag==false){
				//没有匹配到
				throw new RuntimeException("主键在字段中没有找到:"+primaryKey);
			}
			
			//解析索引(如果仅有一个字段则解析,如果多个则直接展示)
			List<String> indexList = null;
			if(indexStr!=null && indexStr.matches("\\w+")){
				indexList = new ArrayList<String>();
				indexList.add(indexStr);
			}
			if(indexList!=null && indexList.size()>0){
				for(String name : indexList){
					boolean flag2 = false;
					for(Field field : fieldList){
						if(field.getName().equalsIgnoreCase(name)){
							flag2 = true;
							break;
						}
					}
					if(flag2==false){
						//没有匹配到
						throw new RuntimeException("索引字段在字段中没有找到:"+name+","+tableComment);
					}
				}
			}
			
			//校验字段类型
			List<String> fieldTypeList = Arrays.asList("varchar","char","text","mediumtext","int","tinyint","bigint","number","number","decimal","datetime","date","time","timestamp","bit","double","longblob");
			for(Field field : fieldList){
				String type = field.getType();
				//修改常用出错字段类型
				if(!fieldTypeList.contains(type.split("\\(")[0].trim())){
					//type = "未识别的字段类型:"+type;
					throw new RuntimeException("未识别的字段类型:"+type+",tableComment:"+tableComment+",fieldName:"+field.getName());
				}
				field.setType(type);
			}
			
			//组装model
			Model model = new Model();
			model.setName(tableName);
			model.setComment(tableComment);
			model.setFieldList(fieldList);
			model.setIndexStr(indexStr);
			if(indexList!=null && indexList.size()>0){
				model.setIndexList(indexList);
			}
			return model;
		} catch (Exception e) {
			throw new RuntimeException("解析表格异常:"+tableComment,e);
		}
	}
	
	/**
	 * 从表格中解析字段
	 * @author likaihao
	 * @datetime 2017年2月28日 下午4:04:43
	 * @param table
	 * @return
	 */
	protected List<Field> getFieldListByTable(Table table){
		List<Field> fieldList = new ArrayList<Field>();
		int rowNum = table.numRows();
		if(rowNum>1){
			int i=0;
			try {
				String tableHeader = null;
				for(i=0;i<rowNum;i++){
					//跳过列数小于3的行
					TableRow row = table.getRow(i);
					if(row.numCells()<3){
						continue;
					}
					//第一行为表头,后续行取字段
					if(tableHeader==null){
						tableHeader = table.getRow(i).text();
					}else{
						Field field = getFiledByRow(tableHeader, row);
						if(field.getName().trim().length()>0 && !field.getName().trim().equalsIgnoreCase("ismock")){//不是空行且不是ismock
							fieldList.add(field);
						}
					}
				}
			} catch (NoMatchTableHeaderException e) {
				throw e;
			} catch (Exception e) {
				//System.out.println("解析表格异常,"+e.getMessage());
				throw new RuntimeException("解析表格异常,行号:"+i,e);
			}
		}
		return fieldList;
	}
	
	/**
	 * 从行中获取字段信息
	 * @author likaihao
	 * @datetime 2017年3月8日 上午9:29:00
	 * @param tableHeader
	 * @param row
	 * @return
	 */
	protected Field getFiledByRow(String tableHeader, TableRow row){
		Field field = new Field();
		
		//word接口生成
		if(tableHeader.equals("参数数据类型可选说明") || tableHeader.equals("名称数据类型必填说明") || tableHeader.equals("参数数据类型必填说明")){
			field.setName(getCellStr(row, 0));
			field.setComment(getCellStr(row, 3));
			field.setType(getCellStr(row, 1));
		}else if(tableHeader.equals("参数参数说明数据类型必填示例") || tableHeader.equals("字段名参数说明数据类型是否为空示例")){
			field.setName(getCellStr(row, 0));
			field.setComment(getCellStr(row, 1));
			field.setType(getCellStr(row, 2));
		}else if(tableHeader.equals("名称数据类型长度必填说明")){
			field.setName(getCellStr(row, 0));
			field.setComment(getCellStr(row, 4));
			field.setType(getCellStr(row, 1));
		}else if(tableHeader.equals("参数参数名称类型参数说明是否可选样例") || tableHeader.equals("参数参数名称类型（字节长度）参数说明是否可选样例") || tableHeader.equals("参数参数名称类型（字节长度）参数说明是否可为空样例")){
			field.setName(getCellStr(row, 0));
			field.setComment(getCellStr(row, 1)+","+getCellStr(row, 3));
			field.setType(getCellStr(row, 2));
		}
		
		//wordSql生成
		else if(tableHeader.equals("字段说明字段名称数据类型（精度范围）允许为空Y/N唯一Y/N备注")){
			field.setName(getCellStr(row, 1).toLowerCase());
			field.setComment(getCellStr(row, 0).toLowerCase());
			field.setType(getCellStr(row, 2).toLowerCase());
			
			//设置不可为空
			String notNull = getCellStr(row, 3).replaceAll("[\\s ]", "");
			if(!notNull.equalsIgnoreCase("y") && !notNull.equalsIgnoreCase("n") && !notNull.equalsIgnoreCase("")){
				throw new RuntimeException("未识别的boolean值:"+notNull+",字段名称:"+field.getName());
			}else{
				field.setNotNull(notNull.equalsIgnoreCase("n"));
			}
			
			//设置唯一
			String unique = getCellStr(row, 4).replaceAll("[\\s ]", "");
			if(!unique.equalsIgnoreCase("y") && !unique.equalsIgnoreCase("n") && !unique.equalsIgnoreCase("")){
				throw new RuntimeException("未识别的boolean值:"+unique+",字段名称:"+field.getName());
			}else{
				field.setUnique(unique.equalsIgnoreCase("y"));
			}
		}
		
		//其它
		else{
			throw new NoMatchTableHeaderException("没有匹配到表头:"+tableHeader);
		}
		return field;
	}
	
	/**
	 * 获得段落内容,并去除最后的换行
	 * @author likaihao
	 * @datetime 2017年2月28日 下午3:24:06
	 * @param paragraph
	 * @return
	 */
	protected String getParagraphStr(Paragraph paragraph){
		String content = paragraph.text();
		if(content.endsWith("\r")){
			content = content.substring(0,content.length()-1);
		}
		if(content.endsWith("")){
			content = content.substring(0,content.length()-1);
		}
		return content.trim();
	}
	
	/**
	 * 获得单元格内容,并去除最后的特殊字符
	 * @author likaihao
	 * @datetime 2017年2月28日 下午3:25:38
	 * @param row
	 * @param cellIndex
	 * @return
	 */
	protected String getCellStr(TableRow row, int cellIndex){
		String content = row.getCell(cellIndex).text();
		if(content.endsWith("")){
			content = content.substring(0,content.length()-1);
		}
		//将单元格内的换行替换为空格
		content = content.replace("\r", " ");
		return content.trim();
	}
	
	public static void main(String[] args) throws Exception {
		WordService service = new WordService();
		
		String wordPath = "";
		
		//打印文档中所有样式的代码和名称
		//service.printAllStyleIndexAndName(doc);
		
		//获取word标题信息
		Map<String, WordTitle> wordTitleMap = service.getWordTitleMap(wordPath);
		
		/*******************wordSql生成************************/
		for(String name : wordTitleMap.keySet()){
			WordTitle wordTitle = wordTitleMap.get(name);
			//解析word内容,获得接口信息
			Model model = service.getTableModelByTable(wordTitle);
			System.out.println(model);
		}
		
//		String name = "5.2.1 会员信息表(users)";
//		WordTitle wordTitle = wordTitleMap.get(name);
//		//解析word内容,获得接口信息
//		Model model = service.getTableModelByTable(wordTitle);
//		System.out.println(model);
		/*******************word接口生成************************/
		//全部打印
//		for(String name : wordTitleMap.keySet()){
//			WordTitle wordTitle = wordTitleMap.get(name);
//			//解析word内容,获得接口信息
//			Inter inter = service.getInterByWordTitle(wordTitle);
//			System.out.println(inter);
//			
//			System.out.println("\r\n\r\n----------------------------\r\n\r\n");
//		}
		
		//单个打印
//		String name = "3.1.18 查询班次模板";
//		WordTitle wordTitle = wordTitleMap.get(name);
//		//解析word内容,获得接口信息
//		Inter inter = service.getInterByWordTitle(wordTitle);
//		System.out.println(inter);
	}
}
