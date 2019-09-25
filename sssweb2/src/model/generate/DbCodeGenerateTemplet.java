package model.generate;


public class DbCodeGenerateTemplet{

	private Integer id;
	private String type;
	private String name;
	private String code;
	private String param;

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
	public void setCode(String code){
		this.code = code;
	}
	public String getCode(){
		return code;
	}
	public void setParam(String param){
		this.param = param;
	}
	public String getParam(){
		return param;
	}
	

}