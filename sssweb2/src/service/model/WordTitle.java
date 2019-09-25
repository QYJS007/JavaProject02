package service.model;

import org.apache.poi.hwpf.HWPFDocument;

public class WordTitle {
	private HWPFDocument doc;			//文档对象
	private String titleIndex;			//标题序号
	private String titleName;			//标题名称
	private int startParagraphIndex;	//开始段落索引
	private int endParagraphIndex;		//结束段落索引
	
	public HWPFDocument getDoc() {
		return doc;
	}
	public void setDoc(HWPFDocument doc) {
		this.doc = doc;
	}
	public String getTitleIndex() {
		return titleIndex;
	}
	public void setTitleIndex(String titleIndex) {
		this.titleIndex = titleIndex;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String name) {
		this.titleName = name;
	}
	public int getStartParagraphIndex() {
		return startParagraphIndex;
	}
	public void setStartParagraphIndex(int startParagraphIndex) {
		this.startParagraphIndex = startParagraphIndex;
	}
	public int getEndParagraphIndex() {
		return endParagraphIndex;
	}
	public void setEndParagraphIndex(int endParagraphIndex) {
		this.endParagraphIndex = endParagraphIndex;
	}
	@Override
	public String toString() {
		return "WordInterInfo [titleIndex=" + titleIndex + ", titleName=" + titleName
				+ ", startParagraphIndex=" + startParagraphIndex
				+ ", endParagraphIndex=" + endParagraphIndex + "]";
	}
}
