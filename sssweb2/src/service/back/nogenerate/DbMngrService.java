package service.back.nogenerate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import model.generate.DbInfo;
import model.vo.MessageResult;
import model.vo.PageResult;
import utils.ExceptionUtils;
import utils.LoggerUtils;
import utils.Page;
import utils.StringUtils;
import work.codegenerate.base.DatabaseInfo;
import work.codegenerate.base.DatabaseUtils;
import work.codegenerate.base.DatabaseInfo.DatabaseType;
import work.codegenerate.base.model.ColumnInfo;
import work.codegenerate.base.model.TableInfo;
import dao.generate.DbInfoGenDao;

public class DbMngrService {
	
	private DatabaseInfo dbInfo = null;
	public DbMngrService(String dbInfoName){
		List<DbInfo> list = new DbInfoGenDao().findCollectionByConditionNoPage(" and name=?", new Object[]{dbInfoName}, null);
		if(list.size()==0){
			throw new RuntimeException("没有查询到指定名称的数据库信息:"+dbInfoName);
		}
		DbInfo db = list.get(0);
		dbInfo = new DatabaseInfo(DatabaseType.valueOf(db.getType()), db.getIp(), db.getPort().toString(), db.getDbname(), db.getUsername(), db.getPassword());
	}
	
	/**
	 * 获得数据库的所有表名
	 * @author likaihao
	 * @date 2016年9月9日 下午3:02:03
	 * @param dbInfoName
	 * @return
	 */
	public List<String> getAllTableName(){
		return DatabaseUtils.getAllTableName(dbInfo);
	}
	
	/**
	 * 查询表信息
	 * @author likaihao
	 * @date 2016年9月9日 下午3:52:26
	 * @param tableName
	 * @return
	 */
	public TableInfo getTableInfo(String tableName){
		return DatabaseUtils.getTableInfoByDatabase(dbInfo, tableName);
	}

	/**
	 * 查询
	 * @author likaihao
	 * @date 2016年9月9日 下午3:52:42
	 * @return
	 */
	public PageResult<Map<String,Object>> query(String tableName, Map<String,Object> paramMap, Integer pageNum, Integer pageSize){
		if(pageNum==null){
			pageNum = 1;
		}
		if(pageSize==null){
			pageSize = 10;
		}
		
		//查询列对应的类型
		Map<String,ColumnInfo> columnInfoMap = getTableInfo(tableName).getColumnInfoMap();
		
		//获取查询参数(以key开头的参数)
		String condition = " where 1=1";
		for(String key : paramMap.keySet()){
			if(key.startsWith("key")){
				String name = paramMap.get(key).toString();
				if(name.length()>0){
					String type = columnInfoMap.get(name).getJavaType();
					if(type.equals("String")){
						//字符串
						String value = (String)paramMap.get(name);
						if(value!=null && value.toString().length()>0){
							condition += " and "+name+" like '%"+value+"%'";
						}
					}else if(type.equals("Integer") || type.equals("Long")){
						//数字
						String value = (String) paramMap.get(name);
						if(value!=null && value.length()>0){
							condition += " and "+name+" = "+new Long(value);
						}
					}else if(type.equals("Date")){
						//日期
						String value_start = (String)paramMap.get(name+"_start");
						if(value_start!=null && value_start.length()>0){
							condition += " and date("+name+") >= date('"+value_start+"')";
						}
						String value_end = (String)paramMap.get(name+"_end");
						if(value_end!=null && value_start.length()>0){
							condition += " and date("+name+") <= date('"+value_end+"')";
						}
					}else{
						throw new RuntimeException("未处理的java类型:"+type);
					}
				}
					
			}
		}
		
		//查询总数据数
		String countSql = "select count(*) from "+tableName+" "+condition;
		DatabaseUtils util = new DatabaseUtils(dbInfo);
		Long count = (Long) util.query(countSql).get(0)[0];
		
		//查询数据
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(pageNum, pageSize, count.intValue());
		String sql = "select * from "+tableName+" "+condition+" limit "+page.getStartIndex()+","+page.getPageSize();
		List<Map<String,Object>> list = util.queryReturnMap(sql);
		page.setData(list);
		return new PageResult<Map<String,Object>>(page);
	}
	
	public MessageResult save(String tableName, String operateType, String[] showColumnArr, HttpServletRequest request){
		try {
			//查询列名
			TableInfo tableInfo = getTableInfo(tableName);
			//Set<String> columnNameSet = tableInfo.getColumnInfoMap().keySet();
			
			//获取值
			Map<String,String> valueMap = new LinkedHashMap<String,String>();
			for(String columnName : showColumnArr){
				String value = request.getParameter(columnName);
				if(value!=null && value.length()>0){
					valueMap.put(columnName, "'"+value+"'");
				}else{
					valueMap.put(columnName, null);
				}
				
			}
			
			//拼凑sql
			String sql = null;
			String primaryKeyValue = valueMap.get(tableInfo.getPrimaryKeyName());
			if(operateType.equals("update")){
				//修改
				//update `tableName` set `key1`=value1,`key2`=value2 where `id`=id
				List<String> list = new ArrayList<String>();
				for(String columnName : showColumnArr){
					if(!columnName.equals(tableInfo.getPrimaryKeyName())){
						list.add("`"+columnName+"`="+valueMap.get(columnName));
					}
				}
				sql = "update `"+tableInfo.getTableName()+"` set "+StringUtils.join(list,",")+" where `"+tableInfo.getPrimaryKeyName()+"`="+primaryKeyValue;
			}else if(operateType.equals("add")){
				//添加
				//insert into `tableName` (`key1`,`key2`) values (value1,value2);
				String keyStr = "`"+StringUtils.join(valueMap.keySet(),"`,`")+"`";
				String valueStr = StringUtils.join(valueMap.values(),",");
				sql = "insert into `"+tableName+"` ("+keyStr+") values ("+valueStr+")";
			}else{
				throw new RuntimeException("未知的类型:"+operateType);
			}
			LoggerUtils.info("执行sql:"+sql);
			DatabaseUtils util = new DatabaseUtils(dbInfo);
			util.executeUpdateSql(sql);
			
			MessageResult result = new MessageResult(true, "操作成功");
			return result;
		} catch (Exception e) {
			LoggerUtils.error("保存失败,tableName:"+tableName,e);
			MessageResult result = new MessageResult(false, "操作失败,"+ExceptionUtils.getErrMsg(e));
			return result;
		}
	}
	
	public MessageResult delete(String tableName, String ids){
		try {
			if(ids==null || ids.length()==0){
				MessageResult result = new MessageResult(false, "操作失败,id不能为空");
				return result;
			}
			//查询主键名
			TableInfo tableInfo = getTableInfo(tableName);
			String primaryKeyName = tableInfo.getPrimaryKeyName();
			
			//拼凑sql
			ids = "'"+StringUtils.join(ids.split(","), "','")+"'";
			String sql = "delete from "+tableName+" where "+primaryKeyName+" in ("+ids+")";
			
			LoggerUtils.info("执行sql:"+sql);
			DatabaseUtils util = new DatabaseUtils(dbInfo);
			util.executeUpdateSql(sql);
			
			MessageResult result = new MessageResult(true, "操作成功");
			return result;
		} catch (Exception e) {
			LoggerUtils.error("删除失败,tableName:"+tableName,e);
			MessageResult result = new MessageResult(false, "操作失败,"+ExceptionUtils.getErrMsg(e));
			return result;
		}
	}
}