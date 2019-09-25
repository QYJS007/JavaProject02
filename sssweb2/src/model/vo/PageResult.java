package model.vo;

import java.util.List;

import utils.Page;

public class PageResult<T> {
	
	public List<T> rows;	//数据
	public Integer total;	//数据库总条数
	
	public PageResult(Page<T> page){
		this.rows = page.getData();
		this.total = page.getAllDataNum();
	}
}
