package work.protec.bus365.codegenerate.qtrip;

import java.lang.reflect.Method;
import java.util.List;

import work.codegenerate.base.CodeGenerate;
import work.codegenerate.base.CommonUtils;
import work.codegenerate.base.model.TableInfo;

public class ServiceMain {
	
	public static void main(String[] args) {
		CodeGenerate.run(ServiceMain.class);
	}
	
	/**
	 * 获得modelName
	 * @author likaihao
	 * @date 2016年11月27日 下午1:49:26
	 * @param tableName
	 * @param tablePrefix
	 * @return
	 */
	public String getModelName(String tableName, String tablePrefix){
		String modelName = null;
		if(tableName.equals("businessbaseconfig")){
			modelName = "BusinessBaseConfig";
		}else if(tableName.equals("citybusiness")){
			modelName = "CityBusiness";
		}else if(tableName.equals("startcity")){
			modelName = "StartCity";
		}else if(tableName.equals("vehiclebusiness")){
			modelName = "VehicleBusiness";
		}else if(tableName.equals("orgopenhours")){
			modelName = "OrgOpenHours";
		}else if(tableName.equals("cancelorderreason")){
			modelName = "CancelOrderReason";
		}else if(tableName.equals("jcyc_terminal")){
			modelName = "Terminal";
		}else if(tableName.equals("jcyc_flightnoterminal")){
			modelName = "FlightnoTerminal";
		}else if(tableName.equals("businessvehicletype")){
			modelName = "BusinessVehicleType";
		}else if(tableName.equals("sellnum")){
			modelName = "SellNum";
		}else{
			//去掉表前缀,按_分割,首字母大写
			modelName = new CommonUtils().getModelNameByTableName(tableName,tablePrefix);
		}
		return modelName;
	}
	
	/**
	 * 根据数据库名称获得play切库的配置名称
	 * @author likaihao
	 * @date 2016年12月12日 上午11:17:59
	 * @param dbName
	 * @return
	 */
	public String getPlayJPAConfigName(String dbName){
		String configName = null;
		if(dbName.equals("qtrp_b")){
			configName = "Gobal.DB_QTRP_B";
		}else if(dbName.equals("qtrp_p")){
			configName = "Gobal.DB_QTRP_P";
		}else if(dbName.equals("qtrp_u")){
			configName = "Gobal.DB_QTRP_U";
		}else if(dbName.equals("data")){
			configName = "null";
		}else{
			throw new RuntimeException("没有指定数据库对应的JPA切库配置名称:"+dbName);
		}
		return configName;
	}
	
	//判断指定列是否是数据字典的列
	public boolean isSelectField(String columnName,List<String> selectFieldList){
		if(selectFieldList.size()>0){
			for(String name : selectFieldList){
				if(name.startsWith(columnName+"|")){
					return true;
				}
			}
		}
		return false;
	}
	
	//判断指定列是否是统计的列
	public boolean isSelectFieldByCount(String columnName,List<String> selectFieldByCountList){
		if(selectFieldByCountList.size()>0){
			for(String name : selectFieldByCountList){
				if(name.startsWith(columnName+"|")){
					return true;
				}
			}
		}
		return false;
	}
	
	//判断指定列是否是统计列的描述列
	public boolean isSelectFieldByCountComment(String columnName,List<String> selectFieldByCountList){
		if(selectFieldByCountList.size()>0){
			for(String name : selectFieldByCountList){
				String comment = name.split("\\|")[1];
				if(columnName.equals(comment)){
					return true;
				}
			}
		}
		return false;
	}
	
	//判断指定列是否是自动补全的描述列
	public boolean isAutoCompleteComment(String columnName,List<String> autoCompleteList){
		if(autoCompleteList.size()>0){
			for(String name : autoCompleteList){
				String comment = name.split("\\|")[1];
				if(columnName.equals(comment)){
					return true;
				}
			}
		}
		return false;
	}
		
	
	/**
	 * 获取业务相关参数
	 * @author likaihao
	 * @date 2016年8月4日 上午11:19:13
	 * @param tableName
	 * @return
	 */
	public ServiceParam getServiceParamMap(TableInfo tableInfo){
		String tableName = tableInfo.getTableName();
		//根据_tableName调用对应的方法
		ServiceParam param = null;
		try {
			Method method = this.getClass().getDeclaredMethod("_"+tableName, TableInfo.class);
			param = (ServiceParam) method.invoke(this, tableInfo);
		} catch (Exception e) {
			throw new RuntimeException("没有指定tableName的业务参数,"+tableName);
		}
		
		//将没有指定中文描述的列 设置为数据库的注释
		for(String columnName : tableInfo.getColumnInfoMap().keySet()){
			if(!param.fieldChineseNameMap.containsKey(columnName)){
				String comment = tableInfo.getColumnInfoMap().get(columnName).getComment();
				//如果没有数据库注释,则采用字段名称作为描述
				if(comment==null || comment.length()==0){
					comment = columnName;
				}
				param.fieldChineseNameMap.put(columnName, comment);
			}
		}
		return param;
	}
	
//	@SuppressWarnings("unused")
//	private ServiceParam _citybusiness(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "机构开通城市和业务";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
//		param.fieldChineseNameMap.put("orgcode", "机构代码");
//		param.fieldChineseNameMap.put("orgname", "机构名称");
//		param.fieldChineseNameMap.put("cityname", "城市名称");
//		//查询条件
//		param.searchParamList.add("orgcode");
//		param.searchParamList.add("citycode");
//		param.searchParamList.add("businesscode");
//		//查询时不显示的列
//		param.noShowList.add("usercode");
//		param.noShowList.add("username");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		//修改时不显示的列
//		param.noUpdateShowList.add("cityname");
//		param.noUpdateShowList.add("orgname");
//		param.noUpdateShowList.add("businessname");
//		param.noUpdateShowList.add("usercode");
//		param.noUpdateShowList.add("username");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("citycode");
//		param.mustFieldList.add("orgcode");
//		param.mustFieldList.add("businesscode");
//		param.mustFieldList.add("isactive");
//		//修改时显示为 textarea 的列
//		//textAreaList.add("explain");
//		//使用数据字典的列(显示为下拉列表)
//		param.selectFieldList.add("isactive");
//		//使用统计表的列(显示为下拉列表)(列名_统计的表名_作为value的列名_作为text的列名)
//		param.selectFieldByCountList.add("businesscode_business_businesscode_businessname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("citycode_cityname_city_citycode_cityname_城市代码_城市名称");
//		param.autoCompleteList.add("orgcode_orgname_organization_code_name_机构代码_机构名称");
//		return param;
//	}
//	
//	@SuppressWarnings("unused")
//	private ServiceParam _startcity(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "热门出发城市";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
//		param.fieldChineseNameMap.put("orgcode", "机构代码");
//		param.fieldChineseNameMap.put("orgname", "机构名称");
//		//查询条件
//		param.searchParamList.add("citycode");
//		param.searchParamList.add("businesscode");
//		//查询时不显示的列
//		param.noShowList.add("usercode");
//		param.noShowList.add("username");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		param.noShowList.add("displayorder");
//		//修改时不显示的列
//		param.noUpdateShowList.add("cityname");
//		param.noUpdateShowList.add("businessname");
//		param.noUpdateShowList.add("displayorder");
//		param.noUpdateShowList.add("usercode");
//		param.noUpdateShowList.add("username");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("citycode");
//		param.mustFieldList.add("businesscode");
////		param.mustFieldList.add("isactive");
//		//修改时显示为 textarea 的列
//		//textAreaList.add("explain");
//		//使用数据字典的列(显示为下拉列表)
////		param.selectFieldList.add("isactive");
//		//使用统计表的列(显示为下拉列表)(列名_统计的表名_作为value的列名_作为text的列名)
//		param.selectFieldByCountList.add("businesscode_business_businesscode_businessname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("citycode_cityname_city_citycode_cityname_城市代码_城市名称");
//		return param;
//	}
//	
//	@SuppressWarnings("unused")
//	private ServiceParam _vehiclebusiness(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "车辆业务";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
////		param.fieldChineseNameMap.put("orgcode", "机构代码");
////		param.fieldChineseNameMap.put("orgname", "机构名称");
//		//查询条件
//		param.searchParamList.add("orgcode");
//		param.searchParamList.add("businesscode");
//		param.searchParamList.add("vehicleno");
//		//查询时不显示的列
//		param.noShowList.add("usercode");
//		param.noShowList.add("username");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		//修改时不显示的列
//		param.noUpdateShowList.add("orgname");
//		param.noUpdateShowList.add("businessname");
//		param.noUpdateShowList.add("usercode");
//		param.noUpdateShowList.add("username");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("orgcode");
//		param.mustFieldList.add("businesscode");
//		param.mustFieldList.add("vehicleno");
////		param.mustFieldList.add("isactive");
//		//修改时显示为 textarea 的列
//		//textAreaList.add("explain");
//		//使用数据字典的列(显示为下拉列表)
////		param.selectFieldList.add("isactive");
//		//使用统计表的列(显示为下拉列表)(列名_统计的表名_作为value的列名_作为text的列名)
//		param.selectFieldByCountList.add("businesscode_business_businesscode_businessname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("orgcode_orgname_organization_code_name_机构代码_机构名称");
//		param.autoCompleteList.add("vehicleno_brandname_vehicle_vehicleno_brandname_车牌号_品牌名称");
//		return param;
//	}
//	
//	@SuppressWarnings("unused")
//	private ServiceParam _orgopenhours(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "机构营业时间";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
////		param.fieldChineseNameMap.put("orgcode", "机构代码");
////		param.fieldChineseNameMap.put("orgname", "机构名称");
//		//查询条件
//		param.searchParamList.add("orgcode");
//		param.searchParamList.add("businesscode");
//		//查询时不显示的列
//		param.noShowList.add("usercode");
//		param.noShowList.add("username");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		//修改时不显示的列
//		param.noUpdateShowList.add("orgname");
//		param.noUpdateShowList.add("businessname");
//		param.noUpdateShowList.add("usercode");
//		param.noUpdateShowList.add("username");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("orgcode");
//		param.mustFieldList.add("businesscode");
//		param.mustFieldList.add("startvalue");
//		param.mustFieldList.add("endvalue");
////		param.mustFieldList.add("isactive");
//		//修改时显示为 textarea 的列
//		//textAreaList.add("explain");
//		//使用数据字典的列(显示为下拉列表)
////		param.selectFieldList.add("isactive");
//		//使用统计表的列(显示为下拉列表)(列名_统计的表名_作为value的列名_作为text的列名)
//		param.selectFieldByCountList.add("businesscode_business_businesscode_businessname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("orgcode_orgname_organization_code_name_机构代码_机构名称");
//		return param;
//	}
	
//	@SuppressWarnings("unused")
//	private ServiceParam _agreement(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "协议";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
////		param.fieldChineseNameMap.put("orgcode", "机构代码");
////		param.fieldChineseNameMap.put("orgname", "机构名称");
//		//查询条件
//		param.searchParamList.add("orgcode");
//		param.searchParamList.add("businesscode");
//		param.searchParamList.add("agreementcode");
//		param.searchParamList.add("isactive");
//		//查询时不显示的列
//		param.noShowList.add("description");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		//修改时不显示的列
//		param.noUpdateShowList.add("orgname");
//		param.noUpdateShowList.add("businessname");
//		param.noUpdateShowList.add("agreementname");
//		param.noUpdateShowList.add("url");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("orgcode");
//		param.mustFieldList.add("businesscode");
//		param.mustFieldList.add("agreementcode");
//		param.mustFieldList.add("description");
//		param.mustFieldList.add("isactive");
//		//修改时显示为 textarea 的列
//		param.textAreaList.add("description");
//		//数据库不修改的列
//		param.noUpdateList.add("createtime");
//		
//		//使用数据字典的列(显示为下拉列表)(列名_描述列名,没有描述列则写null)
//		param.selectFieldList.add("isactive_null");
//		param.selectFieldList.add("agreementcode_agreementname");
//		//使用统计表的列(显示为下拉列表)(列名_描述列名_统计的表名_作为value的列名_作为text的列名)
//		param.selectFieldByCountList.add("businesscode_businessname_business_businesscode_businessname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("orgcode_orgname_organization_code_name_机构代码_机构名称");
//		return param;
//	}
//	
//	@SuppressWarnings("unused")
//	private ServiceParam _cancelorderreason(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "取消订单原因模板";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
////		param.fieldChineseNameMap.put("orgcode", "机构代码");
////		param.fieldChineseNameMap.put("orgname", "机构名称");
//		//查询条件
//		param.searchParamList.add("orgcode");
//		param.searchParamList.add("businesscode");
//		param.searchParamList.add("isactive");
//		//查询时不显示的列
//		param.noShowList.add("usercode");
//		param.noShowList.add("username");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		//修改时不显示的列
//		param.noUpdateShowList.add("orgname");
//		param.noUpdateShowList.add("businessname");
//		param.noUpdateShowList.add("usercode");
//		param.noUpdateShowList.add("username");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("orgcode");
//		param.mustFieldList.add("businesscode");
//		param.mustFieldList.add("reasoncode");
//		param.mustFieldList.add("reasonname");
//		param.mustFieldList.add("isactive");
//		//修改时显示为 textarea 的列
//		param.textAreaList.add("reasonname");
//		//数据库不修改的列
//		param.noUpdateList.add("createtime");
//		
//		//使用数据字典的列(显示为下拉列表)(列名_描述列名,没有描述列则写null)
//		param.selectFieldList.add("isactive_null");
//		//使用统计表的列(显示为下拉列表)(列名_描述列名_统计的表名_作为value的列名_作为text的列名)
//		param.selectFieldByCountList.add("businesscode_businessname_business_businesscode_businessname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("orgcode_orgname_organization_code_name_机构代码_机构名称");
//		return param;
//	}
//	
//	@SuppressWarnings("unused")
//	private ServiceParam _jcyc_terminal(TableInfo tableInfo){
//		ServiceParam param = new ServiceParam();
//		//模型的中文描述
//		param.modelChineseName = "航站楼";
//		//模型字段的中文描述(不指定默认为数据库的字段描述)
////		param.fieldChineseNameMap.put("orgcode", "机构代码");
////		param.fieldChineseNameMap.put("orgname", "机构名称");
//		//查询条件
//		param.searchParamList.add("citycode");
//		param.searchParamList.add("airportname");
//		param.searchParamList.add("terminalname");
//		//查询时不显示的列
//		param.noShowList.add("usercode");
//		param.noShowList.add("username");
//		param.noShowList.add("updatetime");
//		param.noShowList.add("createtime");
//		//修改时不显示的列
//		param.noUpdateShowList.add("cityname");
//		param.noUpdateShowList.add("usercode");
//		param.noUpdateShowList.add("username");
//		param.noUpdateShowList.add("updatetime");
//		param.noUpdateShowList.add("createtime");
//		//修改时不能为空的列
//		param.mustFieldList.add("citycode");
//		param.mustFieldList.add("threeword");
//		param.mustFieldList.add("fourword");
//		param.mustFieldList.add("airportname");
//		param.mustFieldList.add("terminalname");
//		param.mustFieldList.add("address");
//		param.mustFieldList.add("origin");
//		//修改时显示为 textarea 的列
////		param.textAreaList.add("reasonname");
//		//数据库不修改的列
//		param.noUpdateList.add("createtime");
//		
//		//使用数据字典的列(显示为下拉列表)(列名_描述列名,没有描述列则写null)
////		param.selectFieldList.add("isactive_null");
//		//使用统计表的列(显示为下拉列表)(列名_描述列名_统计的表名_作为value的列名_作为text的列名)
////		param.selectFieldByCountList.add("citycode|cityname_city_citycode_cityname");
//		//使用自动补全的列(自动提示)(列名_描述列名_要查询的主表名称_主表条件列名_主表查询列名_主表条件列显示名称_主表查询列显示名称)
//		param.autoCompleteList.add("citycode_cityname_city_citycode_cityname_城市代码_城市名称");
//		return param;
//	}
	
	@SuppressWarnings("unused")
	private ServiceParam _jcyc_flightnoterminal(TableInfo tableInfo){
		ServiceParam param = new ServiceParam();
		//模型的中文描述
		param.modelChineseName = "航班号航站楼关系";
		//模型字段的中文描述(不指定默认为数据库的字段描述)
//		param.fieldChineseNameMap.put("orgcode", "机构代码");
//		param.fieldChineseNameMap.put("orgname", "机构名称");
		//查询条件
		param.searchParamList.add("flightno");
		//查询时不显示的列
		param.noShowList.add("usercode");
		param.noShowList.add("username");
		param.noShowList.add("updatetime");
		param.noShowList.add("createtime");
		//修改时不显示的列
		param.noUpdateShowList.add("usercode");
		param.noUpdateShowList.add("username");
		param.noUpdateShowList.add("updatetime");
		param.noUpdateShowList.add("createtime");
		//修改时不能为空的列
		param.mustFieldList.add("flightno");
		param.mustFieldList.add("tripplanid");
		//修改时显示为 textarea 的列
//		param.textAreaList.add("reasonname");
		//数据库不修改的列
		param.noUpdateList.add("createtime");
		
		//使用数据字典的列(显示为下拉列表)(列名_描述列名,没有描述列则写null)
//		param.selectFieldList.add("isactive|null");
		//使用统计表的列(显示为下拉列表)(列名|描述列名|统计的表名|作为value的列名|作为text的列名)
//		param.selectFieldByCountList.add("citycode|cityname|city|citycode|cityname");
		//使用自动补全的列(自动提示)(列名|描述列名|要查询的主表名称|主表条件列名|主表查询列名|主表条件列显示名称|主表查询列显示名称)
		param.autoCompleteList.add("tripplanid|null|jcyc_terminal|id|terminalname|id|航站楼名称");
		return param;
	}
	
	@SuppressWarnings("unused")
	private ServiceParam _businessvehicletype(TableInfo tableInfo){
		ServiceParam param = new ServiceParam();
		//模型的中文描述
		param.modelChineseName = "业务车型关系";
		//模型字段的中文描述(不指定默认为数据库的字段描述)
//		param.fieldChineseNameMap.put("orgcode", "机构代码");
//		param.fieldChineseNameMap.put("orgname", "机构名称");
		//查询条件
		param.searchParamList.add("businesscode");
		param.searchParamList.add("vehicletypeid");
		//查询时不显示的列
		param.noShowList.add("usercode");
		param.noShowList.add("username");
		param.noShowList.add("updatetime");
		param.noShowList.add("createtime");
		//修改时不显示的列
		param.noUpdateShowList.add("businessname");
		param.noUpdateShowList.add("vttypename");
		param.noUpdateShowList.add("usercode");
		param.noUpdateShowList.add("username");
		param.noUpdateShowList.add("updatetime");
		param.noUpdateShowList.add("createtime");
		//修改时不能为空的列
		param.mustFieldList.add("businesscode");
		param.mustFieldList.add("vehicletypeid");
		//修改时显示为 textarea 的列
//		param.textAreaList.add("reasonname");
		//数据库不修改的列
		param.noUpdateList.add("createtime");
		
		//使用数据字典的列(显示为下拉列表)(列名_描述列名,没有描述列则写null)
//		param.selectFieldList.add("isactive|null");
		//使用统计表的列(显示为下拉列表)(列名|描述列名|统计的表名|作为value的列名|作为text的列名)
		param.selectFieldByCountList.add("businesscode|businessname|business|businesscode|businessname");
		//使用自动补全的列(自动提示)(列名|描述列名|要查询的主表名称|主表条件列名|主表查询列名|主表条件列显示名称|主表查询列显示名称)
		param.autoCompleteList.add("vehicletypeid|vttypename|vehicletype|id|vttypename|id|车型名称");
		return param;
	}
	
	@SuppressWarnings("unused")
	private ServiceParam _sellnum(TableInfo tableInfo){
		ServiceParam param = new ServiceParam();
		//模型的中文描述
		param.modelChineseName = "售票信息";
		//模型字段的中文描述(不指定默认为数据库的字段描述)
//		param.fieldChineseNameMap.put("orgcode", "机构代码");
//		param.fieldChineseNameMap.put("orgname", "机构名称");
		//查询条件
		param.searchParamList.add("selldate");
		//查询时不显示的列
		param.noShowList.add("usercode");
		param.noShowList.add("username");
		param.noShowList.add("updatetime");
		param.noShowList.add("createtime");
		//修改时不显示的列
		param.noUpdateShowList.add("usercode");
		param.noUpdateShowList.add("username");
		param.noUpdateShowList.add("updatetime");
		param.noUpdateShowList.add("createtime");
		//修改时不能为空的列
		param.mustFieldList.add("selldate");
		param.mustFieldList.add("sellnum");
		//修改时显示为 textarea 的列
//		param.textAreaList.add("reasonname");
		//数据库不修改的列
		param.noUpdateList.add("createtime");
		
		//使用数据字典的列(显示为下拉列表)(列名_描述列名,没有描述列则写null)
//		param.selectFieldList.add("isactive|null");
		//使用统计表的列(显示为下拉列表)(列名|描述列名|统计的表名|作为value的列名|作为text的列名)
//		param.selectFieldByCountList.add("businesscode|businessname|business|businesscode|businessname");
		//使用自动补全的列(自动提示)(列名|描述列名|要查询的主表名称|主表条件列名|主表查询列名|主表条件列显示名称|主表查询列显示名称)
//		param.autoCompleteList.add("vehicletypeid|vttypename|vehicletype|id|vttypename|id|车型名称");
		return param;
	}
}