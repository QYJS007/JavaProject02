package model.generate;


public class DbInfo{

	private Integer id;
	private String name;
	private String type;
	private String ip;
	private Integer port;
	private String username;
	private String password;
	private String dbname;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return id;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
	}
	public void setIp(String ip){
		this.ip = ip;
	}
	public String getIp(){
		return ip;
	}
	public void setPort(Integer port){
		this.port = port;
	}
	public Integer getPort(){
		return port;
	}
	public void setUsername(String username){
		this.username = username;
	}
	public String getUsername(){
		return username;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return password;
	}
	public void setDbname(String dbname){
		this.dbname = dbname;
	}
	public String getDbname(){
		return dbname;
	}
	

}