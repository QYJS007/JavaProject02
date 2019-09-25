package model.generate;


public class LineReplaceTemplet{

	private Integer id;
	private String name;
	private String oldTemplet;
	private String newTemplet;
	private String text;

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
	public void setOldTemplet(String oldTemplet){
		this.oldTemplet = oldTemplet;
	}
	public String getOldTemplet(){
		return oldTemplet;
	}
	public void setNewTemplet(String newTemplet){
		this.newTemplet = newTemplet;
	}
	public String getNewTemplet(){
		return newTemplet;
	}
	public void setText(String text){
		this.text = text;
	}
	public String getText(){
		return text;
	}
	

}