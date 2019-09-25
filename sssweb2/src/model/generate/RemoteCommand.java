package model.generate;

import dao.generate.RemoteCommandGroupGenDao;

public class RemoteCommand{

	private Integer id;
	private Integer commandGroupId;
	private String name;
	private String command;
	private String func;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return id;
	}
	public void setCommandGroupId(Integer commandGroupId){
		this.commandGroupId = commandGroupId;
	}
	public Integer getCommandGroupId(){
		return commandGroupId;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
	public void setCommand(String command){
		this.command = command;
	}
	public String getCommand(){
		return command;
	}
	public void setFunc(String func){
		this.func = func;
	}
	public String getFunc(){
		return func;
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