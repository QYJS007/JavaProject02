package model.generate;

import dao.generate.DictionaryGenDao;

public class DictionaryDetail{

	private Integer id;
	private Integer dictionaryId;
	private String value;
	private String showText;
	private String remark;

	public void setId(Integer id){
		this.id = id;
	}
	public Integer getId(){
		return id;
	}
	public void setDictionaryId(Integer dictionaryId){
		this.dictionaryId = dictionaryId;
	}
	public Integer getDictionaryId(){
		return dictionaryId;
	}
	public void setValue(String value){
		this.value = value;
	}
	public String getValue(){
		return value;
	}
	public void setShowText(String showText){
		this.showText = showText;
	}
	public String getShowText(){
		return showText;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}
	public String getRemark(){
		return remark;
	}
	

	public String getDictionaryName(){
		try {
			if(dictionaryId!=null){
				return new DictionaryGenDao().findById(dictionaryId).getName();
			}
		} catch (Exception e) {
		}
		return null;
	}

}