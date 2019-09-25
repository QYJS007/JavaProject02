package model.generate;


public class QueryTemplet{

	private Integer id;
	private String name;
	private String path;
	private String pathPattern;
	private String noPathPattern;
	private String fileNamePattern;
	private String noFileNamePattern;
	private String encoding;

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
	public void setPathPattern(String pathPattern){
		this.pathPattern = pathPattern;
	}
	public String getPathPattern(){
		return pathPattern;
	}
	public void setNoPathPattern(String noPathPattern){
		this.noPathPattern = noPathPattern;
	}
	public String getNoPathPattern(){
		return noPathPattern;
	}
	public void setFileNamePattern(String fileNamePattern){
		this.fileNamePattern = fileNamePattern;
	}
	public String getFileNamePattern(){
		return fileNamePattern;
	}
	public void setNoFileNamePattern(String noFileNamePattern){
		this.noFileNamePattern = noFileNamePattern;
	}
	public String getNoFileNamePattern(){
		return noFileNamePattern;
	}
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	public String getEncoding(){
		return encoding;
	}
	

}