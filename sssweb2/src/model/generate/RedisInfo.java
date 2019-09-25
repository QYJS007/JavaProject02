package model.generate;


public class RedisInfo{

	private Integer id;
	private String name;
	private String ip;
	private Integer port;
	private String password;

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
	public void setPassword(String password){
		this.password = password;
	}
	public String getPassword(){
		return password;
	}
	

}