package work.protec.bus365.codegenerate.patchmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import work.codegenerate.base.CodeGenerate;
import work.codegenerate.base.CommonUtils;
import work.codegenerate.base.model.TableInfo;

public class ServiceParam {
	
	public static void main(String[] args) {
		CodeGenerate.run(ServiceParam.class);
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
		}else{
			//去掉表前缀,按_分割,首字母大写
			modelName = new CommonUtils().getModelNameByTableName(tableName,tablePrefix);
		}
		return modelName;
	}
	
	/**
	 * 获取业务相关参数
	 * @author likaihao
	 * @date 2016年8月4日 上午11:19:13
	 * @param tableName
	 * @return
	 */
	public Map<String,Object> getServiceParamMap(TableInfo tableInfo){
		Map<String,Object> serviceParamMap = new HashMap<String,Object>();
		
		//模型的中文描述
		String modelChineseName = null;
		//模型字段的中文描述
		Map<String,String> fieldChineseNameMap = new HashMap<String,String>(); 
		//查询条件
		List<String> searchParamList = new ArrayList<String>();
		//不能为空的列
		List<String> mustFieldList = new ArrayList<String>();
		//修改时显示为areatext的列
		List<String> textAreaList = new ArrayList<String>();
		//修改时不显示的列
		List<String> noUpdateShowList = new ArrayList<String>();
		//使用数据字典的列(显示为下拉列表)
		List<String> selectFieldList = new ArrayList<String>();
		//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
		List<String> autoCompleteList = new ArrayList<String>();
		
		String tableName = tableInfo.getTableName();
		String modelName = getModelName(tableName,"");
		if(modelName.equals("ServerClient")){
			//模型的中文描述
			modelChineseName = "服务器信息";
			//模型字段的中文描述(不指定默认为数据库的字段描述)
			fieldChineseNameMap.put("id", "id");
			fieldChineseNameMap.put("name", "服务器名称");
			fieldChineseNameMap.put("ip", "服务器ip");
			fieldChineseNameMap.put("username", "用户名");
			fieldChineseNameMap.put("password", "密码");
			fieldChineseNameMap.put("contexttype", "环境类型");
			//查询条件
			searchParamList.add("name");
			searchParamList.add("contexttype");
			//不能为空的列
			mustFieldList.add("name");
			mustFieldList.add("ip");
			mustFieldList.add("username");
			mustFieldList.add("password");
			mustFieldList.add("contexttype");
			//修改时显示为 textarea 的列
			//textAreaList.add("packDir");
			//使用数据字典的列(显示为下拉列表)
			selectFieldList.add("contexttype");
		}else if(modelName.equals("RemoteProject")){
			//模型的中文描述
			modelChineseName = "远程项目信息";
			//模型字段的中文描述
			fieldChineseNameMap.put("id", "id");
			fieldChineseNameMap.put("appid", "项目名称");
			fieldChineseNameMap.put("serverid", "服务器");
			fieldChineseNameMap.put("path", "项目路径");
			fieldChineseNameMap.put("commandgroupid", "命令组");
			fieldChineseNameMap.put("svnconfigpath", "svn配置文件路径");
			//查询条件
			searchParamList.add("appid");
			searchParamList.add("serverid");
			//不能为空的列
			mustFieldList.add("appid");
			mustFieldList.add("serverid");
			mustFieldList.add("path");
			mustFieldList.add("commandgroupid");
			mustFieldList.add("svnconfigpath");
			//修改时显示为 textarea 的列
			//textAreaList.add("packDir");
			//使用数据字典的列(显示为下拉列表)
			//selectFieldList.add("contexttype");
			//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
			autoCompleteList.add("serverid_ServerClient_id_name_id_服务器名称");
			autoCompleteList.add("commandgroupid_CommandGroup_id_name_id_命令组名称");
		}else if(modelName.equals("CommandGroup")){
			//模型的中文描述
			modelChineseName = "命令组信息";
			//模型字段的中文描述
			fieldChineseNameMap.put("id", "id");
			fieldChineseNameMap.put("name", "名称");
			fieldChineseNameMap.put("createtime", "创建时间");
			//查询条件
			searchParamList.add("name");
			//不能为空的列
			mustFieldList.add("name");
			mustFieldList.add("createtime");
			//修改时显示为 textarea 的列
			//textAreaList.add("packDir");
			//修改时不显示的列
			noUpdateShowList.add("createtime");
			//使用数据字典的列(显示为下拉列表)
			//selectFieldList.add("contexttype");
			//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
			//autoCompleteList.add("serverid_ServerClient_id_name_id_服务器名称");
			//autoCompleteList.add("commandgroupid_CommandGroup_id_name_id_命令组名称");
		}else if(modelName.equals("Command")){
			//模型的中文描述
			modelChineseName = "远程命令信息";
			//模型字段的中文描述
			fieldChineseNameMap.put("id", "id");
			fieldChineseNameMap.put("name", "名称");
			fieldChineseNameMap.put("command", "命令");
			fieldChineseNameMap.put("groupid", "命令组");
			fieldChineseNameMap.put("remark", "说明");
			//查询条件
			searchParamList.add("name");
			searchParamList.add("command");
			searchParamList.add("groupid");
			//不能为空的列
			mustFieldList.add("name");
			mustFieldList.add("command");
			mustFieldList.add("groupid");
			//修改时显示为 textarea 的列
			textAreaList.add("remark");
			//使用数据字典的列(显示为下拉列表)
			//selectFieldList.add("contexttype");
			//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
			autoCompleteList.add("groupid_CommandGroup_id_name_id_命令组名称");
		}else if(modelName.equals("Publishlog")){
		}else if(modelName.equals("ProjectGroup")){
			//模型的中文描述
			modelChineseName = "项目组信息";
			//模型字段的中文描述
			fieldChineseNameMap.put("id", "id");
			fieldChineseNameMap.put("groupname", "项目组名称");
			fieldChineseNameMap.put("remark", "备注");
			fieldChineseNameMap.put("createtime", "创建时间");
			//查询条件
			searchParamList.add("groupname");
			//不能为空的列
			mustFieldList.add("groupname");
			//修改时不显示的列
			noUpdateShowList.add("createtime");
			//修改时显示为 textarea 的列
			textAreaList.add("remark");
			//使用数据字典的列(显示为下拉列表)
			//selectFieldList.add("contexttype");
			//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
			//autoCompleteList.add("groupid_CommandGroup_id_name_id_命令组名称");
		}else if(modelName.equals("Project")){
			//模型的中文描述
			modelChineseName = "本地项目信息";
			//模型字段的中文描述
			fieldChineseNameMap.put("Id","Id");
			fieldChineseNameMap.put("appid","项目名称");
			fieldChineseNameMap.put("groupid","项目组");
			fieldChineseNameMap.put("path","项目路径");
			fieldChineseNameMap.put("isactive","是否可用");
			fieldChineseNameMap.put("usercode","操作员code");
			fieldChineseNameMap.put("username","操作员名称");
			fieldChineseNameMap.put("remark","备注");
			fieldChineseNameMap.put("updatetime","修改时间");
			fieldChineseNameMap.put("createtime","创建时间");
			//查询条件
			searchParamList.add("appid");
			searchParamList.add("groupid");
			//不能为空的列
			mustFieldList.add("appid");
			mustFieldList.add("isactive");
			//修改时不显示的列
			noUpdateShowList.add("usercode");
			noUpdateShowList.add("username");
			noUpdateShowList.add("updatetime");
			noUpdateShowList.add("createtime");
			//修改时显示为 textarea 的列
			textAreaList.add("remark");
			//使用数据字典的列(显示为下拉列表)
			selectFieldList.add("isactive");
			//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
			autoCompleteList.add("groupid_ProjectGroup_id_groupname_id_项目组名称");
		}else if(modelName.equals("ProjectVersion")){
			//模型的中文描述
			modelChineseName = "项目组版本信息";
			//模型字段的中文描述
			fieldChineseNameMap.put("Id","Id");
			fieldChineseNameMap.put("version","版本");
			fieldChineseNameMap.put("projectgroupid","项目组");
			fieldChineseNameMap.put("status","状态");
			fieldChineseNameMap.put("isactive","是否可用");
			fieldChineseNameMap.put("usercode","操作员code");
			fieldChineseNameMap.put("username","操作员名称");
			fieldChineseNameMap.put("remark","备注");
			//查询条件
			searchParamList.add("projectgroupid");
			searchParamList.add("version");
			//不能为空的列
			mustFieldList.add("version");
			mustFieldList.add("projectgroupid");
			mustFieldList.add("status");
			mustFieldList.add("usercode");
			//修改时不显示的列
			noUpdateShowList.add("usercode");
			noUpdateShowList.add("username");
			noUpdateShowList.add("status");
			//修改时显示为 textarea 的列
			textAreaList.add("remark");
			//使用数据字典的列(显示为下拉列表)
			selectFieldList.add("isactive");
			//使用自动补全的列(自动提示)(列名_主表对应的model名_外键名_要查询的主表的列名_外键显示名称_要查询的主表的列的显示名称)
			autoCompleteList.add("projectgroupid_ProjectGroup_id_groupname_id_项目组名称");
		}else{
			throw new RuntimeException("没有指定modelName的业务参数,"+modelName);
		}
		
		//将没有指定中文描述的列 设置为数据库的注释
		for(String columnName : tableInfo.getColumnInfoMap().keySet()){
			if(!fieldChineseNameMap.containsKey(columnName)){
				String comment = tableInfo.getColumnInfoMap().get(columnName).getComment();
				fieldChineseNameMap.put(columnName, comment);
			}
		}

		serviceParamMap.put("modelChineseName", modelChineseName);
		serviceParamMap.put("fieldChineseNameMap", fieldChineseNameMap);
		serviceParamMap.put("searchParamList", searchParamList);
		serviceParamMap.put("mustFieldList", mustFieldList);
		serviceParamMap.put("textAreaList", textAreaList);
		serviceParamMap.put("noUpdateShowList", noUpdateShowList);
		serviceParamMap.put("selectFieldList", selectFieldList);
		serviceParamMap.put("autoCompleteList", autoCompleteList);
		return serviceParamMap;
	}
	
	
}