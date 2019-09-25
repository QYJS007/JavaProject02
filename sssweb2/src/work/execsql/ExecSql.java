package work.execsql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.IOUtils;
import utils.RegexUtils;

public class ExecSql {
	
	public static void main(String[] args) throws Exception {
		String ip = "127.0.0.1";
		String port = "3306";
		String dbName = "sssweb2_pub";
		String username = "root";
		String password = "root";
		String sqlPath= "E:\\Windows\\Documents\\Tencent Files\\1219893623\\FileRecv\\bus365\\sssweb2_pub.sql";
		
		exec(ip, port, dbName, username, password, sqlPath);
	}
	
	/**
	 * 描述:执行sql 
	 * @author: 李凯昊
	 * @date:2017年6月9日 下午2:25:42
	 * @param ip
	 * @param port
	 * @param dbName
	 * @param username
	 * @param password
	 * @param sqlPath
	 */
	public static void exec(String ip, String port, String dbName, String username, String password, String sqlPath){
		try {
			if(!new File(sqlPath).exists()){
				throw new RuntimeException("未找到sql文件:"+sqlPath);
			}
			
			//获得sql
			String content = IOUtils.readFile(sqlPath);
			List<String> sqlList = new ArrayList<String>();
			
			//获得数控连接
			Connection conn = getConn(ip, port, dbName, username, password);
			
			//获得所有 分割区域
			List<String> sqlBlockList = RegexUtils.getSubstrByRegexReturnList(content, "-- ------@(.*?)@start--------([\\s\\S]+?)-- ------@\\1@end--------");
			for(String sqlBlock : sqlBlockList){
				//获取区域第一行 查询语句
				String selectSql = RegexUtils.getSubstrByRegex(sqlBlock, "@repeat\\{(.*?)\\}");
				selectSql = selectSql.replace("@@dbname", dbName);
				if(!isExists(conn, selectSql)){
					sqlList.addAll(Arrays.asList(sqlBlock.split(";\r\n")));
				}
			}
			for(String sql : sqlList){
				sql = sql.trim();
				if(sql.length()>0){
					System.out.println("执行sql:"+sql.replace("\r\n", "\\r\\n"));
					execUpdateSql(conn, sql);
				}
			}
			conn.close();
			
			System.out.println("执行完成,执行了"+sqlList.size()+"条sql语句");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//获得数据库连接
	public static Connection getConn(String ip, String port, String dbName, String username, String password){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://"+ip+":"+port+"/"+dbName+"?zeroDateTimeBehavior=convertToNull", username, password);
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	//执行sql更新语句
	public static void execUpdateSql(Connection conn, String sql){
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//执行sql查询语句,返回count是否>0
	public static boolean isExists(Connection conn, String sql){
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet resultSet = st.executeQuery();
			resultSet.next();
			return resultSet.getInt(1)>0;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
