package model.generate;


public class Dictionary{

	private Integer id;
	private String name;
	private String tableName;
	private String columnName;
	private String remark;

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
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
	public String getTableName(){
		return tableName;
	}
	public void setColumnName(String columnName){
		this.columnName = columnName;
	}
	public String getColumnName(){
		return columnName;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public String getRemark(){
		return remark;
	}
	

}