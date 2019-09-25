package model.generate;


public class HttpTemplet{

	private Integer id;
	private String type;
	private String name;
	private String url;
	private String method;
	private String head;
	private String param;
	private String encoding;

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
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return url;
	}
	public void setMethod(String method){
		this.method = method;
	}
	public String getMethod(){
		return method;
	}
	public void setHead(String head){
		this.head = head;
	}
	public String getHead(){
		return head;
	}
	public void setParam(String param){
		this.param = param;
	}
	public String getParam(){
		return param;
	}
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	public String getEncoding(){
		return encoding;
	}
	

}