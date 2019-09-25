package work.codegenerate.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import work.codegenerate.base.DatabaseInfo.DatabaseType;
import work.codegenerate.base.model.ColumnInfo;
import work.codegenerate.base.model.TableInfo;

public class DatabaseUtils {
	
	private DatabaseInfo databaseInfo;
	
	public DatabaseUtils(DatabaseInfo databaseInfo){
		this.databaseInfo = databaseInfo;
	}
	
	/**
	 * 打开一个链接
	 * @author likaihao
	 * @date 2016年5月5日 下午7:39:26
	 * @return
	 */
	private Connection getConn() {
		try {
			Connection conn = null;
			if(databaseInfo.getType()==DatabaseType.mysql){
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://"+databaseInfo.getIp()+":"+databaseInfo.getPort()+"/"+databaseInfo.getName()+"?zeroDateTimeBehavior=convertToNull",databaseInfo.getUsername(),databaseInfo.getPassword());
			}else if(databaseInfo.getType()==DatabaseType.sqlserver){
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				conn = DriverManager.getConnection("jdbc:sqlserver://"+databaseInfo.getIp()+":"+databaseInfo.getPort()+";DatabaseName="+databaseInfo.getName(),databaseInfo.getUsername(),databaseInfo.getPassword());
			}
			return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//关闭资源
	private void close(Connection conn, Statement st, ResultSet re){
		//关闭ResultSet对象
		try{
			if(re != null){
				re.close();
			}
		}catch(Exception e1){
			System.out.println("关闭ResultSet对象异常");
		}finally{
			//关闭Statement对象
			try{
				if(st != null){
					st.close();
				}
			}catch(Exception e2){
				System.out.println("关闭Statement对象异常");
			}finally{
				//关闭Connection对象
				try{
					if(conn != null){
						conn.close();
					}
				}catch(Exception e3){
					System.out.println("关闭Connection对象异常");
				}
			}
		}
	}
	
	/**
	 * 查询
	 * @param dataSource 数据源
	 * @param sql 要执行的sql语句
	 * @param beanClass 目标转换类型的字节码对象
	 * @return 包含指定类型javaBean的集合
	 */
	public List<Object[]> query(String sql) {
		Connection conn = getConn();
		PreparedStatement st = null;
		ResultSet re = null;
		try {
			//获得预处理对象
			st = conn.prepareStatement(sql);
			re = st.executeQuery();
			//将数据转换为集合
			List<Object[]> list = new ArrayList<Object[]>();
			while(re.next()){
				int count = re.getMetaData().getColumnCount();
				Object[] arr = new Object[count];
				for(int i=0;i<count;i++){
					arr[i] = re.getObject(i+1);
				}
				list.add(arr);
			}
			return list;
		} catch (Exception e) {
			System.out.println(sql);
			throw new RuntimeException(e);
		}finally{
			//释放资源
			close(conn,st,re);
		}
	}
	
	/**
	 * 查询
	 * @param dataSource 数据源
	 * @param sql 要执行的sql语句
	 * @return map
	 */
	public List<Map<String,Object>> queryReturnMap(String sql) {
		Connection conn = getConn();
		PreparedStatement st = null;
		ResultSet re = null;
		try {
			//获得预处理对象
			st = conn.prepareStatement(sql);
			re = st.executeQuery();
			//将数据转换为集合
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			while(re.next()){
				int count = re.getMetaData().getColumnCount();
				Map<String,Object> map = new HashMap<String,Object>();
				for(int i=0;i<count;i++){
					String name = re.getMetaData().getColumnName(i+1);
					Object value = re.getObject(i+1);
					map.put(name, value);
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			System.out.println(sql);
			throw new RuntimeException(e);
		}finally{
			//释放资源
			close(conn,st,re);
		}
	}
	
	//查询表名
	public List<String> queryTableName(){
		//执行
		String sql = null;
		if(databaseInfo.getType()==DatabaseType.mysql){
			sql = "select table_name as 'name' from information_schema.tables where table_schema='"+databaseInfo.getName()+"'";
		}else if(databaseInfo.getType()==DatabaseType.sqlserver){
			sql = "select name from sys.tables where lob_data_space_id=0 order by create_date";
		}
		
		List<Object[]> list = query(sql);
		List<String> list2 = new ArrayList<String>();
		for(Object[] arr : list){
			list2.add(arr[0].toString());
		}
		return list2;
	}
	
	//查询主键
	public String queryPrimaryKey(String tableName){
		String sql = null;
		if(databaseInfo.getType()==DatabaseType.mysql){
			sql = "select column_name from INFORMATION_SCHEMA.COLUMNS where table_schema='"+databaseInfo.getName()+"' and table_name='"+tableName+"' AND COLUMN_KEY='PRI';";
		}else if(databaseInfo.getType()==DatabaseType.sqlserver){
			sql = "select b.column_name from information_schema.table_constraints a,information_schema.constraint_column_usage b where a.constraint_name = b.constraint_name and a.constraint_type='primary key' and a.table_name='"+tableName+"'";
		}
		
		List<Object[]> list = query(sql);
		if(list.size()>0){
			Object[] arr = list.get(0);
			if(arr!=null && arr.length>0){
				return (String)arr[0];
			}
		}
		return null;
	}
	
	//查询列名称和列类型,列注释
	public List<ColumnInfo> queryColumnInfo(String tableName){
		String sql = null;
		if(databaseInfo.getType()==DatabaseType.mysql){
			sql = "select column_name,data_type,column_comment from information_schema.columns where table_schema='"+databaseInfo.getName()+"' and table_name='"+tableName+"'";
		}else if(databaseInfo.getType()==DatabaseType.sqlserver){
			sql = "select column_name,data_type from information_schema.columns where table_name='"+tableName+"'";
		}
		List<Object[]> list = query(sql);
		List<ColumnInfo> list2 = new ArrayList<ColumnInfo>();
		for(Object[] arr : list){
			ColumnInfo info = new ColumnInfo();
			info.setName(arr[0].toString());
			info.setType(arr[1].toString());
			if(arr.length>2 && arr[2]!=null && arr[2].toString().length()>0){
				info.setComment(arr[2].toString());
			}
			list2.add(info);
		}
		return list2;
	}
	
	//查询外键(外键名称,主表名称,主键名称)
	public List<String[]> queryForeignKey(String tableName){
		String sql = null;
		if(databaseInfo.getType()==DatabaseType.mysql){
			sql = "select column_name as '外键列',referenced_table_name as '主表',referenced_column_name as '主键列' from information_schema.key_column_usage where table_schema='"+databaseInfo.getName()+"' and referenced_table_name is not null and table_name='"+tableName+"';";
		}else if(databaseInfo.getType()==DatabaseType.sqlserver){
			sql = "select 外键列=(select name from syscolumns where colid=b.fkey and id=b.fkeyid),主表=object_name(b.rkeyid),主键列=(select name from syscolumns where colid=b.rkey and id=b.rkeyid) from sysobjects a join sysforeignkeys b on a.id=b.constid join sysobjects c on a.parent_obj=c.id where object_name(b.fkeyid)='"+tableName+"'";
		}
		List<Object[]> list = query(sql);
		List<String[]> list2 = new ArrayList<String[]>();
		for(Object[] arr : list){
			String[] arr2 = new String[arr.length];
			for(int i=0;i<arr.length;i++){
				arr2[i] = (String)arr[i];
			}
			list2.add(arr2);
		}
		return list2;
	}
	
	//查询自动增长列
	public String queryIdentity(String tableName) {
		String sql = null;
		if(databaseInfo.getType()==DatabaseType.mysql){
			sql = "select column_name as '标识列' from INFORMATION_SCHEMA.COLUMNS where table_schema='"+databaseInfo.getName()+"' and extra='auto_increment' and table_name='"+tableName+"'";
		}else if(databaseInfo.getType()==DatabaseType.sqlserver){
			sql = "select c.name 标识列  from syscolumns c , sysobjects  o where c.id =o.id and o.type ='U' and c.status =128 and o.name='"+tableName+"'";
		}
		List<Object[]> list = query(sql);
		if(list.size()>0 && list.get(0).length>0){
			return (String) list.get(0)[0];
		}
		return null;
	}

	public void executeUpdateSql(String sql){
		Connection conn = getConn();
		PreparedStatement st = null;
		ResultSet re = null;
		try {
			//获得预处理对象
			st = conn.prepareStatement(sql);
			st.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			//释放资源
			close(conn,st,re);
		}
	}
	
	/**
	 * 查询所有表名
	 * @author likaihao
	 * @date 2016年5月5日 下午10:51:45
	 * @param databaseInfo
	 * @return
	 */
	public static List<String> getAllTableName(DatabaseInfo databaseInfo){
		DatabaseUtils util = new DatabaseUtils(databaseInfo);
		return util.queryTableName();
	}
	
	/**
	 * 查询某张表的信息
	 * @author likaihao
	 * @date 2016年5月5日 下午10:51:45
	 * @param databaseInfo
	 * @param tableName
	 * @return
	 */
	public static TableInfo getTableInfoByDatabase(DatabaseInfo databaseInfo,String tableName){
		DatabaseUtils util = new DatabaseUtils(databaseInfo);
		String primaryKeyName = util.queryPrimaryKey(tableName);
		if(primaryKeyName==null){
			throw new RuntimeException("指定表不存在或没有主键:"+tableName);
		}
		String identityName = util.queryIdentity(tableName);
		List<ColumnInfo> columnInfoList = util.queryColumnInfo(tableName);
		Map<String,ColumnInfo> columnInfoMap = new LinkedHashMap<String,ColumnInfo>();
		for(ColumnInfo c : columnInfoList){
			columnInfoMap.put(c.getName(), c);
		}
		
		TableInfo tableInfo = new TableInfo();
		tableInfo.setTableName(tableName);
		tableInfo.setPrimaryKeyName(primaryKeyName);
		tableInfo.setIdentityName(identityName);
		tableInfo.setColumnInfoMap(columnInfoMap);
		return tableInfo;
	}
	
	/**
	 * 查询数据库所有表的信息
	 * @author likaihao
	 * @date 2016年5月5日 下午10:52:05
	 * @param databaseInfo
	 * @return
	 */
	public static Map<String,TableInfo> getAllTableInfoByDatabase(DatabaseInfo databaseInfo){
		DatabaseUtils util = new DatabaseUtils(databaseInfo);
		List<String> tableNameList = util.queryTableName();
		
		Map<String,TableInfo> tableInfoMap = new HashMap<String,TableInfo>();
		for(String tableName : tableNameList){
			tableInfoMap.put(tableName, getTableInfoByDatabase(databaseInfo,tableName));
		}
		return tableInfoMap;
	}
	
	/**
	 * 查询数据库多个表的信息
	 * @author likaihao
	 * @date 2016年5月5日 下午10:51:45
	 * @param databaseInfo
	 * @param tableName
	 * @return
	 */
	public static Map<String,TableInfo> getTableInfoByDatabase(DatabaseInfo databaseInfo,String[] tableNameArr){
		if(tableNameArr[0].equals("all")){
			//如果第一位是all,则获取所有表名, 如果长度大于1,则后续为排除的表名
			Map<String,TableInfo> map = getAllTableInfoByDatabase(databaseInfo);
			if(tableNameArr.length>1){
				for(int i=1;i<tableNameArr.length;i++){
					map.remove(tableNameArr[i]);
				}
			}
			return map;
		}
		Map<String,TableInfo> tableInfoMap = new HashMap<String,TableInfo>();
		for(String tableName : tableNameArr){
			tableInfoMap.put(tableName, getTableInfoByDatabase(databaseInfo,tableName));
		}
		return tableInfoMap;
	}
	
	//返回数据库数据类型对应的java数据类型
	public static String getJavaType(String columnType){
		Map<String,String> column_attr_mapping = new HashMap<String,String>();
		column_attr_mapping.put("char", "String");
		column_attr_mapping.put("varchar", "String");
		column_attr_mapping.put("nvarchar", "String");
		column_attr_mapping.put("text", "String");
		column_attr_mapping.put("int", "Integer");
		column_attr_mapping.put("smallint", "Integer");
		column_attr_mapping.put("bigint", "Long");
		column_attr_mapping.put("decimal", "Double");
		column_attr_mapping.put("double", "Double");
		column_attr_mapping.put("datetime", "Date");
		column_attr_mapping.put("date", "Date");
		column_attr_mapping.put("time", "Date");
		column_attr_mapping.put("smalldatetime", "Date");
		
		String type = column_attr_mapping.get(columnType);
		if(type==null){
			throw new RuntimeException("没有指定此数据类型对应的java类型:"+columnType);
		}
		return type;
	}
}