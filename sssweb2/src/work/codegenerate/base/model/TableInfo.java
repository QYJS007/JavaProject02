package work.codegenerate.base.model;

import java.util.Map;


public class TableInfo {
	private String tableName;
	private String primaryKeyName;
	private String identityName;
	private Map<String,ColumnInfo> columnInfoMap;
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getPrimaryKeyName() {
		return primaryKeyName;
	}
	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}
	public Map<String, ColumnInfo> getColumnInfoMap() {
		return columnInfoMap;
	}
	public void setColumnInfoMap(Map<String, ColumnInfo> columnInfoMap) {
		this.columnInfoMap = columnInfoMap;
	}
	public String getIdentityName() {
		return identityName;
	}
	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}
	
}
