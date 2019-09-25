package service.model;

import java.util.ArrayList;
import java.util.List;

public class Model {
	private String name;
	private String comment;
	private List<Field> fieldList = new ArrayList<Field>();
	
	//下边是数据库专用
	private String indexStr;//索引字段文本
	private List<String> indexList = new ArrayList<String>();//索引字段集合
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Field> getFieldList() {
		return fieldList;
	}
	public void setFieldList(List<Field> fieldList) {
		this.fieldList = fieldList;
	}
	
	public String getIndexStr() {
		return indexStr;
	}
	public void setIndexStr(String indexStr) {
		this.indexStr = indexStr;
	}
	public List<String> getIndexList() {
		return indexList;
	}
	public void setIndexList(List<String> indexList) {
		this.indexList = indexList;
	}
	
	//页面模板中使用了此方法
	public List<String> getAllFieldName(){
		List<String> list = new ArrayList<String>();
		for(Field field : fieldList){
			list.add(field.getName());
		}
		return list;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("--"+name+"\r\n");
		builder.append("描述:"+comment+"\r\n");
		builder.append("字段\r\n");
		for(Field field : fieldList){
			builder.append(field.getType() + " " + field.getName() + "; //"+field.getComment() + "\r\n");
		}
		builder.append("索引:"+indexStr+"\r\n");
		return builder.toString();
	}
}
