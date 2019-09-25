package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.generate.DbCodeGenerateTemplet;
import model.generate.DbInfo;
import service.back.nogenerate.DbMngrService;
import service.generate.DbInfoGenService;
import utils.ListUtils;
import utils.TempletUtils;
import work.codegenerate.base.DatabaseInfo;
import work.codegenerate.base.DatabaseUtils;
import work.codegenerate.base.DatabaseInfo.DatabaseType;
import work.codegenerate.base.model.TableInfo;
import dao.generate.DbCodeGenerateTempletGenDao;
import dao.generate.DbInfoGenDao;


public class DbCodeGenerateService {
	
	DbInfoGenService dbInfoGenService = new DbInfoGenService();
	DbCodeGenerateTempletGenDao dbCodeGenerateTempletGenDao = new DbCodeGenerateTempletGenDao();
	DbInfoGenDao dbInfoGenDao = new DbInfoGenDao();
	
	//查询所有数据库连接信息
	public List<DbInfo> queryAllDbInfo(){
		List<DbInfo> list = dbInfoGenService.findAll();
		ListUtils.clearListValAttr(list, new String[]{"id","name"});
		return list;
	}
	
	//查询所有列名信息
	public Set<String> queryAllColumnName(String dbInfoName, String tableName){
		TableInfo tableInfo = new DbMngrService(dbInfoName).getTableInfo(tableName); 
		Set<String> set = tableInfo.getColumnInfoMap().keySet();
		return set;
	}
	
	//通过type查询模板
	public List<DbCodeGenerateTemplet> queryTempletByType(String type){
		List<DbCodeGenerateTemplet> list = dbCodeGenerateTempletGenDao.findCollectionByConditionNoPage(" and type=?", new Object[]{type}, null);
		return list;
	}
	
	//生成代码
	public String generation(String dbInfoName, String tableName, String columnNameArr, String templet, String paramStr){
		Map<String,Object> paramMap = TempletUtils.getBaseParamMap();
		
		//如果数据库配置不为空,则添加tableName和tableInfo
		if(dbInfoName!=null && dbInfoName.length()>0){
			//查询数据库连接信息
			DbInfo dbInfo = dbInfoGenDao.findCollectionByConditionNoPage(" and name=?", new Object[]{dbInfoName}, null).get(0);
			//查询表相关信息
			DatabaseInfo databaseInfo = new DatabaseInfo(DatabaseType.mysql,dbInfo.getIp(),dbInfo.getPort()+"",dbInfo.getDbname(),dbInfo.getUsername(),dbInfo.getPassword());
			TableInfo tableInfo = DatabaseUtils.getTableInfoByDatabase(databaseInfo, tableName);
			//添加tableName 和 tableInfo
			paramMap.put("tableName", tableName);
			paramMap.put("tableInfo", tableInfo);
			
			//如果列不为空,则添加columnNameList
			if(columnNameArr!=null && columnNameArr.length()>0){
				paramMap.put("columnNameList", new ArrayList<String>(Arrays.asList(columnNameArr.split(","))));
			}
		}
		
		//添加页面传递的参数
		paramMap.putAll(TempletUtils.getParamMap(paramStr,paramMap,null));
		
		//替换模板
		templet = templet.replace("　　", "\t");
		String result = TempletUtils.templetFillMultiLine(templet, paramMap);
		return result;
	}
	
}