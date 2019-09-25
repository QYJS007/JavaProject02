package work.codegenerate.base.model;

import work.codegenerate.base.DatabaseUtils;


public class ColumnInfo {
	private String name;
	private String type;
	private String comment;
	private String javaType;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	public String getJavaType() {
		if(javaType==null){
			javaType = DatabaseUtils.getJavaType(this.type);
		}
		return javaType;
	}
	
}
