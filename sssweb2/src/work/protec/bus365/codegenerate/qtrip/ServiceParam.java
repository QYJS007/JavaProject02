package work.protec.bus365.codegenerate.qtrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceParam {
	//模型的中文描述
	public String modelChineseName = null;
	//模型字段的中文描述
	public Map<String,String> fieldChineseNameMap = new HashMap<String,String>(); 
	
	//查询条件
	public List<String> searchParamList = new ArrayList<String>();
	//查询时不显示的列
	public List<String> noShowList = new ArrayList<String>();
	
	//修改时不显示的列
	public List<String> noUpdateShowList = new ArrayList<String>();
	//修改时不能为空的列
	public List<String> mustFieldList = new ArrayList<String>();
	//修改时显示为areatext的列
	public List<String> textAreaList = new ArrayList<String>();
	//数据库不能修改的列
	public List<String> noUpdateList = new ArrayList<String>();
	
	//使用数据字典的列(显示为下拉列表)
	public List<String> selectFieldList = new ArrayList<String>();
	//值为统计表的列(显示为下拉列表)(列名_统计的表名_作为value的列名_作为text的列名)
	public List<String> selectFieldByCountList = new ArrayList<String>();
	//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
	public List<String> autoCompleteList = new ArrayList<String>();
}