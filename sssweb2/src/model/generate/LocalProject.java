package model.generate;


public class LocalProject{

	private Integer id;
	private String name;
	private String path;
	private String packDir;
	private String packFile;

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
	public void setPath(String path){
		this.path = path;
	}
	public String getPath(){
		return path;
	}
	public void setPackDir(String packDir){
		this.packDir = packDir;
	}
	public String getPackDir(){
		return packDir;
	}
	public void setPackFile(String packFile){
		this.packFile = packFile;
	}
	public String getPackFile(){
		return packFile;
	}
	

}