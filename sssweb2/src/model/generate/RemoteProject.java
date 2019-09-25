package model.generate;

import dao.generate.RemoteCommandGroupGenDao;

public class RemoteProject{

	private Integer id;
	private String type;
	private String name;
	private String ip;
	private String username;
	private String password;
	private String path;
	private Integer port;
	private Integer commandGroupId;
	private String command;
	private String localProject;
	private Integer istest;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return id;
	}
	public void setType(String type){
		this.type = type;
	}
	public String getType(){
		return type;
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
	public void setPath(String path){
		this.path = path;
	}
	public String getPath(){
		return path;
	}
	public void setPort(Integer port){
		this.port = port;
	}
	public Integer getPort(){
		return port;
	}
	public void setCommandGroupId(Integer commandGroupId){
		this.commandGroupId = commandGroupId;
	}
	public Integer getCommandGroupId(){
		return commandGroupId;
	}
	public void setCommand(String command){
		this.command = command;
	}
	public String getCommand(){
		return command;
	}
	public void setLocalProject(String localProject){
		this.localProject = localProject;
	}
	public String getLocalProject(){
		return localProject;
	}
	public void setIstest(Integer istest){
		this.istest = istest;
	}
	public Integer getIstest(){
		return istest;
	}
	

	public String getRemoteCommandGroupName(){
		try {
			if(commandGroupId!=null){
				return new RemoteCommandGroupGenDao().findById(commandGroupId).getName();
			}
		} catch (Exception e) {
		}
		return null;
	}

}