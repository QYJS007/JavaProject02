package model.generate;


public class HttpDownTemplet{

	private Integer id;
	private String name;
	private String url;
	private String urlRegex;
	private String nameRegex;
	private String nextLayer;
	private String nextPage;
	private String encoding;
	private Integer intervalTime;

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
	public void setUrl(String url){
		this.url = url;
	}
	public String getUrl(){
		return url;
	}
	public void setUrlRegex(String urlRegex){
		this.urlRegex = urlRegex;
	}
	public String getUrlRegex(){
		return urlRegex;
	}
	public void setNameRegex(String nameRegex){
		this.nameRegex = nameRegex;
	}
	public String getNameRegex(){
		return nameRegex;
	}
	public void setNextLayer(String nextLayer){
		this.nextLayer = nextLayer;
	}
	public String getNextLayer(){
		return nextLayer;
	}
	public void setNextPage(String nextPage){
		this.nextPage = nextPage;
	}
	public String getNextPage(){
		return nextPage;
	}
	public void setEncoding(String encoding){
		this.encoding = encoding;
	}
	public String getEncoding(){
		return encoding;
	}
	public void setIntervalTime(Integer intervalTime){
		this.intervalTime = intervalTime;
	}
	public Integer getIntervalTime(){
		return intervalTime;
	}
	

}