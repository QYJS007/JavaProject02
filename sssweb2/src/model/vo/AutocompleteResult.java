package model.vo;

import java.util.List;

public class AutocompleteResult{
	private List<String> fieldList;
	private List<String> nameList;
	private List<Object> resultList;
	private List<Integer> fieldWidthList;
	private int resultCount;
	
	public List<String> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}
	public List<String> getNameList() {
		return nameList;
	}
	public void setNameList(List<String> nameList) {
		this.nameList = nameList;
	}
	public List<Object> getResultList() {
		return resultList;
	}
	public void setResultList(List<Object> resultList) {
		this.resultList = resultList;
	}
	public List<Integer> getFieldWidthList() {
		return fieldWidthList;
	}
	public void setFieldWidthList(List<Integer> fieldWidthList) {
		this.fieldWidthList = fieldWidthList;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	
}
