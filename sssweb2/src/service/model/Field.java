package service.model;

public class Field {
	private String name;
	private String comment;
	private String type;
	
	//下边是数据库专用
	private boolean isPrimaryKey;//是否是主键
	private boolean isIndex;//是否是索引
	private boolean isNotNull;//是否非空
	private boolean isUnique;//是否唯一
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isIndex() {
		return isIndex;
	}
	public void setIndex(boolean isIndex) {
		this.isIndex = isIndex;
	}
	public boolean isNotNull() {
		return isNotNull;
	}
	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	public boolean isUnique() {
		return isUnique;
	}
	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
}
