package model.vo;

import java.util.List;

//文件查询结果的模型类
public class SearchResult {
	private String filePath;
	private List<String> contentList;
	private List<Integer> lineNumList;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public List<String> getContentList() {
		return contentList;
	}
	public void setContentList(List<String> contentList) {
		this.contentList = contentList;
	}
	public List<Integer> getLineNumList() {
		return lineNumList;
	}
	public void setLineNumList(List<Integer> lineNumList) {
		this.lineNumList = lineNumList;
	}
}
