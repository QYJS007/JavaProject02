package work.codegenerate.base;


public class DatabaseInfo {
	public enum DatabaseType{
		mysql,
		sqlserver;
	}
	
	private DatabaseType type;
	private String ip;
	private String port;
	private String dbName;//数据库名称
	private String username;
	private String password;
	
	public DatabaseInfo(DatabaseType type,String ip,String port,String dbName,String username,String password){
		this.type = type;
		this.ip = ip;
		this.port = port;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}
	
	public DatabaseInfo(String type,String ip,String port,String name,String username,String password){
		this.type = DatabaseType.valueOf(type);
		this.ip = ip;
		this.port = port;
		this.dbName = name;
		this.username = username;
		this.password = password;
	}
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public DatabaseType getType() {
		return type;
	}
	public void setType(DatabaseType type) {
		this.type = type;
	}
	public String getName() {
		return dbName;
	}
	public void setName(String name) {
		this.dbName = name;
	}
	
}
