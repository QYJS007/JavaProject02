package utils;

import java.util.List;

/**
 * 分页工具类
 * @author Administrator
 *
 */
public class Page<T> {
	
	//必须
	private Integer pageNum;		//当前页
	private Integer pageSize;		//每页显示个数
	private Integer allDataNum;		//总记录数
	
	//计算
	private Integer startIndex;		//开始索引
	private Integer allPageNum;		//总页数
	private List<T> data;			//查询的数据
	
	//扩展
	private Integer showPageCount = 10;	//页面显示页码数(大于总页数的话表示全显示)
	private Integer showPageStart;		//遍历开始页码
	private Integer showPageEnd;		//遍历结束页码
	
	public Page(int pageNum, int pageSize){
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}
	
	public Page(int pageNum, int pageSize, int allDataNum){
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.setAllDataNum(allDataNum);
	}
	
	/**根据基础三项计算其它项的值(在调用setAllDataNum方法时调用)*/
	private void init(){
		if(pageNum==null){
			pageNum = 1;
		}
		if(pageSize==null){
			pageSize = 10;
		}
		
		//计算-总页数
		allPageNum = (allDataNum + pageSize - 1) / pageSize;
		if(allPageNum < 1){
			allPageNum = 1;
		}
		
		//赋值-当前页
		if(pageNum < 1){
			this.pageNum = 1;
		}else if(pageNum > allPageNum){
			this.pageNum = allPageNum;
		}
		
		//计算-开始索引
		startIndex = (pageNum - 1) * pageSize;
		
		//计算start 和 end
		if(allPageNum < showPageCount){
			showPageStart = 1;
			showPageEnd = allPageNum;
		}else{
			showPageStart = pageNum - (showPageCount)/2;
			showPageEnd = pageNum + (showPageCount+1)/2 - 1;
			
			if(showPageStart < 1){
				showPageStart = 1;
				showPageEnd = showPageCount;
			}else if(showPageEnd > allPageNum){
				showPageEnd = allPageNum;
				showPageStart = allPageNum - showPageCount + 1;
			}
		}
	}
	
	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getAllDataNum() {
		return allDataNum;
	}

	public void setAllDataNum(Integer allDataNum) {
		this.allDataNum = allDataNum;
		this.init();//根据基础三项计算其它项的值
	}

	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getAllPageNum() {
		return allPageNum;
	}

	public void setAllPageNum(Integer allPageNum) {
		this.allPageNum = allPageNum;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Integer getShowPageCount() {
		return showPageCount;
	}

	public void setShowPageCount(Integer showPageCount) {
		this.showPageCount = showPageCount;
	}

	public Integer getShowPageStart() {
		return showPageStart;
	}

	public void setShowPageStart(Integer showPageStart) {
		this.showPageStart = showPageStart;
	}

	public Integer getShowPageEnd() {
		return showPageEnd;
	}

	public void setShowPageEnd(Integer showPageEnd) {
		this.showPageEnd = showPageEnd;
	}

}
