package model.vo;


public class MessageResult {
	
	public boolean success;	//是否成功
	public String message;	//消息
	
	public MessageResult(boolean success, String message){
		this.success = success;
		this.message = message;
	}
}
