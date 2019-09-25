package work.codegenerate.base;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.IOUtils;
import utils.RegexUtils;
import utils.StringUtils;
import utils.TempletUtils;
import work.codegenerate.base.model.TableInfo;

public class CodeGenerate {
	
	private static String templetBasePath = null;//模板根路径
	
	//固定模板
	//公共参数模板
	static String pubParamTempletName = "_all_pub_param.templet";
	//表相关的参数模板
	static String tableParamTempletName = "_every_table_init_param.templet";
	
	//固定key
	//输出位置
	static String outPathKey = "out_path";
	//是否覆盖文件
	static String isReplaceKey = "isReplace";
	//跟表相关的需要生成的模板
	static String tableTempletListKey = "table_templet_list";
	//跟表无关的需要生成的模板
	static String otherTempletListKey = "other_templet_list";
	//表信息
	static String tableInfoMapKey = "tableInfoMap";
	//表名称
	static String tableNameKey = "tableName";
	
	
	
	/**
	 * 开始运行
	 * @author likaihao
	 * @date 2016年8月6日 上午11:10:27
	 * @param mainClass mian方法所在类的class对象
	 */
	@SuppressWarnings("unchecked")
	public static void run(Class<?> mainClass){
		try {
			templetBasePath = mainClass.getResource("").getPath();
			templetBasePath = StringUtils.urlDecoding(templetBasePath);
			File templetBaseDir = new File(templetBasePath);
			if(!templetBaseDir.exists()){
				throw new RuntimeException("指定路径不存在:"+templetBasePath);
			}
			
			//获取基本参数
			Map<String,Object> pubParamMap = new HashMap<String,Object>();
			// * 添加main方法所在类的对象
			pubParamMap.put(StringUtils.firstCharLowerCase(mainClass.getSimpleName()), mainClass.newInstance());
			// * 读取模板
			pubParamMap.putAll(getTempletParam(pubParamTempletName, null));
			
			// ** 获取输出路径
			String outPath = pubParamMap.get(outPathKey).toString();
			// ** 判断是否需要覆盖文件
			boolean isReplace = "true".equals(pubParamMap.get(isReplaceKey));
			if(isReplace && !outPath.equals("console")){
				System.out.println("是否确定当原文件存在时直接覆盖(yes/no)?");
				isReplace = IOUtils.readSystemIn().equals("yes");
			}
			// ** 获取跟表无关的模板
			List<String> otherTempletList = new ArrayList<String>();
			String other_templet = (String)pubParamMap.get(otherTempletListKey);
			if(other_templet.trim().length()>0){
				otherTempletList = Arrays.asList(other_templet.split(","));
			}
			// ** 获取跟表相关的模板
			List<String> tableTempletList = new ArrayList<String>();
			String table_templet_list_str = pubParamMap.get(tableTempletListKey).toString();
			if(table_templet_list_str.equals("all")){
				//获取指定路径下所有模板,去除公共模板 和 跟表无关的模板
				for(File file : templetBaseDir.listFiles()){
					if(file.getName().endsWith(".templet")){
						if(!file.getName().equals(pubParamTempletName) && !file.getName().equals(tableParamTempletName)){
							tableTempletList.add(file.getName());
						}
					}
				}
				tableTempletList.removeAll(otherTempletList);
			}else{
				tableTempletList = Arrays.asList(table_templet_list_str.split(","));
			}
			
			
			//读取数据库信息
			Map<String,TableInfo> tableInfoMap = (Map<String, TableInfo>) pubParamMap.get(tableInfoMapKey);
			
			//生成跟表相关的模板
			for(String tableName : tableInfoMap.keySet()){
				//准备参数
				Map<String,Object> paramMap = new HashMap<String,Object>();
				// * 添加基本参数
				paramMap.putAll(pubParamMap);
				// * 填充tableName
				paramMap.put(tableNameKey, tableName);
				// * 加载每个表的初始化参数
				paramMap.putAll(getTempletParam(tableParamTempletName, paramMap));
				
				//填充模板
				if(tableTempletList==null || tableTempletList.size()==0){
					throw new RuntimeException("没有要填充的模板");
				}
				for(String templetName : tableTempletList){
					try {
						Map<String,Object> paramMap2 = new HashMap<String,Object>(paramMap);
						generate(templetName,paramMap2,outPath,isReplace);
					} catch (Exception e) {
						throw new RuntimeException("渲染失败,templetName:"+templetName,e);
					}
				}
			}
			
			//生成跟表无关的模板
			for(String templetName : otherTempletList){
				Map<String,Object> paramMap2 = new HashMap<String,Object>(pubParamMap);
				generate(templetName,paramMap2,outPath,isReplace);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取模板中的参数
	 * @author likaihao
	 * @date 2016年7月16日 下午9:54:06
	 * @param templetName
	 * @return
	 */
	private static Map<String,Object> getTempletParam(String templetName, Map<String,Object> paramMap){
		String pubParamTemplet = IOUtils.readFile(CodeGenerate.templetBasePath+templetName);
		String startTagStr = "@@param:{";
		String paramStr = TempletUtils.getAllTwinTagContent(pubParamTemplet, startTagStr).get(0);
		paramStr = paramStr.substring(startTagStr.length(),paramStr.length()-1).trim().replace("\t", "");
		return TempletUtils.getParamMap(paramStr, paramMap, null);
	}
	
	
	/**
	 * 生成代码
	 * @author likaihao
	 * @date 2016年5月7日 下午12:18:31
	 * @param templetPath
	 * @param paramMap
	 * @param path 输出路径
	 * @return
	 */
	private static void generate(String templetPath,Map<String,Object> paramMap,String path, boolean isReplace){
		//获取模板
		String templet;
		try {
			templetPath = CodeGenerate.templetBasePath+StringUtils.urlDecoding(templetPath);//将{转义回来
			templet = IOUtils.readFile(templetPath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("模板未找到:"+templetPath);
		}
		
		//添加模板中的参数
		String startTagStr = "@@param:{";
		int index = templet.indexOf(startTagStr);
		if(index!=-1){
			String paramStr = TempletUtils.getAllTwinTagContent(templet, startTagStr).get(0);
			paramStr = paramStr.substring(startTagStr.length(),paramStr.length()-1).trim().replace("\t", "");
			Map<String,Object> subParamMap = TempletUtils.getParamMap(paramStr, paramMap, null);
			paramMap.putAll(subParamMap);
			templet = templet.substring(0,index);
		}
		
		//替换模板
		String content = TempletUtils.templetFillMultiLine(templet, paramMap);
		
		//获取文件名
		String templetName = new File(templetPath).getName();
		templetName = templetName.substring(0,templetName.lastIndexOf("."));
		String fileName = TempletUtils.templetFillOneLine(templetName.replace("{", "{:"), paramMap);
		
		//获取文件路径,文件名称
		String subPath = null;
		String suffix = IOUtils.getSuffix(fileName);
		if(suffix.equals("java")){
			//计算包路径
			subPath = (String) paramMap.get(suffix+"Dir") + "/" + RegexUtils.getSubstrByRegex(content, "package\\s+(.*?);").replace(".", "/");
		}else if(suffix!=null){
			//计算页面路径
			subPath = (String) paramMap.get(suffix+"Dir");
			if(subPath==null){
				subPath = "";
			}
		}
		
		//输出
		write(path,subPath,fileName,content,isReplace);
	}
	
	/**
	 * 输出
	 * @author likaihao
	 * @date 2016年5月10日 上午10:38:15
	 * @param path
	 * @param templetName
	 * @param content
	 */
	private static void write(String path, String subPath, String fileName, String content, boolean isReplace){
		try {
			if(path==null){
				return;
			}
			if(path.equals("console")){
				System.out.println("\r\n\r\n"+fileName+"\r\n-------------------------------------------------------------------------------------------------");
				System.out.println(content);
			}else{
				if(path.equals("desktop")){
					path = IOUtils.getHomeDirectoryPath()+"/project";
				}
				String filePath = null;
				if(subPath!=null && subPath.length()>0){
					filePath = path + "/" + subPath + "/" + fileName;
				}else{
					filePath = path + "/" + fileName;
				}
				File file = new File(filePath);
				if(file.exists() && !isReplace){
					System.err.println("@@@@@@文件已存在,忽略:"+file.getAbsolutePath());
				}else{
					IOUtils.writeFileReplace(file.getAbsolutePath(), content);
					System.out.println("写入"+fileName+":"+file.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}