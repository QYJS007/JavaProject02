package model.vo;


public class UrlInfo {
	public String url;
	public String name;
	public int subUrlCount;
	public UrlInfo parent;
	public String localDownPath;
	
	@Override
	public String toString() {
		return "[name=" + name + ", subUrlCount=" + subUrlCount+"]";
	}
}
